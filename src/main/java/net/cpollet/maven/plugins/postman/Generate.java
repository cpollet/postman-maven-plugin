package net.cpollet.maven.plugins.postman;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.cpollet.maven.plugins.postman.backend.adapters.ClassAdapter;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import net.cpollet.maven.plugins.postman.frontend.postman.Postman;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE)
public class Generate extends AbstractMojo {
    @Parameter(property = "project.build.finalName", required = true, readonly = true)
    private String finalName;

    @Parameter(property = "project.build.directory", required = true, readonly = true)
    private String directory;

    @Parameter(property = "project.packaging", required = true, readonly = true)
    private String packaging;

    @Parameter(name = "packagesToScan", required = true)
    private String[] packagesToScan;

    @Parameter(name = "baseUrl")
    private String baseUrl;

    @Parameter(name = "basicAuth")
    private BasicAuth basicAuth;

    public void execute() throws MojoExecutionException {
        if (basicAuth == null) {
            basicAuth = new BasicAuth();
        }

        File jarFile = new File(String.format("%s/%s.%s", directory, finalName, packaging));

        if (!jarFile.exists()) {
            throw new MojoExecutionException(String.format("File %s does not exist", jarFile.getAbsolutePath()));
        }

        getLog().debug(String.format("Final name: %s/%s.%s", directory, finalName, packaging));

        List<Endpoint> endpoints = getClassesToScan(classLoader(jarFile)).stream()
                .map(c -> new ClassAdapter(c).getEndpoints())
                .flatMap(List::stream)
                .map(e -> e.withBaseUrl(baseUrl))
                .map(e -> e.withAuthentication(basicAuth.getUsername(), basicAuth.getPassword()))
                .collect(Collectors.toList());

        File destinationFile = new File(String.format("%s/%s.json", directory, finalName));
        String result = new Postman(finalName, endpoints).generate();

        try {
            Files.write(
                    destinationFile.toPath(),
                    result.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new MojoExecutionException("Unable to write to " + destinationFile.getAbsolutePath(), e);
        }
    }

    private ClassLoader classLoader(File jarFile) {
        try {
            return new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, getClass().getClassLoader());
        } catch (MalformedURLException e) {
            // should never happen
            throw new RuntimeException(e);
        }
    }

    private List<Class<?>> getClassesToScan(ClassLoader classLoader) {
        List<Class<?>> classesToScan = new ArrayList<>();

        new FastClasspathScanner(packagesToScan)
                .overrideClassLoaders(classLoader)
                .matchClassesWithAnnotation(Path.class, classesToScan::add)
                .scan();

        return classesToScan;
    }
}

package net.cpollet.maven.plugins.postman;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.cpollet.maven.plugins.postman.backend.adapters.ClassAdapter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.ws.rs.Path;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE)
public class Generate extends AbstractMojo {
    @Parameter(property = "project.build.finalName", required = true, readonly = true)
    private String finalName;

    @Parameter(property = "project.build.directory", required = true, readonly = true)
    private String directory;

    @Parameter(property = "project.packaging", required = true, readonly = true)
    private String packaging;

    @Parameter(name = "baseUrl", defaultValue = "http://localhost:8000")
    private String baseUrl;

    @Parameter(name = "packagesToScan", required = true)
    private String[] packagesToScan;

    public void execute() throws MojoExecutionException {
        File jarFile = new File(String.format("%s/%s.%s", directory, finalName, packaging));

        if (!jarFile.exists()) {
            throw new MojoExecutionException(String.format("File %s does not exist", jarFile.getAbsolutePath()));
        }

        getLog().debug(String.format("Final name: %s/%s.%s", directory, finalName, packaging));

        ClassLoader classLoader = classLoader(jarFile);

        List<Class<?>> classesToScan = new ArrayList<>();

        new FastClasspathScanner(packagesToScan)
                //.verbose()
                .overrideClassLoaders(classLoader)
                .matchClassesWithAnnotation(Path.class, classesToScan::add)
                .scan();

        classesToScan.stream()
                .map(c -> new ClassAdapter(c).getEndpoints())
                .flatMap(List::stream)
                .map(e -> e.withBaseUrl(baseUrl))
                .forEach(e -> getLog().info(e.toString()));
    }

    private ClassLoader classLoader(File jarFile) {
        try {
            return new URLClassLoader(new URL[]{jarFile.toURI().toURL()}, getClass().getClassLoader());
        } catch (MalformedURLException e) {
            // should never happen
            throw new RuntimeException(e);
        }
    }
}

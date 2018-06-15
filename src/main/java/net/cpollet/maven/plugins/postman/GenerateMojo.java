package net.cpollet.maven.plugins.postman;

import net.cpollet.maven.plugins.postman.backend.ClassScanner;
import net.cpollet.maven.plugins.postman.backend.adapters.ClassesAdapter;
import net.cpollet.maven.plugins.postman.frontend.api.Context;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import net.cpollet.maven.plugins.postman.frontend.postman.Postman;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates a postman collection in project build directory.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.COMPILE)
public class GenerateMojo extends AbstractMojo {
    @Parameter(property = "project.compileClasspathElements", readonly = true, required = true)
    private List<String> compilePath;

    @Parameter(property = "project.build.finalName", required = true, readonly = true)
    private String finalName;

    @Parameter(property = "project.build.directory", required = true, readonly = true)
    private String directory;

    /**
     * List of packages to scan. If not provided, the whole artifact is scanned.
     */
    @Parameter(name = "packagesToScan", defaultValue = "${postman.packagesToScan}")
    private List<String> packagesToScan;

    @Parameter(name = "environments")
    private List<Environment> environments;

    @Parameter(defaultValue = "${postman.baseUrl}", readonly = true)
    private URL baseUrl;

    @Parameter(defaultValue = "${postman.basicAuth.username}", readonly = true)
    private String basicAuthUsername;

    @Parameter(defaultValue = "${postman.basicAuth.password}", readonly = true)
    private String basicAuthPassword;

    public void execute() throws MojoExecutionException {
        if (environments == null) {
            environments = new ArrayList<>();
        }

        if (environments.isEmpty()) {
            environments.add(new Environment(null, baseUrl, new BasicAuth(basicAuthUsername, basicAuthPassword)));
        }

        getLog().debug(String.join(", ", environments.stream().map(Environment::toString).collect(Collectors.toList())));

        List<Endpoint> endpoints = new ClassesAdapter(classesToScan()).getEndpoints();
        Map<String, Context> contexts = new EnvironmentsAdapter(environments).contexts();

        String result = new Postman(finalName, endpoints, contexts).generate();

        File destinationFile = new File(String.format("%s/%s.json", directory, finalName));
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

    private List<Class> classesToScan() {
        List<Class> classesToScan = new ClassScanner(compilePath, packagesToScan).find();

        getLog().debug(String.format("Classes to scan: %s", String.join(", ", classesToScan.stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toList())
        )));

        return classesToScan;
    }
}

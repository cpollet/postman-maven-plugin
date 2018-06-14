package net.cpollet.maven.plugins.postman;

import net.cpollet.maven.plugins.postman.backend.ClassScanner;
import net.cpollet.maven.plugins.postman.backend.adapters.ClassAdapter;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a postman collection in project build directory.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PACKAGE,
        requiresDependencyResolution = ResolutionScope.COMPILE)
public class Generate extends AbstractMojo {
    @Parameter(property = "project.compileClasspathElements", readonly = true, required = true)
    private List<String> compilePath;

    @Parameter(property = "project.build.finalName", required = true, readonly = true)
    private String finalName;

    @Parameter(property = "project.build.directory", required = true, readonly = true)
    private String directory;

    @Parameter(property = "project.packaging", required = true, readonly = true)
    private String packaging;

    /**
     * List of packages to scan. If not provided, the whole artifact is scanned.
     */
    @Parameter(name = "packagesToScan", defaultValue = "${postman.packagesToScan}")
    private List<String> packagesToScan;

    /**
     * The base URL to use when generating the postman collections. This has to be a valid URL.
     */
    @Parameter(name = "baseUrl", defaultValue = "${postman.baseUrl}", required = true)
    private URL baseUrl;

    /**
     * The basic auth username and password to use, if any. It contains two fields, <code>username</code> and
     * <code>password</code> (see {@link BasicAuth}).
     */
    @Parameter(name = "basicAuth", defaultValue = "${postman.basicAuth}")
    private BasicAuth basicAuth;

    public void execute() throws MojoExecutionException {
        if (basicAuth == null) {
            basicAuth = new BasicAuth();
        }

        getLog().debug(String.format("Classpath: %s", String.join(", ", compilePath)));
        getLog().debug(String.format("Scan packages: %s", String.join(", ", packagesToScan)));
        getLog().debug(String.format("Base URL: %s", baseUrl));
        getLog().debug(String.format("Final name: %s/%s.%s", directory, finalName, packaging));

        List<Class> classesToScan = new ClassScanner(compilePath, packagesToScan).find();

        getLog().debug(String.format("Classes to scan: %s", String.join(", ", classesToScan.stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toList())
        )));

        List<Endpoint> endpoints = classesToScan.stream()
                .map(c -> new ClassAdapter(c).getEndpoints())
                .flatMap(List::stream)
                .map(e -> e.withBaseUrl(baseUrl.toString()))
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
}

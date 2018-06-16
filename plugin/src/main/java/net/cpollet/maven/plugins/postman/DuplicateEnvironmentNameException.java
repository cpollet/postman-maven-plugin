package net.cpollet.maven.plugins.postman;

import org.apache.maven.plugin.MojoExecutionException;

import java.util.List;
import java.util.stream.Collectors;

public class DuplicateEnvironmentNameException extends MojoExecutionException {
    public DuplicateEnvironmentNameException(List<String> duplicates) {
        super(String.format(
                "Duplicate environment name found: %s",
                String.join(
                        ", ",
                        duplicates.stream()
                                .sorted()
                                .collect(Collectors.toList()))
                )
        );
    }
}

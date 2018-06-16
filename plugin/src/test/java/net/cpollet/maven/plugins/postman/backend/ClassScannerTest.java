package net.cpollet.maven.plugins.postman.backend;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ClassScannerTest {
    private static final String API_JAR = "/api.jar";

    @Test
    public void find_returnsClassWithAnnotation() {
        // GIVEN
        List<String> path = Collections.singletonList(resourcePath(API_JAR));
        ClassScanner scanner = new ClassScanner(path, Collections.emptyList());

        // WHEN
        List<Class> classes = scanner.find();

        // THEN
        List<String> classNames = classes.stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toList());

        Assertions.assertThat(classNames)
                .contains("net.cpollet.maven.plugins.postman.it.api.Api");
    }

    @Test
    public void find_doesNotReturnClassesOutsideOfPackage() {
        // GIVEN
        List<String> path = Collections.singletonList(resourcePath(API_JAR));
        ClassScanner scanner = new ClassScanner(path, Collections.singletonList("com"));

        // WHEN
        List<Class> classes = scanner.find();

        // THEN
        List<String> classNames = classes.stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toList());

        Assertions.assertThat(classNames)
                .doesNotContain("net.cpollet.maven.plugins.postman.it.api.Api");
    }

    @Test
    public void find_doesNotReturnClassesWithoutAnnotation() {
        // GIVEN
        List<String> path = Collections.singletonList(resourcePath(API_JAR));
        ClassScanner scanner = new ClassScanner(path, Collections.emptyList());

        // WHEN
        List<Class> classes = scanner.find();

        // THEN
        List<String> classNames = classes.stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toList());

        Assertions.assertThat(classNames)
                .doesNotContain("net.cpollet.maven.plugins.postman.it.api.User");
    }

    private String resourcePath(String resource) {
        return getClass().getResource(resource).getPath();
    }
}

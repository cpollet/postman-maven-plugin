package net.cpollet.maven.plugins.postman.backend;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import lombok.AllArgsConstructor;

import javax.ws.rs.Path;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ClassScanner {
    private final List<String> paths;
    private final List<String> packagesToScan;

    public List<Class> find() {
        List<Class> classes = new ArrayList<>();

        new FastClasspathScanner(packagesToScan.toArray(new String[0]))
                .overrideClassLoaders(classLoader())
                .matchClassesWithAnnotation(Path.class, classes::add)
                .scan();

        return classes;
    }

    private ClassLoader classLoader() {
        URL[] urls = paths.stream()
                .map(this::url)
                .toArray(URL[]::new);

        return new URLClassLoader(urls, getClass().getClassLoader());
    }

    private URL url(String path) {
        try {
            return new File(path).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

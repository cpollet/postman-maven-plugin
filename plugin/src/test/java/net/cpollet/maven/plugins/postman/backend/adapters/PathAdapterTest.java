package net.cpollet.maven.plugins.postman.backend.adapters;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.Path;
import java.lang.annotation.Annotation;

public class PathAdapterTest {
    @Test
    public void path_prependSlash_whenNotPresent() {
        // GIVEN
        PathAdapter adapter = adapt("index");

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/index");
    }

    @Test
    public void path_doesNotPrependSlash_whenPresent() {
        // GIVEN
        PathAdapter adapter = adapt("/list");

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/list");
    }

    @Test
    public void path_removesTrailingSlashes() {
        // GIVEN
        PathAdapter adapter = adapt("/error///");

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/error");
    }

    @Test
    public void path_returnsEmptyString_whenPathIsNull() {
        PathAdapter adapter = adapt(null);

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("");
    }

    @Test
    public void path_returnsEmptyString_whenPathIsEmpty() {
        PathAdapter adapter = adapt("");

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("");
    }

    @Test
    public void path_returnsEmptyString_whenNoPath() {
        PathAdapter adapter = new PathAdapter(null);

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("");
    }

    private PathAdapter adapt(String path) {
        return new PathAdapter(path(path));
    }

    private Path path(final String path) {
        return new Path() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Path.class;
            }

            @Override
            public String value() {
                return path;
            }
        };
    }
}

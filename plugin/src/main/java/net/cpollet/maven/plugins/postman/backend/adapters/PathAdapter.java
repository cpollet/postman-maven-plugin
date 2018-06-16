package net.cpollet.maven.plugins.postman.backend.adapters;

import lombok.AllArgsConstructor;

import javax.ws.rs.Path;

@AllArgsConstructor
public class PathAdapter {
    private final Path annotation;

    /**
     * Returns the path, prefixed with / and not suffixed. Example: /index. If not path specified, returns an empty
     * string.
     *
     * @return the path
     */
    public String getPath() {
        //noinspection ConstantConditions
        if (annotation == null || annotation.value() == null) {
            return "";
        }

        return removeTrailingSlashes(
                prefixWithSlash(
                        annotation.value()
                )
        );
    }

    private String removeTrailingSlashes(String path) {
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    private String prefixWithSlash(String path) {
        if (path.startsWith("/")) {
            return path;
        }

        return "/" + path;
    }
}

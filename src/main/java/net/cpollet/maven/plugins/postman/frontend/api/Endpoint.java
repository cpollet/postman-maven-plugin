package net.cpollet.maven.plugins.postman.frontend.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class Endpoint {
    private final Verb verb;
    private final String path;
    private final Class bodyType;
    private final List<String> queryParametersNames;
    private final Class responseType;
    private final String base;

    public Endpoint(Verb verb, String path, Class bodyType, List<String> queryParametersNames, Class responseType) {
        this(verb, path, bodyType, queryParametersNames, responseType, "");
    }

    public Endpoint withBaseUrl(String baseUrl) {
        return new Endpoint(verb, path, bodyType, queryParametersNames, responseType, removeTrailingSlashes(baseUrl));
    }

    private String removeTrailingSlashes(String path) {
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    public String getUrl() {
        return base + path;
    }

    public enum Verb {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH
    }
}

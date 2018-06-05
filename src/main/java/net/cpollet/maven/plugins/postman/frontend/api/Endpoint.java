package net.cpollet.maven.plugins.postman.frontend.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class Endpoint {
    private final String name;
    private final Verb verb;
    private final String path;
    private final Class bodyType;
    private final List<String> queryParametersNames;
    private final Class responseType;
    private final String base;
    private final String username;
    private final String password;

    public Endpoint(String name, Verb verb, String path, Class bodyType, List<String> queryParametersNames, Class responseType) {
        this(name, verb, path, bodyType, queryParametersNames, responseType, "", "", "");
    }

    public Endpoint withBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isEmpty()) {
            return this;
        }

        return new Endpoint(name, verb, path, bodyType, queryParametersNames, responseType, removeTrailingSlashes(baseUrl), username, password);
    }

    public Endpoint withAuthentication(String username, String password) {
        return new Endpoint(name, verb, path, bodyType, queryParametersNames, responseType, base, username, password);
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

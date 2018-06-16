package net.cpollet.maven.plugins.postman;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EnvironmentAdapter {
    private static final String DEFAULT_NAME = "default";
    private static final String DEFAULT_URL = "http://localhost";

    private final Environment environment;

    public String getBaseUrl() {
        if (environment.getBaseUrl() == null) {
            return DEFAULT_URL;
        }
        return environment.getBaseUrl().toExternalForm();
    }

    public String getName() {
        if (environment.getName() == null || environment.getName().trim().isEmpty()) {
            return DEFAULT_NAME;
        }

        return environment.getName();
    }

    public String getUsername() {
        if (environment.getBasicAuth() == null) {
            return null;
        }

        return environment.getBasicAuth().getUsername();
    }

    public String getPassword() {
        if (environment.getBasicAuth() == null) {
            return null;
        }

        return environment.getBasicAuth().getPassword();
    }
}

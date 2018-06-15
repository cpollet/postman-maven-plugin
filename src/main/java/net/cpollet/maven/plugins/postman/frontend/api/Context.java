package net.cpollet.maven.plugins.postman.frontend.api;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Builder
@Getter
public class Context {
    private static final String SLASH = "/";

    private final String baseUrl;
    private final String username;
    private final String password;

    public String getBaseUrl() {
        return StringUtils.stripEnd(baseUrl, SLASH);
    }
}

package net.cpollet.maven.plugins.postman.frontend.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@AllArgsConstructor
@ToString
@Getter
public class Endpoint {
    private static final String SLASH = "/";

    private final String group;
    private final String name;
    private final Verb verb;
    private final String path;
    private final Class bodyType;
    private final List<String> queryParametersNames;
    private final Class responseType;

    public String getGroup() {
        if (group == null) {
            return "__default";
        }

        return group;
    }

    /**
     * Returns the path, starting with slash and finishing without slash
     *
     * @return the path
     */
    public String getPath() {
        return SLASH + trimSlashes(path);
    }

    private String trimSlashes(String str) {
        return StringUtils.stripEnd(StringUtils.stripStart(str, SLASH), SLASH);
    }

    public enum Verb {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH
    }
}

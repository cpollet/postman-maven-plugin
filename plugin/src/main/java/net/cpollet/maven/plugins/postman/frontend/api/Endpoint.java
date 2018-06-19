package net.cpollet.maven.plugins.postman.frontend.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@ToString
@Getter
public class Endpoint implements Comparable<Endpoint> {
    private static final String SLASH = "/";

    private final String group;
    private final String name;
    private final List<Class> parameterTypes;
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

    @Override
    public int compareTo(Endpoint other) {
        String signature = signature(this);
        String otherSignature = signature(other);

        return signature.compareTo(otherSignature);
    }

    private String signature(Endpoint e) {
        List<String> types = e.parameterTypes.stream()
                .map(Class::getCanonicalName)
                .collect(Collectors.toList());

        return String.format("%s#%s(%s)", e.group, e.name, String.join(",", types));
    }

    public enum Verb {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH
    }
}

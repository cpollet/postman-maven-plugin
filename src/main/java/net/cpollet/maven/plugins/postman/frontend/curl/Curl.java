package net.cpollet.maven.plugins.postman.frontend.curl;

import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.JsonExample;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.codehaus.plexus.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class Curl {
    private final static Map<Endpoint.Verb, String> verbFlags = new HashMap<Endpoint.Verb, String>() {{
        put(Endpoint.Verb.GET, "--get ");
        put(Endpoint.Verb.POST, "-X POST "); // --data
        put(Endpoint.Verb.PUT, "-X PUT "); // or -T with file
        put(Endpoint.Verb.DELETE, "-X DELETE ");
        put(Endpoint.Verb.HEAD, "--head ");
        put(Endpoint.Verb.OPTIONS, "-X OPTIONS ");
        put(Endpoint.Verb.PATCH, "-X PATCH ");
    }};

    private final Endpoint endpoint;

    public String generate() {
        return String.format("curl %s%s%s%s'%s'", auth(), verb(), queryParameters(), body(), endpoint.getUrl());
    }

    private String auth() {
        if (!StringUtils.isEmpty(endpoint.getUsername()) && !StringUtils.isEmpty(endpoint.getPassword())) {
            return String.format("-u %s:%s ", endpoint.getUsername(), endpoint.getPassword());
        }

        return "";
    }


    private String body() {
        if (endpoint.getBodyType() == null) {
            return "";
        }

        return "-d '" + JsonExample.from(endpoint.getBodyType()).generate() + "' ";
    }

    private String queryParameters() {
        if (endpoint.getQueryParametersNames().isEmpty()) {
            return "";
        }

        return String.join("",
                endpoint.getQueryParametersNames().stream()
                        .map(p -> String.format("-d %s=... ", p))
                        .collect(Collectors.toList())
        );
    }

    private String verb() {
        return verbFlags.get(endpoint.getVerb());
    }
}

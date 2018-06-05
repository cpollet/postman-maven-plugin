package net.cpollet.maven.plugins.postman.frontend.curl;

import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.Generator;
import net.cpollet.maven.plugins.postman.frontend.JsonExample;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.codehaus.plexus.util.StringUtils;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class Curl implements Generator {
    private static final Map<Endpoint.Verb, String> verbFlags = Collections.unmodifiableMap(Stream.of(
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.GET, "--get "),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.POST, "-X POST "), // --data
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.PUT, "-X PUT "), // or -T with file
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.DELETE, "-X DELETE "),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.HEAD, "--head "),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.OPTIONS, "-X OPTIONS "),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.PATCH, "-X PATCH ")
    ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

    private final Endpoint endpoint;

    @Override
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
        if (endpoint.getBodyType() == Void.class) {
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

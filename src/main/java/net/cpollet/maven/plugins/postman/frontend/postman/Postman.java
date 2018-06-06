package net.cpollet.maven.plugins.postman.frontend.postman;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.Generator;
import net.cpollet.maven.plugins.postman.frontend.JsonExample;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Auth;
import net.cpollet.maven.plugins.postman.frontend.postman.data.BasicAuthPassword;
import net.cpollet.maven.plugins.postman.frontend.postman.data.BasicAuthUsername;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Collection;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Information;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Item;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Request;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class Postman implements Generator {
    private static final Map<Endpoint.Verb, Request.Method> methods = Collections.unmodifiableMap(Stream.of(
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.GET, Request.Method.GET),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.POST, Request.Method.POST),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.PUT, Request.Method.PUT),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.DELETE, Request.Method.DELETE),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.HEAD, Request.Method.HEAD),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.OPTIONS, Request.Method.OPTIONS),
            new AbstractMap.SimpleEntry<>(Endpoint.Verb.PATCH, Request.Method.PATCH)
    ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

    private final String collectionName;
    private final List<Endpoint> endpoints;

    @Override
    public String generate() {
        ObjectMapper mapper = new ObjectMapper();

        Collection collection = Collection.builder()
                .info(Information.builder()
                        .name(collectionName)
                        .build())
                .items(
                        endpoints.stream()
                                .map(e -> Item.builder()
                                        .name(e.getName())
                                        .request(Request.builder()
                                                .url(url(e))
                                                .method(methods.get(e.getVerb()))
                                                .body(JsonExample.from(e.getBodyType()).generate())
                                                .auth(Auth.builder()
                                                        .basic(Arrays.asList(
                                                                BasicAuthUsername.builder().value(e.getUsername()).build(),
                                                                BasicAuthPassword.builder().value(e.getPassword()).build()
                                                        ))
                                                        .build())
                                                .build())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String url(Endpoint e) {
        if (e.getQueryParametersNames().isEmpty()) {
            return e.getUrl();
        }

        String parameters = String.join("&",
                e.getQueryParametersNames().stream()
                        .map(s -> String.format("%s=...", s))
                        .collect(Collectors.toList())
        );


        return String.format("%s?%s", e.getUrl(), parameters);
    }
}

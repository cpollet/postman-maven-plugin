package net.cpollet.maven.plugins.postman.frontend.postman;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.Generator;
import net.cpollet.maven.plugins.postman.frontend.JsonExample;
import net.cpollet.maven.plugins.postman.frontend.api.Context;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Auth;
import net.cpollet.maven.plugins.postman.frontend.postman.data.BasicAuthPassword;
import net.cpollet.maven.plugins.postman.frontend.postman.data.BasicAuthUsername;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Body;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Collection;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Folder;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Header;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Information;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Item;
import net.cpollet.maven.plugins.postman.frontend.postman.data.Request;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
    private final Map<String, Context> contexts;

    @Override
    public String generate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        Collection collection = Collection.builder()
                .info(information(collectionName))
                .item(contexts.entrySet().stream()
                        .map(c -> Folder.builder()
                                .name(c.getKey())
                                .item(folders(endpoints, c.getValue()))
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Folder> folders(List<Endpoint> endpoints, Context c) {
        return endpoints.stream()
                .collect(Collectors.groupingBy(Endpoint::getGroup))
                .entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(g -> Folder.builder()
                        .name(g.getKey())
                        .item(endpoints(g.getValue(), c))
                        .build()
                )
                .collect(Collectors.toList());
    }

    private Information information(String collectionName) {
        return Information.builder()
                .name(collectionName)
                .build();
    }

    private List<Item> endpoints(List<Endpoint> endpoints, Context c) {
        return endpoints.stream()
                .map(e -> Item.builder()
                        .name(e.getName())
                        .request(request(e, c))
                        .build())
                .collect(Collectors.toList());
    }

    private Request request(Endpoint e, Context c) {
        return Request.builder()
                .url(url(e, c))
                .method(method(e))
                .auth(auth(c))
                .header(header())
                .body(body(e))
                .build();
    }

    private String url(Endpoint e, Context c) {
        if (e.getQueryParametersNames().isEmpty()) {
            return c.getBaseUrl() + e.getPath();
        }

        String parameters = String.join("&",
                e.getQueryParametersNames().stream()
                        .map(s -> String.format("%s=...", s))
                        .collect(Collectors.toList())
        );

        return String.format("%s?%s", c.getBaseUrl() + e.getPath(), parameters);
    }

    private Request.Method method(Endpoint e) {
        return methods.get(e.getVerb());
    }

    private Auth auth(Context c) {
        if (c.getUsername() == null || c.getUsername().trim().equals("")) {
            return null;
        }

        return Auth.builder()
                .basic(Arrays.asList(
                        BasicAuthUsername.builder().value(c.getUsername()).build(),
                        BasicAuthPassword.builder().value(c.getPassword()).build()
                ))
                .build();
    }

    private List<Header> header() {
        return Collections.singletonList(
                Header.builder()
                        .key("Content-Type")
                        .value("application/json")
                        .build()
        );
    }

    private Body body(Endpoint e) {
        if (e.getBodyType() == Void.class) {
            return null;
        }

            return Body.builder()
                .raw(JsonExample.from(e.getBodyType()).generate())
                .build();
    }
}

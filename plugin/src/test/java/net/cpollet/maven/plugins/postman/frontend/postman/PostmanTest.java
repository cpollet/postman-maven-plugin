package net.cpollet.maven.plugins.postman.frontend.postman;

import lombok.Data;
import net.cpollet.maven.plugins.postman.frontend.api.Context;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PostmanTest {
    Map<String, Context> contexts = new HashMap<>();

    @Before
    public void setup() {
        contexts.put(
                "contextName",
                Context.builder()
                        .baseUrl("http://localhost")
                        .build()
        );
    }

    @Test
    public void generate_schema() {
        // GIVEN
        Postman postman = new Postman(
                "collectionName",
                Collections.emptyList(),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"schema\" : \"https://schema.getpostman.com/json/collection/v2.1.0/collection.json\"");
    }

    @Test
    public void generate_collectionName() {
        // GIVEN
        Postman postman = new Postman(
                "collectionName",
                Collections.emptyList(),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"name\" : \"collectionName\"");
    }

    @Test
    public void generate_contextName() {
        // GIVEN
        Postman postman = new Postman(
                "collectionName",
                Collections.emptyList(),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"name\" : \"contextName\"");
    }

    @Test
    public void generate_endpointName() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                "endpointName",
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"name\" : \"endpointName\"");
    }

    @Test
    public void generate_endpointUrl() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                "/path",
                Void.class,
                Collections.emptyList(),
                Void.class
        );


        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"url\" : \"http://localhost/path\"");
    }

    @Test
    public void generate_endpointMethod() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                Endpoint.Verb.GET,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"method\" : \"GET\"");
    }

    @Test
    public void generate_contentType() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                "endpointName",
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"key\" : \"Content-Type\"")
                .contains("\"value\" : \"application/json\"");
    }

    @Test
    public void generate_endpointPayload_whenSet() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                null,
                BodyPayload.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"body\" : {")
                .contains("\"mode\" : \"raw\"")
                .contains("\"raw\" : \"{\\n  \\\"username\\\": \\\"string\\\",\\n  \\\"password\\\": \\\"string\\\"\\n}\"");
    }

    @Test
    public void generate_endpointWithoutPayload_whenNotSet() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .doesNotContain("\"body\" :");
    }

    @Test
    public void generate_endpointGetParameters() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                "path",
                Void.class,
                Arrays.asList("param1", "param2"),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("path?param1=...&param2=...");
    }

    @Test
    public void generate_endpointBasicAuth_whenPresent() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        contexts = new HashMap<>();
        contexts.put(
                "name",
                Context.builder()
                        .baseUrl("http://localhost")
                        .username("username")
                        .password("password")
                        .build()
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"auth\" : {")
                .contains("\"type\" : \"basic\"")
                .contains("\"key\" : \"username\"")
                .contains("\"value\" : \"username\"")
                .contains("\"key\" : \"password\"")
                .contains("\"value\" : \"password\"");
    }

    @Test
    public void generate_endpointNoBasicAuth_whenNotPresent() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .doesNotContain("\"auth\" :");
    }

    @Test
    public void generate_endpointGroup() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Collections.singletonList(endpoint),
                contexts
        );

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"name\" : \"__default\"");
    }

    @Test
    public void generate_groupsAreSorted() {
        // GIVEN
        Endpoint endpointA = new Endpoint(
                "endpointA",
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );
        Endpoint endpointB = new Endpoint(
                "endpointB",
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );
        Endpoint endpointC = new Endpoint(
                "endpointC",
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman(
                "collectionName",
                Arrays.asList(endpointA, endpointC, endpointB),
                contexts
        );

        // WHEN
        String result = postman.generate();
        int posA = result.indexOf("endpointA");
        int posB = result.indexOf("endpointB");
        int posC = result.indexOf("endpointC");

        // THEN
        Assertions.assertThat(posB)
                .isGreaterThan(posA);
        Assertions.assertThat(posC)
                .isGreaterThan(posB);
    }

    @Data
    public static class BodyPayload {
        private String username;
        private String password;
    }
}

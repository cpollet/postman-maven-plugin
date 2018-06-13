package net.cpollet.maven.plugins.postman.frontend.postman;

import lombok.Data;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class PostmanTest {
    @Test
    public void generate_schema() {
        // GIVEN
        Postman postman = new Postman("collectionName", Collections.emptyList());

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"schema\" : \"https://schema.getpostman.com/json/collection/v2.1.0/collection.json\"");
    }

    @Test
    public void generate_collectionName() {
        // GIVEN
        Postman postman = new Postman("collectionName", Collections.emptyList());

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"name\" : \"collectionName\"");
    }

    @Test
    public void generate_endpointName() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                "endpointName",
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

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
                "/path",
                Void.class,
                Collections.emptyList(),
                Void.class
        )
                .withBaseUrl("base");

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"url\" : \"base/path\"");
    }

    @Test
    public void generate_endpointMethod() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                Endpoint.Verb.GET,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

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
                Void.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"key\" : \"Content-Type\"")
                .contains("\"value\" : \"application/json\"");
    }

    @Test
    public void generate_endpointPayload() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                BodyPayload.class,
                Collections.emptyList(),
                Void.class
        );

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("\"body\" : {")
                .contains("\"mode\" : \"raw\"")
                .contains("\"raw\" : \"{\\n  \\\"username\\\": \\\"string\\\",\\n  \\\"password\\\": \\\"string\\\"\\n}\"");
    }

    @Test
    public void generate_endpointGetParameters() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                "path",
                Void.class,
                Arrays.asList("param1", "param2"),
                Void.class
        );

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

        // WHEN
        String result = postman.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("path?param1=...&param2=...");
    }

    @Test
    public void generate_endpointBasicAuth() {
        // GIVEN
        Endpoint endpoint = new Endpoint(
                null,
                null,
                null,
                null,
                Void.class,
                Collections.emptyList(),
                Void.class
        )
                .withAuthentication("username", "password");

        Postman postman = new Postman("collectionName", Collections.singletonList(endpoint));

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

    @Data
    public static class BodyPayload {
        private String username;
        private String password;
    }
}

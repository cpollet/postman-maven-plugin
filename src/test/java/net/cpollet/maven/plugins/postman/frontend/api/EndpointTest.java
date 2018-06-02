package net.cpollet.maven.plugins.postman.frontend.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;

public class EndpointTest {
    @Test
    public void withBaseUrl_returnsNewEndpointInstance() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, null);

        // WHEN
        Endpoint newEndpoint = endpoint.withBaseUrl("http://localhost");

        // THEN
        Assertions.assertThat(newEndpoint)
                .isNotNull()
                .isNotSameAs(endpoint);
    }

    @Test
    public void withBaseUrl_returnsNewEndpoint_withSameAttributes() {
        // GIVEN
        ArrayList<String> names = new ArrayList<>();
        Endpoint endpoint = new Endpoint(Endpoint.Verb.GET, "path", String.class, names, Long.class);

        // WHEN
        Endpoint newEndpoint = endpoint.withBaseUrl("http://localhost");

        // THEN
        Assertions.assertThat(newEndpoint.getVerb())
                .isEqualTo(Endpoint.Verb.GET);

        Assertions.assertThat(newEndpoint.getPath())
                .isEqualTo("path");

        Assertions.assertThat(newEndpoint.getBodyType())
                .isEqualTo(String.class);

        Assertions.assertThat(newEndpoint.getQueryParametersNames())
                .isEqualTo(names);

        Assertions.assertThat(newEndpoint.getResponseType())
                .isEqualTo(Long.class);
    }

    @Test
    public void withBaseUrl_returnsNewEndpoint_hasCorrectBase() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, "/index", null, null, null);

        // WHEN
        Endpoint newEndpoint = endpoint.withBaseUrl("http://localhost");

        // THEN
        Assertions.assertThat(newEndpoint.getUrl())
                .isEqualTo("http://localhost/index");
    }

    @Test
    public void withBaseUrl_removesTrailingSlashes() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, "/index", null, null, null);

        // WHEN
        Endpoint newEndpoint = endpoint.withBaseUrl("http://localhost///");

        // THEN
        Assertions.assertThat(newEndpoint.getUrl())
                .isEqualTo("http://localhost/index");
    }
}

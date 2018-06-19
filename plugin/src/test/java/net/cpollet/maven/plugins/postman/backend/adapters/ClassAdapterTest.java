package net.cpollet.maven.plugins.postman.backend.adapters;

import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

public class ClassAdapterTest {
    @Test
    public void getEndpoints_returnsEmptyList_whenNotARestClass() {
        // GIVEN
        ClassAdapter adapter = new ClassAdapter(NoRest.class);

        // WHEN
        List<Endpoint> endpoints = adapter.getEndpoints();

        // THEN
        Assertions.assertThat(endpoints)
                .isEmpty();
    }

    private class NoRest {
    }

    @Test
    public void getEndpoints_returnsEndpointsList_whenThereAreSome() {
        // GIVEN
        ClassAdapter adapter = new ClassAdapter(Rest.class);

        // WHEN
        List<Endpoint> endpoints = adapter.getEndpoints();

        // THEN
        Assertions.assertThat(endpoints)
                .isNotEmpty();
    }

    @Test
    public void getEndpoints_returnsFullyFilledEndpoint() {
        // GIVEN
        ClassAdapter adapter = new ClassAdapter(Rest.class);

        // WHEN
        Endpoint endpoint = adapter.getEndpoints().get(1);

        // THEN
        Assertions.assertThat(endpoint.getGroup())
                .isEqualTo("Rest");

        Assertions.assertThat(endpoint.getPath())
                .isEqualTo("/get(String,String)");

        Assertions.assertThat(endpoint.getVerb())
                .isEqualTo(Endpoint.Verb.GET);

        Assertions.assertThat(endpoint.getBodyType())
                .isEqualTo(String.class);

        Assertions.assertThat(endpoint.getQueryParametersNames())
                .containsExactly("pathParam");

        Assertions.assertThat(endpoint.getResponseType())
                .isEqualTo(Boolean.class);

        Assertions.assertThat(endpoint.getParameterTypes())
                .containsExactly(String.class, String.class);
    }

    @Test
    public void getEndpoints_sortedEndpoints() {
        // GIVEN
        ClassAdapter adapter = new ClassAdapter(Rest.class);

        // WHEN
        List<Endpoint> endpoints = adapter.getEndpoints();

        // THEN
        Assertions.assertThat(endpoints.get(0).getPath())
                .isEqualTo("/get(String)");
        Assertions.assertThat(endpoints.get(1).getPath())
                .isEqualTo("/get(String,String)");
    }

    @Path("/")
    private interface Rest {
        @GET
        @Path("/get(String,String)")
        Boolean get(@QueryParam("pathParam") String param, String body);

        @GET
        @Path("/get(String)")
        Boolean get(@QueryParam("pathParam") String param);
    }
}

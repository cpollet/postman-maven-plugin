package net.cpollet.maven.plugins.postman.backend.adapters;

import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClassesAdapterTest {
    @Test
    public void getEndpoints_returnsEndpointsOfAllClasses() {
        // GIVEN
        ClassesAdapter adapter = new ClassesAdapter(Arrays.asList(Rest1.class, Rest2.class));

        // WHEN
        List<Endpoint> endpoints = adapter.getEndpoints();
        List<String> endpointPaths = endpoints.stream()
                .map(Endpoint::getPath)
                .collect(Collectors.toList());

        // THEN
        Assertions.assertThat(endpoints)
                .hasSize(2);

        Assertions.assertThat(endpointPaths)
                .containsExactlyInAnyOrder("/index1", "/index2");
    }

    @Path("/")
    private class Rest1 {
        @GET
        @Path("/index1")
        public Boolean get(@QueryParam("pathParam") String param, String body) {
            return null;
        }
    }

    @Path("/")
    private class Rest2 {
        @GET
        @Path("/index2")
        public Boolean get(@QueryParam("pathParam") String param, String body) {
            return null;
        }
    }
}

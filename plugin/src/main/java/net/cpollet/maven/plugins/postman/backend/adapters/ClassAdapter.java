package net.cpollet.maven.plugins.postman.backend.adapters;

import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;

import javax.ws.rs.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ClassAdapter {
    private final Class<?> clazz;

    public List<Endpoint> getEndpoints() {
        return Collections.unmodifiableList(
                Arrays.stream(clazz.getMethods())
                        .map(MethodAdapter::new)
                        .filter(MethodAdapter::isHttpEndpoint)
                        .map(m -> new Endpoint(
                                clazz.getSimpleName(),
                                m.getName(),
                                m.getParameterTypes(),
                                m.getVerb(),
                                getPath() + m.getPath(),
                                m.getBodyParameterType(),
                                m.getQueryParametersNames(),
                                m.getResponseType()
                        ))
                        .sorted(Endpoint::compareTo)
                        .collect(Collectors.toList())
        );
    }

    private String getPath() {
        return new PathAdapter(clazz.getAnnotation(Path.class)).getPath();
    }
}

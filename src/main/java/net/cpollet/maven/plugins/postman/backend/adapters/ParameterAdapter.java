package net.cpollet.maven.plugins.postman.backend.adapters;

import lombok.AllArgsConstructor;

import javax.ws.rs.QueryParam;
import java.lang.reflect.Parameter;
import java.util.Optional;

@AllArgsConstructor
public class ParameterAdapter {
    private final Parameter parameter;

    public Optional<String> getQueryParamName() {
        if (isHttpQueryParameter()) {
            return Optional.of(parameter.getAnnotation(QueryParam.class).value());
        }

        return Optional.empty();
    }

    public boolean isHttpQueryParameter() {
        return parameter.isAnnotationPresent(QueryParam.class);
    }

    public boolean isHttpBodyParameter() {
        return !isHttpQueryParameter();
    }

    public Class getType() {
        return parameter.getType();
    }
}

package net.cpollet.maven.plugins.postman.backend.adapters;

import lombok.AllArgsConstructor;

import javax.ws.rs.QueryParam;
import java.lang.reflect.Parameter;

@AllArgsConstructor
public class ParameterAdapter {
    private final Parameter parameter;

    public String getQueryParamName() {
        if (isHttpQueryParameter()) {
            return parameter.getAnnotation(QueryParam.class).value();
        }

        return null; // FIXME return optional
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

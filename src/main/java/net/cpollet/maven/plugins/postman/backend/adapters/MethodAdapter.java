package net.cpollet.maven.plugins.postman.backend.adapters;

import lombok.AllArgsConstructor;
import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MethodAdapter {
    private final static Map<Class<? extends Annotation>, Endpoint.Verb> verbs = new HashMap<Class<? extends Annotation>, Endpoint.Verb>() {{
        put(GET.class, Endpoint.Verb.GET);
        put(POST.class, Endpoint.Verb.POST);
        put(PUT.class, Endpoint.Verb.PUT);
        put(DELETE.class, Endpoint.Verb.DELETE);
        put(HEAD.class, Endpoint.Verb.HEAD);
        put(OPTIONS.class, Endpoint.Verb.OPTIONS);
        put(PATCH.class, Endpoint.Verb.PATCH);
    }};

    private final Method method;

    public String getPath() {
        return new PathAdapter(method.getAnnotation(Path.class)).getPath();
    }

    public Endpoint.Verb getVerb() {
        Class<? extends Annotation> annotation = verbs.keySet().stream()
                .filter(method::isAnnotationPresent)
                .findFirst()
                .orElse(GET.class);

        return verbs.get(annotation);
    }

    public boolean isHttpEndpoint() {
        return verbs.keySet().stream()
                .anyMatch(method::isAnnotationPresent);
    }

    public List<String> getQueryParametersNames() {
        return Arrays.stream(method.getParameters())
                .map(ParameterAdapter::new)
                .filter(ParameterAdapter::isHttpQueryParameter)
                .map(ParameterAdapter::getQueryParamName)
                .collect(Collectors.toList());
    }

    public Class getBodyParameterType() {
        return Arrays.stream(method.getParameters())
                .map(ParameterAdapter::new)
                .filter(ParameterAdapter::isHttpBodyParameter)
                .findFirst()
                .map(ParameterAdapter::getType)
                .orElse(null);//FIXME return optional
    }

    public boolean hasBodyParameter() {
        return Arrays.stream(method.getParameters())
                .map(ParameterAdapter::new)
                .anyMatch(ParameterAdapter::isHttpBodyParameter);
    }

    public Class getResponseType() {
        if (Void.TYPE.equals(method.getReturnType())) {
            return Void.class;
        }

        return method.getReturnType();
    }
}

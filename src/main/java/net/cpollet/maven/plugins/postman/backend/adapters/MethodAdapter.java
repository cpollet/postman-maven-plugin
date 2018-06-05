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
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class MethodAdapter {
    private static final Map<Class<? extends Annotation>, Endpoint.Verb> verbs = Collections.unmodifiableMap(Stream.of(
            new AbstractMap.SimpleEntry<>(GET.class, Endpoint.Verb.GET),
            new AbstractMap.SimpleEntry<>(POST.class, Endpoint.Verb.POST),
            new AbstractMap.SimpleEntry<>(PUT.class, Endpoint.Verb.PUT),
            new AbstractMap.SimpleEntry<>(DELETE.class, Endpoint.Verb.DELETE),
            new AbstractMap.SimpleEntry<>(HEAD.class, Endpoint.Verb.HEAD),
            new AbstractMap.SimpleEntry<>(OPTIONS.class, Endpoint.Verb.OPTIONS),
            new AbstractMap.SimpleEntry<>(PATCH.class, Endpoint.Verb.PATCH)
    ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

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
                .map(Optional::get) // we are sure it's present, because of above filter calling isHttpQueryParameter
                .collect(Collectors.toList());
    }

    public Class getBodyParameterType() {
        return Arrays.stream(method.getParameters())
                .map(ParameterAdapter::new)
                .filter(ParameterAdapter::isHttpBodyParameter)
                .findFirst()
                .map(ParameterAdapter::getType)
                .orElse(Void.class);
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

    public String getName() {
        return method.getName();
    }
}

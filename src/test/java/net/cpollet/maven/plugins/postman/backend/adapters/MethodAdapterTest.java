package net.cpollet.maven.plugins.postman.backend.adapters;

import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.lang.reflect.Method;
import java.util.List;

public class MethodAdapterTest {
    @Test
    public void isHttpEndpoint_returnsTrue_whenGETAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("getEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    private MethodAdapter adapt(String methodName) throws NoSuchMethodException {
        Method method = getClass().getMethod(methodName);
        return new MethodAdapter(method);
    }

    private MethodAdapter adapt(String methodName, Class<?>... types) throws NoSuchMethodException {
        Method method = getClass().getMethod(methodName, types);
        return new MethodAdapter(method);
    }

    @Test
    public void getHttpMethod_returnsGET_whenGETAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("getEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.GET);
    }

    @Test
    public void getHttpMethod_returnsGET_whenNoMethodAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("noAnnotation");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.GET);
    }

    @GET
    public String getEndpoint() {
        return null;
    }

    @Test
    public void isHttpEndpoint_returnsTrue_whenPOSTAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("postEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    @Test
    public void getHttpMethod_returnsPOST_whenPOSTAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("postEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.POST);
    }

    @POST
    public void postEndpoint() {
    }

    @Test
    public void isHttpEndpoint_returnsTrue_whenPUTAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("putEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    @Test
    public void getHttpMethod_returnsPUT_whenPUTAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("putEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.PUT);
    }

    @PUT
    public void putEndpoint() {
    }

    @Test
    public void isHttpEndpoint_returnsTrue_whenDELETEAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("deleteEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    @Test
    public void getHttpMethod_returnsGET_whenDELETEAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("deleteEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.DELETE);
    }

    @DELETE
    public void deleteEndpoint() {
    }

    @Test
    public void isHttpEndpoint_returnsTrue_whenHEADAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("headEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    @Test
    public void getHttpMethod_returnsGET_whenHEADAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("headEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.HEAD);
    }

    @HEAD
    public void headEndpoint() {
    }

    @Test
    public void isHttpEndpoint_returnsTrue_whenOPTIONSAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("optionsEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    @Test
    public void getHttpMethod_returnsGET_whenOPTIONSAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("optionsEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.OPTIONS);
    }

    @OPTIONS
    public void optionsEndpoint() {
    }

    @Test
    public void isHttpEndpoint_returnsTrue_whenPATCHAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("patchEndpoint");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isTrue();
    }

    @Test
    public void getHttpMethod_returnsGET_whenPATCHAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("patchEndpoint");

        // WHEN
        Endpoint.Verb verb = adapter.getVerb();

        // THEN
        Assertions.assertThat(verb)
                .isEqualTo(Endpoint.Verb.PATCH);
    }

    @PATCH
    public void patchEndpoint() {
    }

    @Test
    public void isHttpEndpoint_returnsFalse_whenNoHttpAnnotationPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("noAnnotation");

        // WHEN
        boolean isHttpEndpoint = adapter.isHttpEndpoint();

        // THEN
        Assertions.assertThat(isHttpEndpoint)
                .isFalse();
    }

    @SuppressWarnings({"WeakerAccess", "unused"}) //because of reflection
    public void noAnnotation() {
    }

    @Test
    public void getPath_returnsSanitizedPath() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("pathAnnotated");

        // WHEN
        String path = adapter.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/index");
    }

    @Path("index//")
    public void pathAnnotated() {
    }

    @Test
    public void getQueryParametersNames_isEmpty_whenNoQueryParams() throws NoSuchMethodException {
        //GIVEN
        MethodAdapter adapter = adapt("pathAnnotated");

        // WHEN
        List<String> params = adapter.getQueryParametersNames();

        // THEN
        Assertions.assertThat(params)
                .isEmpty();
    }

    @Test
    public void getQueryParametersNames_containQueryParamName() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("queryParam", String.class);

        // WHEN
        List<String> params = adapter.getQueryParametersNames();

        // THEN
        Assertions.assertThat(params)
                .containsExactly("paramName");
    }

    @SuppressWarnings({"unused"}) //because of reflection
    public void queryParam(@QueryParam("paramName") String param) {
    }

    @Test
    public void getQueryParametersNames_doesNotIncludeBodyParameter() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("bodyParam", String.class);

        // WHEN
        List<String> params = adapter.getQueryParametersNames();

        // THEN
        Assertions.assertThat(params)
                .isEmpty();
    }

    @Test
    public void hasBodyParameter_returnsFalse_whenBodyParameterNotPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("queryParam", String.class);

        // WHEN
        boolean hasBodyParameter = adapter.hasBodyParameter();

        // THEN
        Assertions.assertThat(hasBodyParameter)
                .isFalse();
    }

    @Test
    public void getBodyParameterType_returnsNull_whenBodyParametersNotPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("queryParam", String.class);

        // WHEN
        Class type = adapter.getBodyParameterType();

        // THEN
        Assertions.assertThat(type)
                .isEqualTo(Void.class);
    }

    @Test
    public void hasBodyParameter_returnsTrue_whenBodyParameterPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("bodyParam", String.class);

        // WHEN
        boolean hasBodyParameter = adapter.hasBodyParameter();

        // THEN
        Assertions.assertThat(hasBodyParameter)
                .isTrue();
    }

    @Test
    public void getBodyParameterType_returnsType_whenBodyParameterPresent() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("bodyParam", String.class);

        // WHEN
        Class type = adapter.getBodyParameterType();

        // THEN
        Assertions.assertThat(type)
                .isEqualTo(String.class);
    }

    @SuppressWarnings({"unused"}) //because of reflection
    public void bodyParam(String param) {
    }

    @Test
    public void getResponseType_returnsVoid_whenVoid() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("bodyParam", String.class);

        // WHEN
        Class type = adapter.getResponseType();

        // THEN
        Assertions.assertThat(type)
                .isEqualTo(Void.class);
    }

    @Test
    public void getResponseType_returnsType() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("nonVoid");

        // WHEN
        Class type = adapter.getResponseType();

        // THEN
        Assertions.assertThat(type)
                .isEqualTo(String.class);
    }

    @Test
    public void getName_returnsMethodName() throws NoSuchMethodException {
        // GIVEN
        MethodAdapter adapter = adapt("nonVoid");

        // WHEN
        String name = adapter.getName();

        // THEN
        Assertions.assertThat(name)
                .isEqualTo("nonVoid");
    }

    @SuppressWarnings({"unused"}) //because of reflection
    public String nonVoid() {
        return null;
    }
}

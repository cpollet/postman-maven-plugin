package net.cpollet.maven.plugins.postman.backend.adapters;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import javax.ws.rs.QueryParam;
import java.lang.reflect.Method;
import java.util.Optional;

public class ParameterAdapterTest {
    private final static int NO_QUERY_PARAM = 0;
    private static final int QUERY_PARAM = 1;

    @Test
    public void getQueryParamName_returnsEmptyOptional_whenNoQueryParam() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(NO_QUERY_PARAM);

        // WHEN
        Optional<String> queryParamName = parameterAdapter.getQueryParamName();

        // THEN
        Assertions.assertThat(queryParamName)
                .isEmpty();
    }

    @Test
    public void isHttpQueryParameter_returnsFalse_whenNoQueryParam() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(NO_QUERY_PARAM);

        // WHEN
        boolean httpQueryParameter = parameterAdapter.isHttpQueryParameter();

        Assertions.assertThat(httpQueryParameter)
                .isFalse();
    }

    @Test
    public void getQueryParamName_returnsPramName_whenQueryParam() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(QUERY_PARAM);

        // WHEN
        Optional<String> queryParamName = parameterAdapter.getQueryParamName();

        // THEN
        Assertions.assertThat(queryParamName)
                .contains("name");
    }

    @Test
    public void isHttpQueryParameter_returnsTrue_whenQueryParam() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(QUERY_PARAM);

        // WHEN
        boolean httpQueryParameter = parameterAdapter.isHttpQueryParameter();

        Assertions.assertThat(httpQueryParameter)
                .isTrue();
    }

    @Test
    public void isBodyParameter_returnsTrue_whenNoQueryParam() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(NO_QUERY_PARAM);

        // WHEN
        boolean httpQueryParameter = parameterAdapter.isHttpBodyParameter();

        Assertions.assertThat(httpQueryParameter)
                .isTrue();
    }

    @Test
    public void isBodyParameter_returnsFalse_whenQueryParam() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(QUERY_PARAM);

        // WHEN
        boolean httpQueryParameter = parameterAdapter.isHttpBodyParameter();

        Assertions.assertThat(httpQueryParameter)
                .isFalse();
    }

    @Test
    public void getType_returnsType() throws NoSuchMethodException {
        // GIVEN
        ParameterAdapter parameterAdapter = adapt(QUERY_PARAM);

        // WHEN
        Class type = parameterAdapter.getType();

        Assertions.assertThat(type)
                .isEqualTo(String.class);
    }

    @SuppressWarnings({"WeakerAccess", "unused"}) // because of reflection
    public void queryParameters(String noQueryParam, @QueryParam("name") String queryParam) {
    }

    private ParameterAdapter adapt(int position) throws NoSuchMethodException {
        Method method = getClass().getMethod("queryParameters", String.class, String.class);

        if (method.getParameters().length < position - 1) {
            throw new IllegalStateException("Not enough queryParameters");
        }

        return new ParameterAdapter(
                method.getParameters()[position]
        );
    }
}

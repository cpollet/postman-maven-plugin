package net.cpollet.maven.plugins.postman.frontend.curl;

import net.cpollet.maven.plugins.postman.frontend.api.Endpoint;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class CurlTest {
    @Mock
    private Endpoint endpoint;

    private Curl curl;

    @Before
    public void setup() {
        curl = new Curl(endpoint);
    }

    @Test
    public void generate_generatesCurlCommand() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .startsWith("curl ");
    }

    @Test
    public void generate_generatesDataFlag_forQueryParameters() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getQueryParametersNames()).thenReturn(Arrays.asList("p1", "p2"));

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-d p1=... -d p2=... ");
    }

    @Test
    public void generate_generatesGet() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.GET);
        Mockito.when(endpoint.getUrl()).thenReturn("url");

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("--get ");
    }

    @Test
    public void generate_generatesPost() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.POST);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-X POST");
    }

    @Test
    public void generate_generatesPut() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.PUT);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-X PUT ");
    }

    @Test
    public void generate_generatesDelete() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.DELETE);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-X DELETE ");
    }

    @Test
    public void generate_generatesHead() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.HEAD);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("--head ");
    }

    @Test
    public void generate_generatesOptions() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.OPTIONS);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-X OPTIONS ");
    }

    @Test
    public void generate_generatesPatch() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getVerb()).thenReturn(Endpoint.Verb.PATCH);

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-X PATCH ");
    }

    @Test
    public void generate_generatesNoBodyParameter() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);

        // WHEN
        String result = curl.generate();

        Assertions.assertThat(result)
                .doesNotContain("-d");
    }

    @Test
    public void generate_generatesBodyParameter() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(String.class);

        // WHEN
        String result = curl.generate();

        Assertions.assertThat(result)
                .contains("-d '\"string\"' ");
    }

    @Test
    public void generate_generatesUsernamePassword() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getUsername()).thenReturn("username");
        Mockito.when(endpoint.getPassword()).thenReturn("password");

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .contains("-u username:password ");
    }

    @Test
    public void generate_doesNotGeneratesUsernamePassword_whenUsernameNotSet() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getPassword()).thenReturn("password");

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .doesNotContain("-u ");
    }

    @Test
    public void generate_doesNotGeneratesUsernamePassword_whenPasswordNotSet() {
        // GIVEN
        Mockito.when(endpoint.getBodyType()).thenReturn(Void.class);
        Mockito.when(endpoint.getPassword()).thenReturn("username");

        // WHEN
        String result = curl.generate();

        // THEN
        Assertions.assertThat(result)
                .doesNotContain("-u ");
    }
}

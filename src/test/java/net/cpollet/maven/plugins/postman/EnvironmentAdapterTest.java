package net.cpollet.maven.plugins.postman;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class EnvironmentAdapterTest {
    @Test
    public void getBaseUrl_returnsBaseUrl() throws MalformedURLException {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, new URL("http://localhost"), null)
        );

        // WHEN
        String baseUrl = adapter.getBaseUrl();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("http://localhost");
    }

    @Test
    public void getBaseUrl_returnsLocalhost_whenNotSet() throws MalformedURLException {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, null, null)
        );

        // WHEN
        String baseUrl = adapter.getBaseUrl();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("http://localhost");
    }

    @Test
    public void getName_returnsName() {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment("name", null, null)
        );

        // WHEN
        String baseUrl = adapter.getName();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("name");
    }

    @Test
    public void getName_returnsDefault_whenNotSet() {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, null, null)
        );

        // WHEN
        String baseUrl = adapter.getName();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("default");
    }

    @Test
    public void getName_returnsDefault_whenEmpty() {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment("  ", null, null)
        );

        // WHEN
        String baseUrl = adapter.getName();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("default");
    }

    @Test
    public void getBasicAuthUsername_returnsUsername_whenSet() {
        // GIVEN
        BasicAuth basicAuth = new BasicAuth("username",null);
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, null, basicAuth)
        );

        // WHEN
        String baseUrl = adapter.getUsername();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("username");
    }

    @Test
    public void getBasicAuthUsername_returnsNull_whenNotSet() {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, null, null)
        );

        // WHEN
        String baseUrl = adapter.getUsername();

        // THEN
        Assertions.assertThat(baseUrl)
                .isNull();
    }

    @Test
    public void getBasicAuthPassword_returnsPassword_whenSet() {
        // GIVEN
        BasicAuth basicAuth = new BasicAuth(null, "password");
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, null, basicAuth)
        );

        // WHEN
        String baseUrl = adapter.getPassword();

        // THEN
        Assertions.assertThat(baseUrl)
                .isEqualTo("password");
    }

    @Test
    public void getBasicAuthPassword_returnsNull_whenNotSet() {
        // GIVEN
        EnvironmentAdapter adapter = new EnvironmentAdapter(
                new Environment(null, null, null)
        );

        // WHEN
        String baseUrl = adapter.getPassword();

        // THEN
        Assertions.assertThat(baseUrl)
                .isNull();
    }
}

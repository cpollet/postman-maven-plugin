package net.cpollet.maven.plugins.postman;

import net.cpollet.maven.plugins.postman.frontend.api.Context;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class EnvironmentsAdapterTest {
    @Test
    public void contexts_returnsAsManyContextsAsEnvironments() throws DuplicateEnvironmentNameException {
        // GIVEN
        EnvironmentsAdapter environmentsAdapter = new EnvironmentsAdapter(
                Arrays.asList(
                        new Environment("name1", null, null),
                        new Environment("name2", null, null)
                )
        );

        // WHEN
        Map<String, Context> contexts = environmentsAdapter.contexts();

        // THEN
        Assertions.assertThat(contexts)
                .hasSize(2);
    }

    @Test
    public void contexts_throwsDuplicateEnvironmentName_whenNamesAreNotUnique() {
        // GIVEN
        EnvironmentsAdapter environmentsAdapter = new EnvironmentsAdapter(
                Arrays.asList(
                        new Environment("name", null, null),
                        new Environment("name2", null, null),
                        new Environment("name1", null, null),
                        new Environment("name1", null, null),
                        new Environment("name3", null, null),
                        new Environment("name2", null, null)
                )
        );

        try {
            // WHEN
            environmentsAdapter.contexts();
        } catch (DuplicateEnvironmentNameException e) {
            // THEN
            Assertions.assertThat(e.getMessage())
                    .isEqualTo("Duplicate environment name found: name1, name2");

            return;
        }

        Assertions.fail("Expected exception");
    }

    @Test
    public void contexts_mapsEnvironmentNameToContextName() throws DuplicateEnvironmentNameException {
        // GIVEN
        EnvironmentsAdapter environmentsAdapter = new EnvironmentsAdapter(
                Collections.singletonList(
                        new Environment("name", null, null)
                )
        );

        // WHEN
        Map<String, Context> contexts = environmentsAdapter.contexts();

        // THEN
        Assertions.assertThat(contexts.keySet())
                .containsExactly("name");
    }

    @Test
    public void contexts_mapsEnvironmentToContext() throws MalformedURLException, DuplicateEnvironmentNameException {
        // GIVEN
        BasicAuth basicAuth = new BasicAuth("username", "password");
        EnvironmentsAdapter environmentsAdapter = new EnvironmentsAdapter(
                Collections.singletonList(
                        new Environment("name", new URL("http://localhost"), basicAuth)
                )
        );

        // WHEN
        Context context = environmentsAdapter.contexts().get("name");

        // THEN
        Assertions.assertThat(context.getBaseUrl())
                .isEqualTo("http://localhost");

        Assertions.assertThat(context.getUsername())
                .isEqualTo("username");

        Assertions.assertThat(context.getPassword())
                .isEqualTo("password");
    }
}

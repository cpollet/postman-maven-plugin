package net.cpollet.maven.plugins.postman.frontend.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class EndpointTest {
    @Test
    public void getGroup_returnsDefault_whenNoSpecificGroupSet() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, null, null, null, null);

        // WHEN
        String group = endpoint.getGroup();

        // THEN
        Assertions.assertThat(group)
                .isEqualTo("__default");
    }

    @Test
    public void getGroup_returnsGroup_whenGroupSet() {
        // GIVEN
        Endpoint endpoint = new Endpoint("Group", null, null, null, null, null, null, null);

        // WHEN
        String group = endpoint.getGroup();

        // THEN
        Assertions.assertThat(group)
                .isEqualTo("Group");
    }

    @Test
    public void getPath_addsInitialSlashIfMissing() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, "path", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void getPath_doesNotAddInitialSlashIfNotMissing() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, "/path", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void getPath_removesUselessInitialSlashes() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, "//path", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void getPath_removeEndSlashes() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, "/path//", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void compareTo_comparesByGroup_1() {
        // GIVEN
        Endpoint first = new Endpoint("a", "a", Collections.emptyList(), null, null, null, null, null);
        Endpoint second = new Endpoint("b", "a", Collections.emptyList(), null, null, null, null, null);

        // WHEN
        int result = first.compareTo(second);

        // THEN
        Assertions.assertThat(result)
                .isLessThan(0);
    }

    @Test
    public void compareTo_comparesByGroup_2() {
        // GIVEN
        Endpoint first = new Endpoint("a", "a", Collections.emptyList(), null, null, null, null, null);
        Endpoint second = new Endpoint("b", "a", Collections.emptyList(), null, null, null, null, null);

        // WHEN
        int result = second.compareTo(first);

        // THEN
        Assertions.assertThat(result)
                .isGreaterThan(0);
    }

    @Test
    public void compareTo_comparesByName_1() {
        // GIVEN
        Endpoint first = new Endpoint(null, "a", Collections.emptyList(), null, null, null, null, null);
        Endpoint second = new Endpoint(null, "b", Collections.emptyList(), null, null, null, null, null);

        // WHEN
        int result = first.compareTo(second);

        // THEN
        Assertions.assertThat(result)
                .isLessThan(0);
    }

    @Test
    public void compareTo_comparesByName_2() {
        // GIVEN
        Endpoint first = new Endpoint(null, "a", Collections.emptyList(), null, null, null, null, null);
        Endpoint second = new Endpoint(null, "b", Collections.emptyList(), null, null, null, null, null);

        // WHEN
        int result = second.compareTo(first);

        // THEN
        Assertions.assertThat(result)
                .isGreaterThan(0);
    }

    @Test
    public void compareTo_comparesByNameThenParameterTypes_1() {
        // GIVEN
        List<Class> firstTypes = Collections.emptyList();
        List<Class> secondTypes = Collections.singletonList(String.class);
        Endpoint first = new Endpoint(null, "a", firstTypes, null, null, null, null, null);
        Endpoint second = new Endpoint(null, "a", secondTypes, null, null, null, null, null);

        // WHEN
        int result = first.compareTo(second);

        // THEN
        Assertions.assertThat(result)
                .isLessThan(0);
    }

    @Test
    public void compareTo_comparesByNameThenParameterTypes_2() {
        // GIVEN
        List<Class> firstTypes = Collections.emptyList();
        List<Class> secondTypes = Collections.singletonList(String.class);
        Endpoint first = new Endpoint(null, "a", firstTypes, null, null, null, null, null);
        Endpoint second = new Endpoint(null, "a", secondTypes, null, null, null, null, null);

        // WHEN
        int result = second.compareTo(first);

        // THEN
        Assertions.assertThat(result)
                .isGreaterThan(0);
    }
}

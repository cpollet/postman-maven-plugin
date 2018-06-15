package net.cpollet.maven.plugins.postman.frontend.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class EndpointTest {
    @Test
    public void getGroup_returnsDefault_whenNoSpecificGroupSet() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null, null, null, null, null, null);

        // WHEN
        String group = endpoint.getGroup();

        // THEN
        Assertions.assertThat(group)
                .isEqualTo("__default");
    }

    @Test
    public void getGroup_returnsGroup_whenGroupSet() {
        // GIVEN
        Endpoint endpoint = new Endpoint("Group", null, null, null, null, null, null);

        // WHEN
        String group = endpoint.getGroup();

        // THEN
        Assertions.assertThat(group)
                .isEqualTo("Group");
    }

    @Test
    public void getPath_addsInitialSlashIfMissing() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null,null, "path", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void getPath_doesNotAddInitialSlashIfNotMissing() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null,null, "/path", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void getPath_removesUselessInitialSlashes() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null,null, "//path", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }

    @Test
    public void getPath_removeEndSlashes() {
        // GIVEN
        Endpoint endpoint = new Endpoint(null, null,null, "/path//", null, null, null);

        // WHEN
        String path = endpoint.getPath();

        // THEN
        Assertions.assertThat(path)
                .isEqualTo("/path");
    }
}

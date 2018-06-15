package net.cpollet.maven.plugins.postman.frontend.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ContextTest {
    @Test
    public void getBaseUrl_doesNotHaveTrailingSlashes() {
        // GIVEN
        Context context = new Context("url//", null, null);

        // WHEN
        String url = context.getBaseUrl();

        // THEN
        Assertions.assertThat(url)
                .isEqualTo("url");
    }
}

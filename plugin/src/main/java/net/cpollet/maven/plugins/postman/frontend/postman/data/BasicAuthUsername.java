package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicAuthUsername implements BasicAuthField {
    @SuppressWarnings("UnusedAssignment") // used by the Builder.Default annotation
    @Builder.Default
    private final String key = "username";

    @SuppressWarnings("UnusedAssignment") // used by the Builder.Default annotation
    @Builder.Default
    private final String type = "string";

    private String value;
}

package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Body {
    @SuppressWarnings("UnusedAssignment") // used by the Builder.Default annotation
    @Builder.Default
    private String mode = "raw";

    private String raw;
}

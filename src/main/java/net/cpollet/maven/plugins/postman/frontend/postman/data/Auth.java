package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Auth {
    @SuppressWarnings("UnusedAssignment") // used by the Builder.Default annotation
    @Builder.Default
    private String type = "basic";

    @Singular("basic")
    private List<BasicAuthField> basic;
}

package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Information {
    private final String schema = "https://schema.getpostman.com/json/collection/v2.1.0/collection.json";

    @SuppressWarnings("UnusedAssignment") // used by the Builder.Default annotation
    @Builder.Default
    private String _postman_id = UUID.randomUUID().toString();

    private String name;
}

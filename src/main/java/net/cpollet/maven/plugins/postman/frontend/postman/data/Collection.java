package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

/**
 * http://schema.getpostman.com/json/collection/v2.1.0/docs/index.html
 */
@Data
@Builder
public class Collection {
    private Information info;
    @Singular
    private List<ItemOrFolder> items;
}


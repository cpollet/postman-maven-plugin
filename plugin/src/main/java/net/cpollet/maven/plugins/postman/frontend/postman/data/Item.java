package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item implements ItemOrFolder {
    private String name;
    private Request request;
}

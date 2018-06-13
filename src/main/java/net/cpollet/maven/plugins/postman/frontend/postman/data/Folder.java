package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Folder implements ItemOrFolder {
    private String name;
    @Singular("item")
    private List<Item> item;
}

package net.cpollet.examples.jaxrs;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String name;
    private String job;
    private List<String> things;
}

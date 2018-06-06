package net.cpollet.maven.plugins.postman.it.api;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private String username;
    private List<String> roles;
}

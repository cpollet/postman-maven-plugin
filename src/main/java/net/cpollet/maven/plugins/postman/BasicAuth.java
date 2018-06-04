package net.cpollet.maven.plugins.postman;

import lombok.Data;

@Data
public class BasicAuth {
    private String username;
    private String password;
}

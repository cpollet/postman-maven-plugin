package net.cpollet.maven.plugins.postman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BasicAuth {
    private String username;
    private String password;
}

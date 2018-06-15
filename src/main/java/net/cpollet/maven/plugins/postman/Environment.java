package net.cpollet.maven.plugins.postman;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Environment {
    private String name;

    /**
     * The base URL to use when generating the postman collections. This has to be a valid URL.
     */
    private URL baseUrl;

    /**
     * The basic auth username and password to use, if any. It contains two fields, <code>username</code> and
     * <code>password</code> (see {@link BasicAuth}).
     */
    private BasicAuth basicAuth;
}

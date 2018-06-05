package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String url;
    private Auth auth;
    private Method method;
    private Object body;

    public enum Method {
        GET, PUT, POST, PATCH, DELETE, HEAD, OPTIONS;
    }
}

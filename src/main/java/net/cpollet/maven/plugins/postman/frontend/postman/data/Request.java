package net.cpollet.maven.plugins.postman.frontend.postman.data;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Request {
    private String url;
    private Auth auth;
    private Method method;
    private Body body;

    @Singular("item")
    private List<Header> header;

    public enum Method {
        GET, PUT, POST, PATCH, DELETE, HEAD, OPTIONS;
    }
}

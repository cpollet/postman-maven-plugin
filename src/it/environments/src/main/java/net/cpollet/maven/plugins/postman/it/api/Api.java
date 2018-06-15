package net.cpollet.maven.plugins.postman.it.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/api/public")
public class Api {
    @GET
    @Path("users")
    public List<User> getUsers() {
        return null;
    }
}

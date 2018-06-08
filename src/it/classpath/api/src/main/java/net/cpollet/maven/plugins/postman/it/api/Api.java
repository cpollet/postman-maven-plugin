package net.cpollet.maven.plugins.postman.it.api;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api/public")
public class Api {
    @POST
    @Path("users")
    public User createUser(User user) {
        return null;
    }
}

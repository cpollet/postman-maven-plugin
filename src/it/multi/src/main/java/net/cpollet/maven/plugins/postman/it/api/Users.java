package net.cpollet.maven.plugins.postman.it.api;

import net.cpollet.maven.plugins.postman.it.api.entity.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/api/public")
public class Users {
    @GET
    @Path("users")
    public List<User> getUsers() {
        return null;
    }

    @GET
    @Path("users/userId") // should be a {userId} with @PathParam, but it causes issues with wiremock
    public User getUser() {
        return null;
    }
}

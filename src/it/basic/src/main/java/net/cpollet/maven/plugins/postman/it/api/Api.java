package net.cpollet.maven.plugins.postman.it.api;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/api/public")
public class Api {
    @GET
    @Path("users")
    public void getUsers() {
    }
}

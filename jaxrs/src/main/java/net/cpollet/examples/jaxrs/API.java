package net.cpollet.examples.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * JAX-RS API for https://reqres.in/
 */
@Path("/api")
public class API {
    @GET
    @Path("users")
    public void getUsers(@QueryParam("page") Integer page, @QueryParam("delay") Integer delay) {
    }

    @GET
    @Path("users/{id}")
    public void getUser(@PathParam("id") Integer page) {
    }

    @POST
    @Path("users")
    public void createUser(User payload) {
    }
}

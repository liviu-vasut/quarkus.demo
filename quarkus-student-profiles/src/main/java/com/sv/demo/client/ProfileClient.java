package com.sv.demo.client;

import com.sv.demo.dto.StudentProfile;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/users")
@RegisterRestClient
public interface ProfileClient {

    @GET
    @Path("/{name}")
    @Produces("application/json")
    StudentProfile getByName(@PathParam("name") String name);

}

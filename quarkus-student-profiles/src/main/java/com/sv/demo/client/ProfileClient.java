package com.sv.demo.client;

import com.sv.demo.dto.StudentProfile;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/users")
@RegisterRestClient
public interface ProfileClient {

    @GET
    @Path("/{name}")
    @Produces("application/json")
    @Fallback(fallbackMethod = "getDummyProfile")
    StudentProfile getByName(@PathParam("name") String name);

    private static StudentProfile getDummyProfile(String name) {
        StudentProfile profile = new StudentProfile();
        profile.setName(name);
        profile.setAvatarUrl("https://thesocietypages.org/socimages/files/2009/05/nopic_192.gif");
        return profile;
    }
}

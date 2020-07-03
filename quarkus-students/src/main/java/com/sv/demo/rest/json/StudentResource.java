package com.sv.demo.rest.json;

import com.sv.demo.dto.Student;
import com.sv.demo.service.StudentService;
import java.util.Set;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentResource {

    @Inject
    StudentService studentService;

    @GET
    @Path("/list")
    public Set<Student> list() {
        return studentService.findAll();
    }

    @GET
    @Path("/{id}")
    public Student getById(@PathParam("id") long id) {
        return studentService.findById(id);
    }

    @POST
    public Student add(Student student) {
        return studentService.add(student);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        studentService.delete(id);
    }

}

package com.sv.demo.rest.json;

import com.sv.demo.dto.Student;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/student")
public class StudentResource {

    private final Set<Student> students = Collections.synchronizedSet(new HashSet<>());

    @GET
    @Path("/list")
    public Set<Student> list() {
        return students;
    }

    @GET
    @Path("/{id}")
    public Student getById(@PathParam("id") long id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @POST
    public Student add(Student student) {
        students.add(student);
        return student;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") long id) {
        students.removeIf(student -> student.getId() == id);
    }

}

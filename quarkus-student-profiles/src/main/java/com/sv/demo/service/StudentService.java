package com.sv.demo.service;

import com.sv.demo.client.ProfileClient;
import com.sv.demo.dto.StudentProfile;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class StudentService {

    private final Set<StudentProfile> studentProfiles = new HashSet<>();

    @Inject
    @RestClient
    private ProfileClient profileClient;

    @Incoming("student")
    public void receive(String name) {
        StudentProfile profile = profileClient.getByName(name);
        studentProfiles.add(profile);
    }

    public StudentProfile getProfileByName(String name) {
        return studentProfiles.stream()
                .filter(student -> student.getName().equals(name))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}

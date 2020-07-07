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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    private final Set<StudentProfile> studentProfiles = new HashSet<>();

    @Inject
    @RestClient
    private ProfileClient profileClient;

    @Incoming("student")
    public void receive(String name) {
        StudentProfile profile = profileClient.getByName(name);
        studentProfiles.add(profile);
        LOG.info("Created profile for {}", name);
    }

    public StudentProfile getProfileByName(String name) {
        return studentProfiles.stream()
                .filter(student -> student.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}

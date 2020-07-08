package com.sv.demo.service;

import com.sv.demo.client.ProfileClient;
import com.sv.demo.dto.StudentProfile;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@ApplicationScoped
public class StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    private final Set<StudentProfile> studentProfiles = new HashSet<>();

    @Inject
    @RestClient
    private ProfileClient profileClient;

    @Incoming("student")
    public CompletionStage<Void> receive(Message<String> message) {
        StudentProfile profile = profileClient.getByName(message.getPayload());
        studentProfiles.add(profile);
        message.getMetadata(IncomingAmqpMetadata.class)
                .ifPresent(meta -> MDC.put("traceId", meta.getCorrelationId()));
        LOG.info("Created profile for {}", message.getPayload());
        return message.ack();
    }

    public StudentProfile getProfileByName(String name) {
        return studentProfiles.stream()
                .filter(student -> student.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}

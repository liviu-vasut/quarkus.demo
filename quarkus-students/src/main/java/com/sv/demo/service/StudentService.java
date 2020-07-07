package com.sv.demo.service;

import com.sv.demo.dto.Student;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class StudentService {

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

    @Inject
    @Channel("student")
    Emitter<String> studentEmitter;

    @Inject
    @Named("countErrorGenerator")
    Runnable countErrorGenerator;

    private final AtomicLong requestCounter = new AtomicLong(0);

    private final Set<Student> students = Collections.synchronizedSet(new HashSet<>());

    public Set<Student> findAll() {
        return students;
    }

    public Student findById(long id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @CircuitBreaker(requestVolumeThreshold = 4)
    public Student findByName(String name) {
        countErrorGenerator.run();
        return students.stream()
                .filter(student -> student.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    public Student add(Student student) {
        students.add(student);
        studentEmitter.send(student.getName());
        LOG.info("Added student {}", student.getName());
        return student;
    }

    public void delete(long id) {
        students.removeIf(f -> f.getId() == id);
    }

    @Gauge(name = "dbCount", unit = MetricUnits.NONE, description = "Database element count.")
    public int databaseSize() {
        return students.size();
    }

}

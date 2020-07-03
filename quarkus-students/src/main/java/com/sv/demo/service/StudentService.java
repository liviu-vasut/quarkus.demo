package com.sv.demo.service;

import com.sv.demo.dto.Student;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class StudentService {

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

    public Student add(Student student) {
        students.add(student);
        return student;
    }

    public void delete(long id) {
        students.removeIf(f -> f.getId() == id);
    }
}

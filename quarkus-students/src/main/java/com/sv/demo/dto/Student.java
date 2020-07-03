package com.sv.demo.dto;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Student {

    private static final AtomicLong counter = new AtomicLong(0);

    private long id;
    private String name;
    private String location;

    public Student() {
        id = counter.getAndIncrement();
    }

    public Student(String name, String location) {
        this();
        this.name = name;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                Objects.equals(name, student.name) &&
                Objects.equals(location, student.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }
}

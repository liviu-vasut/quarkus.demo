package com.sv.demo.rest.json;

import com.sv.demo.dto.Student;
import com.sv.demo.service.StudentService;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class StudentResourceTest {

    @Inject
    StudentService studentService;

    @Test
    public void testListEndpoint() {
        given()
                .when().get("/student/list")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    public void testGetByIdEndpoint() {
        Student student = new Student("a name", "a location");
        QuarkusMock.installMockForInstance(new StudentService() {
            @Override
            public Student findById(long id) {
                return student;
            }
        }, studentService);

        Student responseStudent = given()
                .when().get("/student/1")
                .then()
                .statusCode(200)
                //can test each field individually
                .body("name", equalTo(student.getName()))
                //or extract the whole object and assert that
                .contentType(ContentType.JSON)
                .extract()
                .response()
                .as(Student.class);

        assertEquals(student, responseStudent);
    }

}

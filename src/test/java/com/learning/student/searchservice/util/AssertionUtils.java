package com.learning.student.searchservice.util;

import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.persistance.model.Student;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssertionUtils {
    private AssertionUtils() {
    }

    public static void assertStudents(Student expectedStudent, Student actualStudent) {
        assertEquals(expectedStudent.getId(), actualStudent.getId());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
        assertEquals(expectedStudent.getCnp(), actualStudent.getCnp());
        assertEquals(expectedStudent.isValid(), actualStudent.isValid());
    }

    public static void assertStudentDto(Student expectedStudent, StudentDto actualStudent) {
        assertEquals(expectedStudent.getId(), actualStudent.getId());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
        assertEquals(expectedStudent.getCnp(), actualStudent.getCnp());
        assertEquals(expectedStudent.isValid(), actualStudent.isValid());
    }
}

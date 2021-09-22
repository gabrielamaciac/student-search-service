package com.learning.student.searchservice.util;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;

import java.util.UUID;

public class SearchTestData {
    public static final String STUDENT_ID = UUID.randomUUID().toString();
    public static final String TEST_FIRST_NAME = "TestFirstName";
    public static final String UPDATED_FIRST_NAME = "TestFirstNameUpdated";
    public static final String TEST_LAST_NAME = "TestLastName";
    public static final String TEST_CNP = UUID.randomUUID().toString().replace("-", "");

    private SearchTestData() {
    }

    public static Student getStudent() {
        Student student = new Student();
        student.setId(STUDENT_ID);
        student.setFirstName(TEST_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setCnp(TEST_CNP);
        student.setValid(true);
        return student;
    }

    public static StudentUpdate getUpdatedStudent() {
        StudentUpdate student = new StudentUpdate();
        student.setFirstName(UPDATED_FIRST_NAME);
        student.setLastName(TEST_LAST_NAME);
        student.setCnp(TEST_CNP);
        student.setValid(true);
        return student;
    }

    public static String getSearchJson(String operationType) {
        return "{\"operationType\":\"" + operationType + "\",\"student\":{\"id\":\"e3015a0b-f4d3-4cbf-9da3-f9dd223f3c52\",\"firstName\":\"TestFirstName\",\"lastName\":\"TestLastName\",\"cnp\":\"16523888877566\",\"valid\":false}}";

    }
}

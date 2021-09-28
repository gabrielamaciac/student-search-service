package com.learning.student.searchservice.service;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;

import java.util.List;

public interface SearchService {
    Student findById(String id);

    List<Student> findByCustomQuery(String searchTerm, int page, int size);

    List<Student> findOnlyValidStudents(boolean isValid, int page, int size);

    Student findByNameAndCnp(String firstName, String lastName, String cnp);

    Student create(Student student);

    void update(String id, StudentUpdate student);

    void delete(Student student);
}

package com.learning.student.searchservice.facade;

import com.learning.student.searchservice.persistance.model.Student;

import java.util.List;

public interface SearchFacade {
    Student findById(String id);

    List<Student> findByCustomQuery(String searchTerm, int page, int size);

    List<Student> findOnlyValidStudents(boolean isValid, int page, int size);

    Student create(Student student);

    void update(Student student);

    void delete(Student student);
}

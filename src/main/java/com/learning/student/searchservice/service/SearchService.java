package com.learning.student.searchservice.service;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.repository.SearchRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SearchService {
    SearchRepository searchRepository;

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public Student findById(String id) {
        Optional<Student> student = searchRepository.findById(id);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new NoSuchElementException("No student found.");
        }
    }

    public List<Student> findByCustomQuery(String searchTerm, int page, int size) {
        return searchRepository.findByUserQuery(searchTerm, PageRequest.of(page, size)).getContent();
    }

    public List<Student> findByFirstName(String firstName, int page, int size) {
        return searchRepository.findByFirstName(firstName, PageRequest.of(page, size)).getContent();
    }

    public List<Student> findByLastName(String lastName, int page, int size) {
        return searchRepository.findByLastName(lastName, PageRequest.of(page, size)).getContent();
    }

    public List<Student> findByCnp(String cnp, int page, int size) {
        return searchRepository.findByCnp(cnp, PageRequest.of(page, size)).getContent();
    }

    public List<Student> findOnlyValidStudents(boolean isValid, int page, int size) {
        return searchRepository.findByIsValid(isValid, PageRequest.of(page, size)).getContent();
    }

    public Student saveStudent(String id, Student student) {
        Optional<Student> studentFound = searchRepository.findById(id);
        if (studentFound.isPresent()) {
            //update if it already exists
            Student existingStudent = studentFound.get();
            existingStudent.setFirstName(student.getFirstName());
            existingStudent.setLastName(student.getLastName());
            existingStudent.setCnp(student.getCnp());
            existingStudent.setValid(student.isValid());
            return searchRepository.save(existingStudent);
        } else {
            // create if not
            return searchRepository.save(student);
        }
    }
}

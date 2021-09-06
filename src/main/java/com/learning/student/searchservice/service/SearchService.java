package com.learning.student.searchservice.service;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.repository.SearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
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
            throw new NoSuchElementException("No student found with the given id.");
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

    public Student create(Student student) {
        return searchRepository.save(student);
    }

    public void update(Student student) {
        Student studentFound = findById(student.getId());
        if (studentFound != null) {
            studentFound.setId(student.getId());
            studentFound.setFirstName(student.getFirstName());
            studentFound.setLastName(student.getLastName());
            studentFound.setCnp(student.getCnp());
            studentFound.setValid(student.isValid());
            searchRepository.save(studentFound);
        } else {
            log.info("Student was not updated.");
        }
    }

    public void delete(Student student) {
        searchRepository.deleteById(student.getId());
        log.info("Student deleted from solr.");
    }
}

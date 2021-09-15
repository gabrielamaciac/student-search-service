package com.learning.student.searchservice.service;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.repository.SearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    SearchRepository searchRepository;

    public SearchServiceImpl(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public Student findById(String id) {
        return searchRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No student found with the given id."));
    }

    @Override
    public List<Student> findByCustomQuery(String searchTerm, int page, int size) {
        return searchRepository.findByUserQuery(searchTerm, PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<Student> findOnlyValidStudents(boolean isValid, int page, int size) {
        return searchRepository.findByIsValid(isValid, PageRequest.of(page, size)).getContent();
    }

    @Override
    public Student create(Student student) {
        Student saved = searchRepository.save(student);
        log.info("Student created in solr." + saved.getFirstName());
        return saved;
    }

    @Override
    public void update(Student student) {
        Student studentFound = findById(student.getId());
        studentFound.setId(student.getId());
        studentFound.setFirstName(student.getFirstName());
        studentFound.setLastName(student.getLastName());
        studentFound.setCnp(student.getCnp());
        studentFound.setValid(student.isValid());
        searchRepository.save(studentFound);
        log.info("Student updated in solr.");
    }

    @Override
    public void delete(Student student) {
        searchRepository.deleteById(student.getId());
        log.info("Student deleted from solr.");
    }
}

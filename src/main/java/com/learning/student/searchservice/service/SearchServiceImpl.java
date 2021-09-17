package com.learning.student.searchservice.service;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;
import com.learning.student.searchservice.persistance.repository.SearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    private final SearchRepository searchRepository;
    private final ModelMapper modelMapper = new ModelMapper();

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
    public List<Student> findByNameAndCnp(String firstName, String lastName, String cnp, int page, int size) {
        return searchRepository.findByFirstNameAndLastAndCnp(firstName, lastName, cnp, PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<Student> findOnlyValidStudents(boolean isValid, int page, int size) {
        return searchRepository.findByIsValid(isValid, PageRequest.of(page, size)).getContent();
    }

    @Override
    public Student create(Student student) {
        Student saved = searchRepository.save(student);
        log.info("Student created in solr: " + saved.getFirstName() + " " + saved.getLastName());
        return saved;
    }

    @Override
    public void update(String id, StudentUpdate studentUpdate) {
        Student studentFound = findById(id);
        modelMapper.map(studentUpdate, studentFound);
        Student saved = searchRepository.save(studentFound);
        log.info("Student updated in solr: " + saved.getFirstName() + " " + saved.getLastName());
    }

    @Override
    public void delete(Student student) {
        searchRepository.deleteById(student.getId());
        log.info("Student deleted from solr: " + student.getFirstName() + " " + student.getLastName());
    }
}

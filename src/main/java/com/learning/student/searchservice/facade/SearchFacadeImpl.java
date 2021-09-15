package com.learning.student.searchservice.facade;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.service.SearchService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchFacadeImpl implements SearchFacade {
    private final SearchService searchService;

    public SearchFacadeImpl(SearchService searchService) {
        this.searchService = searchService;
    }

    @Override
    public Student findById(String id) {
        return searchService.findById(id);
    }

    @Override
    public List<Student> findByCustomQuery(String searchTerm, int page, int size) {
        return searchService.findByCustomQuery(searchTerm, page, size);
    }

    @Override
    public List<Student> findOnlyValidStudents(boolean isValid, int page, int size) {
        return searchService.findOnlyValidStudents(isValid, page, size);
    }

    @Override
    public Student create(Student student) {
        return searchService.create(student);
    }

    @Override
    public void update(Student student) {
        searchService.update(student);
    }

    @Override
    public void delete(Student student) {
        searchService.delete(student);
    }
}

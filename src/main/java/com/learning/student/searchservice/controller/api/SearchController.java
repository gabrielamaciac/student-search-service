package com.learning.student.searchservice.controller.api;

import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.facade.SearchFacade;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest controller used for performing search queries on students [read only].
 */
@RestController
@Slf4j
public class SearchController implements SearchApi {
    private final SearchFacade searchFacade;

    public SearchController(SearchFacade searchFacade) {
        this.searchFacade = searchFacade;
    }

    @Override
    public ResponseEntity<List<StudentDto>> findBySearchTerm(String searchTerm, int pageNo, int pageSize) {
        List<Student> students = searchFacade.findByCustomQuery(searchTerm, pageNo, pageSize);
        return convertResponse(students);
    }

    @Override
    public ResponseEntity<List<StudentDto>> findByNameAndCnp(String firstName, String lastName, String cnp, int pageNo, int pageSize) {
        List<Student> students = searchFacade.findByNameAndCnp(firstName, lastName, cnp, pageNo, pageSize);
        return convertResponse(students);
    }

    @Override
    public StudentDto findById(String id) {
        return StudentMapper.convertStudentToStudentDto(searchFacade.findById(id));
    }

    @Override
    public ResponseEntity<List<StudentDto>> findValidStudents(String valid, int pageNo, int pageSize) {
        boolean isValid = valid.equalsIgnoreCase("true");
        List<Student> students = searchFacade.findOnlyValidStudents(isValid, pageNo, pageSize);
        return convertResponse(students);
    }

    private ResponseEntity<List<StudentDto>> convertResponse(List<Student> students) {
        List<StudentDto> response = students.stream().map(StudentMapper::convertStudentToStudentDto).collect(Collectors.toList());
        log.info("Students found: " + response.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

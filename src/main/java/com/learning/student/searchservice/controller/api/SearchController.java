package com.learning.student.searchservice.controller.api;

import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.facade.SearchFacade;
import com.learning.student.searchservice.persistance.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public SearchController(SearchFacade searchFacade, ModelMapper modelMapper) {
        this.searchFacade = searchFacade;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<List<StudentDto>> findBySearchTerm(String searchTerm, int pageNo, int pageSize) {
        List<Student> students = searchFacade.findByCustomQuery(searchTerm, pageNo, pageSize);
        return convertResponse(students);
    }

    @Override
    public ResponseEntity<StudentDto> findByNameAndCnp(String firstName, String lastName, String cnp) {
        Student student = searchFacade.findByNameAndCnp(firstName, lastName, cnp);
        log.info("Student found by name and cnp: " + student.getFirstName());
        return new ResponseEntity<>(mapStudentToStudentDto(student), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StudentDto> findById(String id) {
        return new ResponseEntity<>(mapStudentToStudentDto(searchFacade.findById(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<StudentDto>> findValidStudents(String valid, int pageNo, int pageSize) {
        boolean isValid = valid.equalsIgnoreCase("true");
        List<Student> students = searchFacade.findOnlyValidStudents(isValid, pageNo, pageSize);
        return convertResponse(students);
    }

    private ResponseEntity<List<StudentDto>> convertResponse(List<Student> students) {
        List<StudentDto> response = students.stream().map(this::mapStudentToStudentDto).collect(Collectors.toList());
        log.info("Students found: " + response.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private StudentDto mapStudentToStudentDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }
}

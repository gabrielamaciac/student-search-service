package com.learning.student.searchservice.controller.api;

import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.service.SearchService;
import com.learning.student.searchservice.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest controller used for performing search on students [GET].
 */
@RestController
@RequestMapping("/student/search")
@Slf4j
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("")
    public ResponseEntity<List<StudentDto>> findBySearchTerm(@RequestParam String searchTerm,
                                                             @RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> students = searchService.findByCustomQuery(searchTerm, pageNo, pageSize);
        return convertResponse(students);
    }

    @GetMapping("/id/{id}")
    public Student findById(@PathVariable(name = "id") String id) {
        return searchService.findById(id);
    }

    @GetMapping("/firstName/{firstName}")
    public ResponseEntity<List<StudentDto>> findByFirstName(@PathVariable String firstName,
                                                            @RequestParam(defaultValue = "0") int pageNo,
                                                            @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> students = searchService.findByFirstName(firstName, pageNo, pageSize);
        return convertResponse(students);

    }

    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<StudentDto>> findByLastName(@PathVariable String lastName,
                                                           @RequestParam(defaultValue = "0") int pageNo,
                                                           @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> students = searchService.findByLastName(lastName, pageNo, pageSize);
        return convertResponse(students);
    }

    @GetMapping("/cnp/{cnp}")
    public ResponseEntity<List<StudentDto>> findByCnp(@PathVariable String cnp,
                                                      @RequestParam(defaultValue = "0") int pageNo,
                                                      @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> students = searchService.findByCnp(cnp, pageNo, pageSize);
        return convertResponse(students);
    }

    @GetMapping("/isValid/{valid}")
    public ResponseEntity<List<StudentDto>> findValidStudents(@PathVariable String valid,
                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
        boolean isValid = valid.equalsIgnoreCase("true");
        List<Student> students = searchService.findOnlyValidStudents(isValid, pageNo, pageSize);
        return convertResponse(students);
    }


    private ResponseEntity<List<StudentDto>> convertResponse(List<Student> students) {
        List<StudentDto> response = students.stream().map(StudentMapper::convertStudentToStudentDto).collect(Collectors.toList());
        log.info("Students found: " + response.size());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

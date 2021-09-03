package com.learning.student.searchservice.controller.api;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller used for performing search on students [GET].
 */
@RestController
@RequestMapping("/student/search")
@AllArgsConstructor
public class SearchController {
    SearchService searchService;

    @GetMapping("/{id}")
    public Student findById(@PathVariable(name = "id") String id) {
        return searchService.findById(id);
    }

    @GetMapping("")
    public List<Student> findBySearchTerm(@RequestParam String searchTerm,
                                          @RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        return searchService.findByCustomQuery(searchTerm, pageNo, pageSize);
    }

    @GetMapping("/firstName/{firstName}")
    public List<Student> findByFirstName(@PathVariable String firstName,
                                         @RequestParam(defaultValue = "0") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        return searchService.findByFirstName(firstName, pageNo, pageSize);
    }

    @GetMapping("/lastName/{lastName}")
    public List<Student> findByLastName(@PathVariable String lastName,
                                        @RequestParam(defaultValue = "0") int pageNo,
                                        @RequestParam(defaultValue = "10") int pageSize) {
        return searchService.findByLastName(lastName, pageNo, pageSize);
    }

    @GetMapping("/cnp/{cnp}")
    public List<Student> findByCnp(@PathVariable String cnp,
                                   @RequestParam(defaultValue = "0") int pageNo,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return searchService.findByCnp(cnp, pageNo, pageSize);
    }

    @GetMapping("/isValid/{valid}")
    public List<Student> findValidStudents(@PathVariable String valid,
                                           @RequestParam(defaultValue = "0") int pageNo,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        boolean isValid = valid.equalsIgnoreCase("true");
        return searchService.findOnlyValidStudents(isValid, pageNo, pageSize);
    }
}

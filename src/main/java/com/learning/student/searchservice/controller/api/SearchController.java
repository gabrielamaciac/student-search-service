package com.learning.student.searchservice.controller.api;

import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.facade.SearchFacade;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.util.StudentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * Rest controller used for performing search queries on students [read only].
 */
@RestController
@Tag(name = "Student Search Service", description = "Search students through SOLr.")
@RequestMapping("/student/search")
@Slf4j
public class SearchController implements SearchApi {
    private final SearchFacade searchFacade;

    public SearchController(SearchFacade searchFacade) {
        this.searchFacade = searchFacade;
    }

    @Override
    @Operation(summary = "Search students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students matching the search query found.", content = {
                    @Content(mediaType = "application/json", array =
                    @ArraySchema(schema = @Schema(implementation = StudentDto.class)))}
            )})
    @GetMapping("")
    public ResponseEntity<List<StudentDto>> findBySearchTerm(@RequestParam String searchTerm,
                                                             @RequestParam(defaultValue = "0") int pageNo,
                                                             @RequestParam(defaultValue = "10") int pageSize) {
        List<Student> students = searchFacade.findByCustomQuery(searchTerm, pageNo, pageSize);
        return convertResponse(students);
    }

    @Override
    @Operation(summary = "Get a student by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the student.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @GetMapping("/id/{id}")
    public StudentDto findById(@PathVariable(name = "id") String id) {
        return StudentMapper.convertStudentToStudentDto(searchFacade.findById(id));
    }

    @Override
    @Operation(summary = "Find all valid students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid students found.", content = {
                    @Content(mediaType = "application/json", array =
                    @ArraySchema(schema = @Schema(implementation = StudentDto.class)))}
            )})
    @GetMapping("/isValid/{valid}")
    public ResponseEntity<List<StudentDto>> findValidStudents(@PathVariable String valid,
                                                              @RequestParam(defaultValue = "0") int pageNo,
                                                              @RequestParam(defaultValue = "10") int pageSize) {
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

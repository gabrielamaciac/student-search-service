package com.learning.student.searchservice.controller.api;

import com.learning.student.searchservice.controller.model.StudentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "Student Search Service", description = "Search students through SOLr.")
@RequestMapping("/search")
public interface SearchApi {

    @Operation(summary = "Search students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Students matching the search query found.", content = {
                    @Content(mediaType = "application/json", array =
                    @ArraySchema(schema = @Schema(implementation = StudentDto.class)))}
            )})
    @GetMapping("")
    ResponseEntity<List<StudentDto>> findBySearchTerm(@RequestParam @NotBlank String searchTerm,
                                                      @RequestParam(defaultValue = "0") int pageNo,
                                                      @RequestParam(defaultValue = "10") int pageSize);

    @Operation(summary = "Search students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the student.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @GetMapping("/student")
    ResponseEntity<StudentDto> findByNameAndCnp(@RequestParam @NotBlank String firstName,
                                                @RequestParam @NotBlank String lastName,
                                                @RequestParam @NotBlank String cnp);

    @Operation(summary = "Get a student by its id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the student.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StudentDto.class))}),
            @ApiResponse(responseCode = "404", description = "Student not found",
                    content = @Content)})
    @GetMapping("student/id/{id}")
    ResponseEntity<StudentDto> findById(@PathVariable(name = "id") @NotBlank String id);

    @Operation(summary = "Find all valid students.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valid students found.", content = {
                    @Content(mediaType = "application/json", array =
                    @ArraySchema(schema = @Schema(implementation = StudentDto.class)))}
            )})
    @GetMapping("student/isValid/{valid}")
    ResponseEntity<List<StudentDto>> findValidStudents(@PathVariable @NotBlank String valid,
                                                       @RequestParam(defaultValue = "0") int pageNo,
                                                       @RequestParam(defaultValue = "10") int pageSize);
}

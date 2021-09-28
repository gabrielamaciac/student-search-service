package com.learning.student.searchservice.controller;

import com.learning.student.searchservice.controller.api.SearchController;
import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.facade.SearchFacade;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.util.AssertionUtils;
import com.learning.student.searchservice.util.SearchTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link SearchController}
 */
class SearchControllerTest {
    private SearchFacade searchFacade;
    private ModelMapper modelMapper;
    private SearchController searchController;

    private Student expectedStudent = SearchTestData.getStudent();
    private List<Student> expectedList = Arrays.asList(expectedStudent);

    @BeforeEach
    void setUp() {
        searchFacade = mock(SearchFacade.class);
        modelMapper = new ModelMapper();
        searchController = new SearchController(searchFacade, modelMapper);
    }

    @Test
    void findBySearchTermReturns200StatusCodeAndCorrectStudentList() {
        // given
        given(searchFacade.findByCustomQuery(any(String.class), any(Integer.class), any(Integer.class)))
                .willReturn(expectedList);

        // when
        ResponseEntity<List<StudentDto>> response = searchController.findBySearchTerm("searchTerm", 0, 5);

        // then
        assertResponse(expectedList, response);
    }

    @Test
    void findByNameAndCnpReturns200StatusCodeAndCorrectStudentList() {
        // given
        given(searchFacade.findByNameAndCnp(any(String.class), any(String.class), any(String.class))).willReturn(expectedStudent);

        // when
        ResponseEntity<StudentDto> response = searchController.findByNameAndCnp("FirstName", "LastName", "Cnp");

        // then
        AssertionUtils.assertStudentDto(expectedStudent, response.getBody());
    }

    @Test
    void findByIdReturns200StatusCodeAndCorrectStudent() {
        // given
        given(searchFacade.findById(any(String.class))).willReturn(expectedStudent);

        // when
        ResponseEntity<StudentDto> response = searchController.findById(SearchTestData.STUDENT_ID);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudent.getId(), response.getBody().getId());
    }

    @Test
    void findValidStudentsReturns200StatusCodeAndCorrectStudentList() {
        // given
        given(searchFacade.findOnlyValidStudents(any(Boolean.class),
                any(Integer.class), any(Integer.class))).willReturn(expectedList);

        // when
        ResponseEntity<List<StudentDto>> response = searchController.findValidStudents("true", 0, 5);

        // then
        assertResponse(expectedList, response);
    }

    private void assertResponse(List<Student> expectedList, ResponseEntity<List<StudentDto>> response) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList.size(), response.getBody().size());
        assertEquals(expectedList.get(0).getId(), response.getBody().get(0).getId());
        assertEquals(expectedList.get(0).getFirstName(), response.getBody().get(0).getFirstName());
        assertEquals(expectedList.get(0).getLastName(), response.getBody().get(0).getLastName());
        assertEquals(expectedList.get(0).getCnp(), response.getBody().get(0).getCnp());
        assertEquals(expectedList.get(0).isValid(), response.getBody().get(0).isValid());
    }
}

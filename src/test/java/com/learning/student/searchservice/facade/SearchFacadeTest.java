package com.learning.student.searchservice.facade;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;
import com.learning.student.searchservice.service.SearchService;
import com.learning.student.searchservice.service.SearchServiceImpl;
import com.learning.student.searchservice.util.AssertionUtils;
import com.learning.student.searchservice.util.SearchTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SearchFacade}
 */
class SearchFacadeTest {
    private SearchService searchService;
    private SearchFacade searchFacade;

    private Student expectedStudent = SearchTestData.getStudent();
    private List<Student> expectedList = Arrays.asList(expectedStudent);

    @BeforeEach
    void setUp() {
        searchService = mock(SearchServiceImpl.class);
        searchFacade = new SearchFacadeImpl(searchService);
    }

    @Test
    void findByIdReturnsValidStudent() {
        // Given
        when(searchService.findById(SearchTestData.STUDENT_ID)).thenReturn(expectedStudent);

        // When
        Student actualStudent = searchFacade.findById(SearchTestData.STUDENT_ID);

        // Then
        AssertionUtils.assertStudents(expectedStudent, actualStudent);
    }

    @Test
    void findByCustomQueryReturnsValidList() {
        // Given
        when(searchService.findByCustomQuery(any(String.class), any(Integer.class), any(Integer.class))).thenReturn(expectedList);

        // When
        List<Student> actualList = searchFacade.findByCustomQuery("searchTerm", 0, 5);

        // Then
        assertFalse(actualList.isEmpty());
        AssertionUtils.assertStudents(expectedList.get(0), actualList.get(0));
    }

    @Test
    void findByNameAndCnpReturnsValidList() {
        // Given
        when(searchService.findByNameAndCnp(any(String.class), any(String.class), any(String.class)))
                .thenReturn(expectedStudent);

        // When
        Student actualStudent = searchFacade.findByNameAndCnp("FirstName", "LastName", "Cnp");

        // Then
        AssertionUtils.assertStudents(expectedStudent, actualStudent);
    }

    @Test
    void findOnlyValidStudentsReturnsValidList() {
        // Given
        when(searchService.findOnlyValidStudents(any(Boolean.class), any(Integer.class), any(Integer.class))).thenReturn(expectedList);

        // When
        List<Student> actualList = searchFacade.findOnlyValidStudents(true, 0, 5);

        // Then
        assertFalse(actualList.isEmpty());
        AssertionUtils.assertStudents(expectedList.get(0), actualList.get(0));
    }

    @Test
    void createReturnsValidStudent() {
        // Given
        when(searchService.create(expectedStudent)).thenReturn(expectedStudent);

        // When
        Student actualStudent = searchFacade.create(expectedStudent);

        // Then
        AssertionUtils.assertStudents(expectedStudent, actualStudent);
    }

    @Test
    void updateStudentIsSuccessful() {
        // Given
        ArgumentCaptor<StudentUpdate> argumentCaptor = ArgumentCaptor.forClass(StudentUpdate.class);
        StudentUpdate updatedStudent = SearchTestData.getUpdatedStudent();

        // When
        searchFacade.update(SearchTestData.STUDENT_ID, updatedStudent);

        // Then
        verify(searchService).update(any(String.class), argumentCaptor.capture());
        assertEquals(SearchTestData.UPDATED_FIRST_NAME, argumentCaptor.getValue().getFirstName());
    }

    @Test
    void deleteByIdIsSuccessful() {
        // Given
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);

        // When
        searchFacade.delete(expectedStudent);

        // Then
        verify(searchService).delete(argumentCaptor.capture());
        assertEquals(SearchTestData.STUDENT_ID, argumentCaptor.getValue().getId());
    }
}
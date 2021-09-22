package com.learning.student.searchservice.service;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;
import com.learning.student.searchservice.persistance.repository.SearchRepository;
import com.learning.student.searchservice.util.SearchTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link SearchService}
 */
class SearchServiceTest {
    private SearchRepository searchRepository;
    private SearchService searchService;
    private ModelMapper modelMapper;

    private Student expectedStudent = SearchTestData.getStudent();
    private List<Student> expectedList = Arrays.asList(expectedStudent);
    private Page<Student> expectedPagedResponse = new PageImpl<>(expectedList);


    @BeforeEach
    void setUp() {
        searchRepository = mock(SearchRepository.class);
        modelMapper = new ModelMapper();
        searchService = new SearchServiceImpl(searchRepository, modelMapper);
    }

    @Test
    void findByIdReturnsValidStudent() {
        // Given
        when(searchRepository.findById(SearchTestData.STUDENT_ID)).thenReturn(Optional.of(expectedStudent));

        // When
        Student actualStudent = searchService.findById(SearchTestData.STUDENT_ID);

        assertStudents(expectedStudent, actualStudent);
    }

    @Test
    void findByIdThrowsException() {
        // Given
        when(searchRepository.findById(SearchTestData.STUDENT_ID)).thenReturn(Optional.empty());

        // When Then
        assertThrows(NoSuchElementException.class, () -> searchService.findById(SearchTestData.STUDENT_ID));
    }

    @Test
    void findByCustomQueryReturnsValidList() {
        // Given
        when(searchRepository.findByUserQuery(any(String.class), eq(PageRequest.of(0, 5)))).thenReturn(expectedPagedResponse);

        // When
        List<Student> actualList = searchService.findByCustomQuery("searchTerm", 0, 5);

        // Then
        assertFalse(actualList.isEmpty());
        assertStudents(expectedList.get(0), actualList.get(0));
    }

    @Test
    void findByNameAndCnpReturnsValidList() {
        // Given
        when(searchRepository.findByFirstNameAndLastAndCnp(any(String.class), any(String.class), any(String.class), eq(PageRequest.of(0, 5))))
                .thenReturn(expectedPagedResponse);

        // When
        List<Student> actualList = searchService.findByNameAndCnp("FirstName", "LastName", "Cnp", 0, 5);

        // Then
        assertFalse(actualList.isEmpty());
        assertStudents(expectedList.get(0), actualList.get(0));
    }

    @Test
    void findOnlyValidStudentsReturnsValidList() {
        // Given
        when(searchRepository.findByIsValid(any(Boolean.class), eq(PageRequest.of(0, 5)))).thenReturn(expectedPagedResponse);

        // When
        List<Student> actualList = searchService.findOnlyValidStudents(true, 0, 5);

        // Then
        assertFalse(actualList.isEmpty());
        assertStudents(expectedList.get(0), actualList.get(0));
    }

    @Test
    void createReturnsValidStudent() {
        // Given
        when(searchRepository.save(expectedStudent)).thenReturn(expectedStudent);

        // When
        Student actualStudent = searchService.create(expectedStudent);

        // Then
        assertStudents(expectedStudent, actualStudent);
    }

    @Test
    void updateStudentIsSuccessful() {
        // Given
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        StudentUpdate updatedStudent = SearchTestData.getUpdatedStudent();
        when(searchRepository.findById(SearchTestData.STUDENT_ID)).thenReturn(Optional.of(expectedStudent));
        when(searchRepository.save(expectedStudent)).thenReturn(expectedStudent);

        // When
        searchService.update(SearchTestData.STUDENT_ID, updatedStudent);

        // Then
        verify(searchRepository).save(argumentCaptor.capture());
        assertEquals(SearchTestData.UPDATED_FIRST_NAME, argumentCaptor.getValue().getFirstName());
    }

    @Test
    void deleteByIdIsSuccessful() {
        // Given
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        // When
        searchService.delete(expectedStudent);

        // Then
        verify(searchRepository).deleteById(argumentCaptor.capture());
        assertEquals(SearchTestData.STUDENT_ID, argumentCaptor.getValue());
    }

    private void assertStudents(Student expectedStudent, Student actualStudent) {
        assertEquals(expectedStudent.getId(), actualStudent.getId());
        assertEquals(expectedStudent.getFirstName(), actualStudent.getFirstName());
        assertEquals(expectedStudent.getLastName(), actualStudent.getLastName());
        assertEquals(expectedStudent.getCnp(), actualStudent.getCnp());
        assertEquals(expectedStudent.isValid(), actualStudent.isValid());
    }
}

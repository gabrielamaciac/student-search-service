package com.learning.student.searchservice.integration.queue;

import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;
import com.learning.student.searchservice.service.SearchService;
import com.learning.student.searchservice.util.SearchTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link StudentServiceConsumer}
 */
class StudentServiceConsumerTest {

    private SearchService searchService;
    private ModelMapper modelMapper;
    private StudentServiceConsumer studentServiceConsumer;

    @BeforeEach
    void setUp() {
        searchService = mock(SearchService.class);
        modelMapper = new ModelMapper();
        studentServiceConsumer = new StudentServiceConsumer(searchService, modelMapper);
    }

    @Test
    void processMessageForCreateIsSuccessful() {
        studentServiceConsumer.receiveMessage(SearchTestData.getSearchJson("CREATE"));

        verify(searchService).create(any(Student.class));
    }

    @Test
    void processMessageForUpdateIsSuccessful() {
        studentServiceConsumer.receiveMessage(SearchTestData.getSearchJson("UPDATE"));

        verify(searchService).update(any(String.class), any(StudentUpdate.class));
    }

    @Test
    void processMessageForDeleteIsSuccessful() {
        studentServiceConsumer.receiveMessage(SearchTestData.getSearchJson("DELETE"));

        verify(searchService).delete(any(Student.class));
    }
}

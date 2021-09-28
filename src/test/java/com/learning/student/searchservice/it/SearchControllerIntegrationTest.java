package com.learning.student.searchservice.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.searchservice.controller.api.SearchApi;
import com.learning.student.searchservice.controller.api.SearchController;
import com.learning.student.searchservice.exception.NoSuchElementExceptionHandler;
import com.learning.student.searchservice.facade.SearchFacade;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.util.AssertionUtils;
import com.learning.student.searchservice.util.SearchTestData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchControllerIntegrationTest {
    private final Student student = SearchTestData.getStudent();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SearchFacade searchFacade;
    @Autowired
    private ModelMapper modelMapper;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        SearchApi studentController = new SearchController(searchFacade, modelMapper);
        mvc = MockMvcBuilders.standaloneSetup(studentController)
                .setControllerAdvice(new NoSuchElementExceptionHandler())
                .build();
    }

    @BeforeAll
    void beforeAll() {
        searchFacade.create(student);
    }

    @Test
    void testSearchById() throws Exception {
        MockHttpServletResponse response = getResponse("/search/student/id/" + student.getId());

        Student actualStudent = objectMapper.readValue(response.getContentAsString(), Student.class);
        AssertionUtils.assertStudents(student, actualStudent);
    }

    @Test
    void testSearchByCustomQuery() throws Exception {
        MockHttpServletResponse response = getResponse("/search?searchTerm=" + student.getCnp());

        List<Student> actualStudents = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        AssertionUtils.assertStudents(student, actualStudents.get(0));
    }

    @Test
    void testSearchByNameAndCnp() throws Exception {
        MockHttpServletResponse response = getResponse("/search/student?firstName=" + student.getFirstName()
                + "&lastName=" + student.getLastName() + "&cnp=" + student.getCnp());

        Student actualStudent = objectMapper.readValue(response.getContentAsString(), Student.class);
        AssertionUtils.assertStudents(student, actualStudent);
    }

    @Test
    void testSearchByValidStudents() throws Exception {
        MockHttpServletResponse response = getResponse("/search/student/isValid/true");

        List<Student> actualStudents = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        actualStudents.forEach(s -> assertTrue(s.isValid()));
    }

    private MockHttpServletResponse getResponse(String endpoint) throws Exception {
        return mvc.perform(get(endpoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

}

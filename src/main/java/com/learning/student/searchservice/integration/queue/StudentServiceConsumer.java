package com.learning.student.searchservice.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.searchservice.integration.model.StudentMessage;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Consumes the message from student-service and stores the indexed document.
 */
@Component
@Slf4j
public class StudentServiceConsumer {

    @Autowired
    SearchService searchService;

    ObjectMapper objectMapper = new ObjectMapper();
    ModelMapper modelMapper = new ModelMapper();

    public StudentServiceConsumer(SearchService searchService) {
        this.searchService = searchService;
    }

    @RabbitListener(queues = "search-queue")
    public void receiveMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        try {
            log.info("Processing message: " + message);
            StudentMessage student = objectMapper.readValue(message, StudentMessage.class);
            log.info("Received student from search-queue: " + student.getFirstName());
            Student savedStudent = searchService.saveStudent(student.getId(), modelMapper.map(student, Student.class));
            log.info("Student saved to solr: " + savedStudent.getFirstName());
        } catch (JsonProcessingException e) {
            log.error("Error processing received json: " + e);
        }
    }
}
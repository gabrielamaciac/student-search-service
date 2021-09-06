package com.learning.student.searchservice.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.searchservice.integration.model.OperationType;
import com.learning.student.searchservice.integration.model.SearchPayload;
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
            SearchPayload payload = objectMapper.readValue(message, SearchPayload.class);
            log.info("Received student from search-queue: " + payload.getStudent().getFirstName());
            savePayload(payload.getOperationType(), modelMapper.map(payload.getStudent(), Student.class));
            log.info("Student saved to solr.");
        } catch (JsonProcessingException e) {
            log.error("Error processing received json: " + e);
        }
    }

    private void savePayload(OperationType operationType, Student student) {
        switch (operationType) {
            case CREATE:
                searchService.create(student);
                break;
            case UPDATE:
                searchService.update(student);
                break;
            case DELETE:
                searchService.delete(student);
                break;
        }
    }
}
package com.learning.student.searchservice.integration.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.student.searchservice.integration.model.OperationType;
import com.learning.student.searchservice.integration.model.SearchPayload;
import com.learning.student.searchservice.integration.model.StudentMessage;
import com.learning.student.searchservice.service.SearchService;
import com.learning.student.searchservice.util.StudentMapper;
import lombok.extern.slf4j.Slf4j;
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
            // rename searchpayload
            SearchPayload payload = objectMapper.readValue(message, SearchPayload.class);
            log.info("Received student from search-queue: " + payload.getStudent().getFirstName() + " " + payload.getStudent().getLastName());
            savePayload(payload.getOperationType(), payload.getStudent());
        } catch (JsonProcessingException e) {
            log.error("Error processing received json: ", e);
        }
    }

    private void savePayload(OperationType operationType, StudentMessage studentMessage) {
        switch (operationType) {
            case CREATE:
                searchService.create(StudentMapper.convertStudentMessageToStudent(studentMessage));
                break;
            case UPDATE:
                searchService.update(studentMessage.getId(), StudentMapper.convertStudentMessageToStudentUpdate(studentMessage));
                break;
            case DELETE:
                searchService.delete(StudentMapper.convertStudentMessageToStudent(studentMessage));
                break;
        }
    }
}
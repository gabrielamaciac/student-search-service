package com.learning.student.searchservice.integration.queue;

import com.learning.student.searchservice.integration.model.OperationType;
import com.learning.student.searchservice.integration.model.SearchPayload;
import com.learning.student.searchservice.integration.model.StudentMessage;
import com.learning.student.searchservice.persistance.model.Student;
import com.learning.student.searchservice.persistance.model.StudentUpdate;
import com.learning.student.searchservice.service.SearchService;
import com.learning.student.searchservice.util.GenericMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes the message from student-service and stores the indexed document.
 */
@Component
@Slf4j
public class StudentServiceConsumer {

    private final SearchService searchService;
    private final ModelMapper modelMapper;

    public StudentServiceConsumer(SearchService searchService, ModelMapper modelMapper) {
        this.searchService = searchService;
        this.modelMapper = modelMapper;
    }

    @RabbitListener(queues = "search-queue")
    public void receiveMessage(String message) {
        processMessage(message);
    }

    private void processMessage(String message) {
        log.info("Processing message: " + message);
        SearchPayload payload = GenericMapper.readValue(message, SearchPayload.class);
        log.info("Received student from search-queue: " + payload.getStudent().getFirstName() + " " + payload.getStudent().getLastName());
        savePayload(payload.getOperationType(), payload.getStudent());
    }

    private void savePayload(OperationType operationType, StudentMessage studentMessage) {
        switch (operationType) {
            case CREATE:
                searchService.create(modelMapper.map(studentMessage, Student.class));
                break;
            case UPDATE:
                searchService.update(studentMessage.getId(), modelMapper.map(studentMessage, StudentUpdate.class));
                break;
            case DELETE:
                searchService.delete(modelMapper.map(studentMessage, Student.class));
                break;
        }
    }
}
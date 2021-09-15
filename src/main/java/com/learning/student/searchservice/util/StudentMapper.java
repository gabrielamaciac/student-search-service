package com.learning.student.searchservice.util;

import com.learning.student.searchservice.controller.model.StudentDto;
import com.learning.student.searchservice.integration.model.StudentMessage;
import com.learning.student.searchservice.persistance.model.Student;
import org.modelmapper.ModelMapper;

public class StudentMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    private StudentMapper() {
    }

    public static StudentDto convertStudentToStudentDto(Student student) {
        return modelMapper.map(student, StudentDto.class);
    }

    public static Student convertStudentMessageToStudent(StudentMessage studentMessage) {
        return modelMapper.map(studentMessage, Student.class);
    }
}

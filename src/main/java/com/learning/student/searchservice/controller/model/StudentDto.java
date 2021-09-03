package com.learning.student.searchservice.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDto {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private boolean isValid;
}

package com.learning.student.searchservice.integration.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentMessage {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private boolean isValid;
}

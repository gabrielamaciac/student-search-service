package com.learning.student.searchservice.persistance.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

/**
 * Document class
 */
@Getter
@Setter
@SolrDocument(collection = "Students")
public class Student {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;
    @Indexed(name = "first_name", type = "string")
    private String firstName;
    @Indexed(name = "last_name", type = "string")
    private String lastName;
    @Indexed(name = "cnp", type = "string")
    private String cnp;
    @Indexed(name = "is_valid", type = "boolean")
    private boolean isValid;
}


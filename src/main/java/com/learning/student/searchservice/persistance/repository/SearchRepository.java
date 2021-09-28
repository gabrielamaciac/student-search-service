package com.learning.student.searchservice.persistance.repository;

import com.learning.student.searchservice.persistance.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

import java.util.Optional;

public interface SearchRepository extends SolrCrudRepository<Student, String> {

    @Query("first_name:*?0* OR last_name:*?0* OR cnp:*?0*")
    Page<Student> findByUserQuery(String searchTerm, Pageable pageable);

    @Query("first_name:*?0* AND last_name:*?1* AND cnp:*?2*")
    Optional<Student> findByFirstNameAndLastAndCnp(String firstName, String lastName, String cnp);

    @Query("is_valid:?0")
    Page<Student> findByIsValid(boolean isValid, Pageable page);

    @Query(value = "id:?0", delete = true)
    void deleteById(String id);
}

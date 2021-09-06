package com.learning.student.searchservice.persistance.repository;

import com.learning.student.searchservice.persistance.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface SearchRepository extends SolrCrudRepository<Student, String> {

    @Query("first_name:*?0* OR last_name:*?0* OR cnp:*?0*")
    Page<Student> findByUserQuery(String searchTerm, Pageable pageable);

    @Query("first_name:*?0*")
    Page<Student> findByFirstName(String firstName, Pageable page);

    @Query("last_name:*?0*")
    Page<Student> findByLastName(String lastName, Pageable page);

    @Query("cnp:*?0*")
    Page<Student> findByCnp(String cnp, Pageable page);

    @Query("is_valid:*?0*")
    Page<Student> findByIsValid(boolean isValid, Pageable page);

    @Query(value = "id:?0", delete = true)
    void deleteById(String id);
}

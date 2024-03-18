package com.example.fitfusion.Person;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByUsername(String username);

    @Query("SELECT p FROM Person p where SUBSTRING(p.username, 1, :queryLength) = :prefix")
    List<Person> GetByMatchingPrefix(String prefix, int queryLength);


}

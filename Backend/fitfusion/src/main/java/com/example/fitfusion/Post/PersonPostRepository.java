package com.example.fitfusion.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPostRepository extends JpaRepository<PersonPost, Long> {



}

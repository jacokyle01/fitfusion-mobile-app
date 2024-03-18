package com.example.fitfusion.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BusinessPostRepository extends JpaRepository<BusinessPost, Long> {

}

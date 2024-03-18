package com.example.fitfusion.Business;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Business findByBusinessName(String businessName);

    @Query("SELECT b FROM Business b WHERE b.zipcode = :zipcode")
    List<Business> findByProximity(int zipcode);
}

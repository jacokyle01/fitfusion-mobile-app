package com.example.fitfusion.Challenge;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Challenge findById(int id);
    void deleteById(int id);

    @Query("SELECT c from Challenge c where c.challenger.username = :username")
    List<Challenge> getChallengesByChallenger(String username);

    @Query("SELECT c from Challenge c where c.challengee.username = :username")
    List<Challenge> getChallengesByChallengee(String username);
}

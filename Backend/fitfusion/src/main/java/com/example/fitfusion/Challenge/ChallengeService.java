package com.example.fitfusion.Challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fitfusion.Person.Person;

import jakarta.transaction.Transactional;

@Service
public class ChallengeService {
    @Autowired
    private ChallengeRepository challengeRepository;

    public Challenge createChallenge(Person challenger, Person challengee) {
        //TODO handle if challenger and challengee are not friends (?) 
        long timestamp = System.currentTimeMillis();
        Challenge challenge = new Challenge(challenger, challengee);
        return challengeRepository.save(challenge);
    }

    @Transactional
    public void declineChallenge(int id) {
        //TODO notify challenger?
        //TODO save somewhere? 
        challengeRepository.deleteById(id);
    }

    public Challenge acceptChallenge(int id) { 
        Challenge challenge = challengeRepository.findById(id);
        long now = System.currentTimeMillis();
        challenge.setAcceptedAt(now);
        challenge.setStatus(Status.IN_PROGRESS);
        challengeRepository.save(challenge);
        return challenge;
    }

    @Transactional
    public void completeChallenge(int id) {
        Challenge challenge = challengeRepository.findById(id);
        Person challengee = challenge.getChallengee();
        //reward challengee for completing challenge 
        challengee.setPoints(challengee.getPoints() + 10);
        challengeRepository.deleteById(id);
    }

    
}

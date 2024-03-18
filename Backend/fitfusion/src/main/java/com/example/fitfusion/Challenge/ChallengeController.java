package com.example.fitfusion.Challenge;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;

@RestController
public class ChallengeController {

    @Autowired
    ChallengeRepository challengeRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private ChallengeService challengeService;

    @GetMapping("/challenges")
    List<Challenge> listAllChallenges() {
        return challengeRepository.findAll();
    }

    @GetMapping("/challenges/{id}")
    Challenge getAChallenge(@PathVariable int id) {
        return challengeRepository.findById(id);
    }

    @GetMapping("/challenges/from/{username}")
    List<Challenge> getChallengesFrom(@PathVariable String username) {
        return challengeRepository.getChallengesByChallenger(username);
    }

    @GetMapping("/challenges/to/{username}")
    List<Challenge> getChallengesTo(@PathVariable String username) {
        return challengeRepository.getChallengesByChallengee(username);
    }

    @DeleteMapping("/challenges/{id}/decline")
    public ResponseEntity<Void> declineChallenge(@PathVariable int id) {
        challengeService.declineChallenge(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("challenges/{id}/accept")
    public ResponseEntity<Challenge> acceptChallenge(@PathVariable int id) {
        Challenge acceptedChallenge = challengeService.acceptChallenge(id);
        return new ResponseEntity<Challenge>(acceptedChallenge, HttpStatus.CREATED);
    }

    @DeleteMapping("challenges/{id}/complete")
    public ResponseEntity<Void> completeChallenge(@PathVariable int id) {
        challengeService.completeChallenge(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/people/@/{username}/challenge/{username2}")
    ResponseEntity<Challenge> challengeUser(@PathVariable String username, @PathVariable String username2) {
        Person challenger = personRepository.findByUsername(username);
        Person challengee = personRepository.findByUsername(username2);
        Challenge createdChallenge = challengeService.createChallenge(challenger, challengee);
        return new ResponseEntity<>(createdChallenge, HttpStatus.CREATED);
    }

}

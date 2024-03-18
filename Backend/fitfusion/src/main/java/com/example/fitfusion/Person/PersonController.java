package com.example.fitfusion.Person;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.fitfusion.Image.Image;
import com.example.fitfusion.Image.ImageController;
import com.example.fitfusion.Image.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.fitfusion.Business.Business;
import com.example.fitfusion.Business.BusinessRepository;
import com.example.fitfusion.Workout.Workout;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PersonController {

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/people")
    List<Person> getAllPeople() {
        return personRepository.findAll();
    }

    @GetMapping("/people/@/{username}")
    Person findPerson(@PathVariable String username) {
        return personRepository.findByUsername(username);
    }

    // all workouts of a user
    @GetMapping("/people/@/{username}/workouts")
    Set<String> getAllWorkouts(@PathVariable String username) {
        Person person = personRepository.findByUsername(username);
        List<String> workouts = personService.getWorkoutsOfPerson(person);
        Set<String> myWorkouts = new HashSet<>();
        for (String workout : workouts) {
            myWorkouts.add(workout);
        }

        return myWorkouts;

    }

    @PostMapping("/people/@/{username}/profilePicture")
    ResponseEntity<String> uploadProfilePic(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {
            Image image = new Image();
            image.setName(file.getOriginalFilename());
            image.setData(file.getBytes());
            imageRepository.save(image);
            Person tempPerson = personRepository.findByUsername(username);
            tempPerson.setProfilePic(image);
            personRepository.save(tempPerson);
            return ResponseEntity.status(HttpStatus.OK).body("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
        }
    }

    @GetMapping("/people/@/{username}/profilePicture")
    public ResponseEntity<byte[]> getProfilePic(@PathVariable String username) {
        Person tempPerson = personRepository.findByUsername(username);
        Image image = tempPerson.getProfilePic();
        if (image != null) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/people")
    String addPerson(@RequestBody Person person) {
        if (person == null) {
            return failure;
        }

        personRepository.save(person);
        return success;
    }

    @GetMapping("/people/@/{username}/friends")
    Set<Person> getAllFriends(@PathVariable String username) {
        Person person = personRepository.findByUsername(username);
        return person.getFriends();
    }

    @GetMapping("/people/search/{prefix}")
    List<Person> getMatchingPeople(@PathVariable String prefix) {
        int queryLength = prefix.length();
        return personRepository.GetByMatchingPrefix(prefix, queryLength);
    }

    @GetMapping("/people/@/{username}/search/{prefix}")
    List<Person> searchForFriends(@PathVariable String username, @PathVariable String prefix) {
        Person person = personRepository.findByUsername(username);
        int queryLength = prefix.length();
        List<Person> candidates = personRepository.GetByMatchingPrefix(prefix, queryLength);
        List<Person> newFriends = new LinkedList<Person>();
        for (Person candidate : candidates) {
            if (!candidate.getFriends().contains(person) && !candidate.getUsername().equals(username)) {
                newFriends.add(candidate);
            }
        }
        return newFriends;
    }

    @GetMapping("/people/@/{username}/recommend/{strictness}")
    List<Person> recommendFriends(@PathVariable String username, @PathVariable int strictness) {
        return personService.recommendFriends(username, strictness);
    }

    @PostMapping("/people/@/{username}/setHome/{gymName}")
    String addGymToPerson(@PathVariable String username, @PathVariable String gymName) {
        Person person = personRepository.findByUsername(username);
        Business business = businessRepository.findByBusinessName(gymName);
        if (person == null || business == null) {
            return "Home gym cannot be added to " + username;
        }

        person.setMyGym(business);
        personRepository.save(person);
        return "Home gym successfully added to " + username;
    }

    @GetMapping("/people/@/{username}/isBanned")
    boolean getIsBanned(@PathVariable String username) {
        Person person = personRepository.findByUsername(username);
        return person.isBanned();
    }

    @PostMapping("/people/@/{username}/ban")
    String banPerson(@PathVariable String username) {
        Person person = personRepository.findByUsername(username);
        person.setBanned(true);
        personRepository.save(person);
        return "banned " + username;
    }

    @PostMapping("/people/@/{username}/unban")
    String unbanPerson(@PathVariable String username) {
        Person person = personRepository.findByUsername(username);
        person.setBanned(false);
        personRepository.save(person);
        return "unbanned " + username;
    }

}

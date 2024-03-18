package com.example.fitfusion.Friendships;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;

@RestController
public class FriendRequestController {
    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    FriendRequestService friendRequestService;

    // @Autowired
    // PersonRepository personRepository;

    String success = "success";

    @GetMapping("/friendrequests")
    List<FriendRequest> getAllFriendRequest() {
        return friendRequestRepository.findAll();
    }

    @PostMapping("/people/@/{person1}/friend/{person2}")
    String tryFriendRequest(@PathVariable String person1, @PathVariable String person2) {
        //check if the two people exist here? 
        String message = friendRequestService.handleFriendRequest(person1, person2);

        return message;
    }

}

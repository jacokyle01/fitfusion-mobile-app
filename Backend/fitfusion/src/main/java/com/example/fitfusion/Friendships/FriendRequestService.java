package com.example.fitfusion.Friendships;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;

@Service
public class FriendRequestService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    //cases:
    //-they are friends already
    //there is already a pending request in same direction
    //there is pending request in opposite direction
    //there are no requests
    String handleFriendRequest(String person1, String person2) {
        Person sender = personRepository.findByUsername(person1);
        Person receiver = personRepository.findByUsername(person2);

        //does this request exist already?
        if (friendRequestRepository.findBySenderAndReceiver(sender, receiver) != null) {
            return person1 + " already has a pending friend request to " + person2 + "!";
        }

        //are they friends already?
        if (sender.getFriends().contains(receiver)) {
            return person1 + " is already friends with " + person2 + "!";
        }

        //does the receiver have a pending request from sender?
        //if so, they become friends
        if (friendRequestRepository.findBySenderAndReceiver(receiver, sender) != null) {
            sender.addFriend(receiver);
            receiver.addFriend(sender);
            personRepository.save(sender);
            personRepository.save(receiver);
            //remove dangling request from repository

            return person1 + " is now friends with " + person2 + "!";
        }
        //if we reach here, the only possible state is that this is a unique friend request
        FriendRequest friendRequest = new FriendRequest(sender, receiver);
        friendRequestRepository.save(friendRequest);
        return "Created new friend request from " + person1 + " to " + person2 + ".";
    }
}

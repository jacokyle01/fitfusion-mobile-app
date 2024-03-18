package com.example.fitfusion.Friendships;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fitfusion.Person.Person;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    FriendRequest findBySenderAndReceiver(Person sender, Person receiver);
}

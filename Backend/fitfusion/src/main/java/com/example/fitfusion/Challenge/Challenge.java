package com.example.fitfusion.Challenge;

import com.example.fitfusion.Person.Person;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Challenge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Status status;

    @ManyToOne
    private Person challenger;

    @ManyToOne
    private Person challengee;

    //record when the challenge was sent & accepted (when applicable)
    //TODO store time in more readable format  

    private long sentAt;
    private long acceptedAt;

    public Challenge(Person challenger, Person challengee) {
        this.status = Status.PENDING;
        this.sentAt = System.currentTimeMillis();
        this.acceptedAt = -1;
        this.challenger = challenger;
        this.challengee = challengee;
    }

    public Challenge() {

    }

    public Person getChallenger() {
        return challenger;
    }
    public void setChallenger(Person challenger) {
        this.challenger = challenger;
    }
    public Person getChallengee() {
        return challengee;
    }
    public void setChallengee(Person challengee) {
        this.challengee = challengee;
    }
    public long getsentAt() {
        return sentAt;
    }
    public void setsentAt(int sentAt) {
        this.sentAt = sentAt;
    }
    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public long getAcceptedAt() {
        return this.acceptedAt;
    }

    public void setAcceptedAt(long acceptedAt) {
        this.acceptedAt = acceptedAt;
    }


}

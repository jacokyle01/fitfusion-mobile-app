package com.example.fitfusion.Person;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.fitfusion.Image.Image;
import com.example.fitfusion.Post.PersonPost;
import com.example.fitfusion.Routine.Routine;
import com.example.fitfusion.WorkoutSession.WorkoutSession;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import com.example.fitfusion.Business.Business;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int points;

    @Column(unique = true)
    private String username;
    private String password;

    @JsonIgnore
    @OneToOne
    private Image profilePic;

    @ManyToOne
    @JoinColumn(name = "home_gym_id")
    private Business myGym;

    @OneToMany
    private List<Routine> routines;

    @JsonIgnore
    @OneToMany
    private List<WorkoutSession> workoutSessions;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Person> friends = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<PersonPost> personPosts;

    @JsonIgnore
    private boolean isBanned = false;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
        points = 0;
    }

    public Person() {

    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }

    public List<Routine> getRoutines() {
        return routines;
    }

    public void addRoutine(Routine routine) {
        routines.add(routine);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addFriend(Person person) {
        this.friends.add(person);
    }

    public Set<Person> getFriends() {
        return this.friends;
    }

    public List<PersonPost> getPersonPosts() {
        return personPosts;
    }

    public void setPersonPosts(List<PersonPost> personPosts) {
        this.personPosts = personPosts;
    }

    public void addPersonPost(PersonPost personPost) {
        personPosts.add(personPost);
    }

    public Business getMyGym() {
        return this.myGym;
    }

    public void setMyGym(Business newGym) {
        this.myGym = newGym;
    }

    public List<WorkoutSession> getWorkoutSessions() {
        return this.workoutSessions;
    }

    public void addWorkoutSession(WorkoutSession workoutSession) {
        workoutSessions.add(workoutSession);
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

}

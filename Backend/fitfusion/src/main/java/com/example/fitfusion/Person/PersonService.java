package com.example.fitfusion.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fitfusion.Workout.Workout;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    public List<Person> recommendFriends(String username, int strictness) {
        Person searcher = personRepository.findByUsername(username);
        List<Person> recommendations = new ArrayList<Person>();
        List<String> workoutsOfSearcher = getWorkoutsOfPerson(searcher);
        // hash the workouts for quick comparison
        Set<String> myWorkouts = new HashSet<>();
        for (String workout : workoutsOfSearcher) {
            myWorkouts.add(workout);
        }
        // generate list of people to search
        List<Person> allPeople = personRepository.findAll();
        List<Person> candidates = new ArrayList<Person>();
        for (Person person : allPeople) {

            // don't include friends and the searcher in the searching list
            if (person != searcher && !searcher.getFriends().contains(person)) {
                candidates.add(person);
            }
        }

        //iterate through candidates and note if they have enough matching workouts
        for (Person candidate : candidates) {
            List<String> candidateWorkouts = getWorkoutsOfPerson(candidate);
            //count matching workouts
            int count = 0;
            for (String workout : candidateWorkouts) {
                if (myWorkouts.contains(workout)) {
                    count++;
                }
            }
            if (count >= strictness) {
                recommendations.add(candidate);
            }
        }

        return recommendations;
    }

    public List<String> getWorkoutsOfPerson(Person person) {
        List<String> workouts = new ArrayList<String>();
        person.getRoutines().forEach((routine) -> {
            for (Workout workout : routine.getWorkouts()) {
                workouts.add(workout.getName());
            }
        });
        return workouts;
    }
}

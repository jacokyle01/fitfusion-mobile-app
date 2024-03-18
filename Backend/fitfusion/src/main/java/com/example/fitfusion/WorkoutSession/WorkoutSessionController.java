package com.example.fitfusion.WorkoutSession;

import java.util.List;

import org.hibernate.boot.registry.classloading.spi.ClassLoaderService.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;
import com.example.fitfusion.Workout.Workout;
import com.example.fitfusion.Workout.WorkoutRepository;


@RestController
@RequestMapping("/wsessions")
public class WorkoutSessionController {

    private String success = "{\"message\":\"success\"}";

    @Autowired
    WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    WorkoutSessionService workoutSessionService;

    @Autowired 
    WorkoutRepository workoutRepository;

    @GetMapping("/{username}")
    List<WorkoutSession> getWorkoutSessionsOfPerson(@PathVariable String username) {
        Person person = personRepository.findByUsername(username);
        return person.getWorkoutSessions();
    }

    //get `WorkoutSession` of a person on a date, if it exists
    @GetMapping("/{username}/{year}/{month}/{day}")
    WorkoutSession GetWorkoutSessionOnDate(@PathVariable String username, @PathVariable int year,
            @PathVariable int month, @PathVariable int day) {
                return workoutSessionService.getWorkoutSessionOnDate(username, year, month, day);
            }

    //instantiate a new `WorkoutSession` for a person 
    @PostMapping("/{username}/{year}/{month}/{day}/new")
    String addWorkoutSession(@PathVariable String username, @PathVariable int year,
            @PathVariable int month, @PathVariable int day) {
                return workoutSessionService.addWorkoutSessionToPerson(username, year, month, day);
    }

    //add a workout to a person's workout session 
    @PostMapping("/{username}/{year}/{month}/{day}")
    String addWorkoutToSession(@RequestBody Workout workout, @PathVariable String username, @PathVariable int year,
            @PathVariable int month, @PathVariable int day) {
                return workoutSessionService.addWorkoutToSession(username, year, month, day, workout);
    }

}

package com.example.fitfusion.WorkoutSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;
import com.example.fitfusion.Workout.Workout;
import com.example.fitfusion.Workout.WorkoutRepository;

@Service
public class WorkoutSessionService {
    @Autowired
    PersonRepository personRepository;

    @Autowired
    WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    WorkoutRepository workoutRepository;

    public WorkoutSession getWorkoutSessionOnDate(String username, int year, int month, int day) {
        Person person = personRepository.findByUsername(username);
        WorkoutSession date = new WorkoutSession(year, month, day);
        List<WorkoutSession> myWorkoutSessions = person.getWorkoutSessions();
        for (WorkoutSession workoutSession : myWorkoutSessions) {
            if (workoutSession.equals(date)) {
                return workoutSession;
            }
        }

        return null;
    }

    public String addWorkoutSessionToPerson(String username, int year, int month, int day) {
        WorkoutSession workoutSession = new WorkoutSession(year, month, day);
        // check if there already exists a workout session on this date and respond
        // accordingly
        if (getWorkoutSessionOnDate(username, year, month, day) != null) {
            return username + " already has a workout session on " + month + "/" + day + "/" + year;
        }
        workoutSessionRepository.save(workoutSession);
        Person person = personRepository.findByUsername(username);
        person.addWorkoutSession(workoutSession);
        personRepository.save(person);
        return "success";
    }

    public String addWorkoutToSession(String username, int year, int month, int day, Workout workout) {
        WorkoutSession workoutSession = getWorkoutSessionOnDate(username, year, month, day);
        if (workoutSession == null) {
            return username + " doesn't have a workout session on " + month + "/" + day + "/" + year;
        }
        if (workout == null) {
            return "failure";
        }

        workoutRepository.save(workout);
        workoutSession.addWorkout(workout);
        workoutSessionRepository.save(workoutSession);
        return "Added workout to " + username + " on " + month + "/" + day + "/" + year;

    }

}

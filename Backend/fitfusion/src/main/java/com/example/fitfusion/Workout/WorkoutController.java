package com.example.fitfusion.Workout;

import com.example.fitfusion.Routine.Routine;
import com.example.fitfusion.Routine.RoutineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WorkoutController {

    @Autowired
    WorkoutRepository workoutRepository;

    @Autowired
    RoutineRepository routineRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping(path = "routine/{id}/workout")
    String createRoutineWorkout(@PathVariable int id, @RequestBody Workout workout)
    {

        if(workout == null)
            return failure;

        Routine tempRoutine = routineRepository.findById(id);

        if(tempRoutine == null)
            return "user not found";

        tempRoutine.addWorkout(workout);
        workoutRepository.save(workout);
        routineRepository.save(tempRoutine);

        return success;

    }

    @GetMapping(path = "routine/{id}/workout")
    List<Workout> getRoutineWorkouts(@PathVariable int id)
    {
        Routine tempRoutine = routineRepository.findById(id);

        if(tempRoutine == null)
            return null;

        return tempRoutine.getWorkouts();

    }

}

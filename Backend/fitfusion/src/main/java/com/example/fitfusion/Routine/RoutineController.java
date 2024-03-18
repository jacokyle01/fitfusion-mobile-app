package com.example.fitfusion.Routine;

import com.example.fitfusion.Person.Person;
import com.example.fitfusion.Person.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoutineController {

    @Autowired
    RoutineRepository routineRepository;

    @Autowired
    PersonRepository personRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @PostMapping(path = "/user/{username}/routines")
    String createPersonRoutine(@PathVariable String username, @RequestBody Routine routine)
    {
        if(routine == null)
            return failure;

        Person tempPerson = personRepository.findByUsername(username);

        if(tempPerson == null)
            return "user not found";

        tempPerson.addRoutine(routine);

        routineRepository.save(routine);
        personRepository.save(tempPerson);

        return success;

    }



    @GetMapping(path = "user/{username}/routines")
    List<Routine> getPersonRoutines(@PathVariable String username)
    {
        Person tempPerson = personRepository.findByUsername(username);

        if(tempPerson == null)
            return null;

        return tempPerson.getRoutines();

    }

    @GetMapping(path = "/routines")
    List<Routine> getRoutines() {
        return routineRepository.findAll();
    }

}

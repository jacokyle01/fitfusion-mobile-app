package com.example.fitfusion.WorkoutSession;

import java.util.List;

import com.example.fitfusion.Workout.Workout;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class WorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int year;
    private int month;
    private int day;

    @OneToMany
    private List<Workout> workouts;

    public WorkoutSession(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public WorkoutSession() {

    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkoutSession that = (WorkoutSession) o;
        return (this.day == that.day && this.month == that.month && this.year == that.year);
    }

}

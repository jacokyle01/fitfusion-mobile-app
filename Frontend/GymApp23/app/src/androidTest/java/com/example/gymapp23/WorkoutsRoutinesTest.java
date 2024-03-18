package com.example.gymapp23;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@LargeTest
public class WorkoutsRoutinesTest {

    private ActivityScenario<PostRoutines> scenario;

    private static final int SIMULATED_DELAY_MS = 500;
    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Before
    public void setUp() {
        //username: test and password: test is a real user
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("ideal2"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("ideal2"));
//
//        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());
//
        //Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //Check if the new activity (HomePage) is displayed
        Espresso.onView(withId(R.id.UserProfileFAB)).check(matches(isDisplayed()));

        //Navigate to Post Routines
        Espresso.onView(withId(R.id.workoutsFAB)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.workoutsPage)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeBTN(){
        onView(withId(R.id.workoutsToHomeBtn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.helloUserTxt)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testWorkoutPostBTN(){
        onView(withId(R.id.reps)).perform(ViewActions.typeText("10"));
        onView(withId(R.id.sets)).perform(ViewActions.typeText("2"));
        onView(withId(R.id.weight)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.switchWorkout)).perform(click());
//        Espresso.onView(withId(R.id.userMsgEtx_GC)).perform(ViewActions.typeText("Automated Test Message"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.trackWorkoutBtn)).perform(click());
//        Espresso.onView(ViewMatchers.withId(R.id.routinePage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testRoutineBTN(){
        onView(withId(R.id.routine)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.routinePage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testRoutinePostBTN(){
        onView(withId(R.id.routine)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.customName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        onView(withId(R.id.addRoutine)).perform(click());
        onView(withId(R.id.customName)).perform(ViewActions.typeText("TestCaseRoutine"));
        onView(withId(R.id.workout)).perform(ViewActions.typeText("rdl"));
        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.weight)).perform(ViewActions.typeText("100"));
        onView(withId(R.id.trackRoutineBtn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
//        Espresso.onView(withId(R.id.userMsgEtx_GC)).perform(ViewActions.typeText("Automated Test Message"));

        onView(withId(R.id.routineToWorkout)).perform(click());
//        Espresso.onView(ViewMatchers.withId(R.id.routinePage)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }


}

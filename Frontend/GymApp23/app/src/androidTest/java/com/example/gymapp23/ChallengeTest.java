package com.example.gymapp23;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChallengeTest {
    private static final int SIMULATED_DELAY_MS = 500;
    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
        new ActivityScenarioRule<>(LoginPage.class);

    @Before
    public void setUp() {
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
        Espresso.onView(withId(R.id.challenge)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.ChallengePage)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeBTN() {
        onView(withId(R.id.homebtn)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.helloUserTxt)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testEncourage() {
        onView(withId(R.id.encourageChallenge)).perform(ViewActions.click());
    }

    @Test
    public void testTrashTalk() {
        onView(withId(R.id.trashtalkChallenge)).perform(ViewActions.click());
    }

    @Test
    public void testNewChallenge() {
        Espresso.onView(ViewMatchers.withId(R.id.newChallengeUser))
                .perform(ViewActions.typeText("tom"));
        onView(withId(R.id.createChallenge)).perform(ViewActions.click());
    }

    @Test
    public void testCards() {
        //onView(withId(R.id.followBTN)).perform(ViewActions.click());
        //onView(withId(R.id.btn1)).perform(ViewActions.click());
    }
}
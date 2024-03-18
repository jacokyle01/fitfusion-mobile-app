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
public class ProfilePageTest {
    private ActivityScenario<ProfilePage> scenario;

    private static final int SIMULATED_DELAY_MS = 500;
    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Before
    public void setUp() {
        //username: test and password: test is a real user
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("ideal"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("ideal"));
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
        Espresso.onView(withId(R.id.UserProfileFAB)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.PFP)).check(matches(isDisplayed()));
    }
    @Test
    public void testHomeBTN(){
        onView(withId(R.id.homePage)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.feedTxt)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testFollowBtn() {
        Espresso.onView(withId(R.id.userFollowBtn)).check(matches(isDisplayed()));
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.userFollowBtn)).perform(ViewActions.click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.profileBtn)).check(matches(isDisplayed()));
        //Espresso.onView(ViewMatchers.withId(R.id.container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testRoutineBTN(){
        Espresso.onView(ViewMatchers.withId(R.id.showRoutines)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.showRoutines)).perform(click());
    }

    @Test
    public void testPostsBTN(){
        Espresso.onView(withId(R.id.showPosts)).check(matches(isDisplayed()));
        onView(withId(R.id.showPosts)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        Espresso.onView(withId(R.id.likeBTN)).check(matches(isDisplayed()));
        onView(withId(R.id.likeBTN)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        onView(withId(R.id.likeBTN)).perform(click());

    }
//
    @Test
    public void testPFP(){
        Espresso.onView(withId(R.id.PFP)).check(matches(isDisplayed()));
        onView(withId(R.id.PFP)).perform(click());
        Espresso.onView(withId(R.id.PFSelectPage)).check(matches(isDisplayed()));
        onView(withId(R.id.picture1)).perform(click());
        onView(withId(R.id.picture2)).perform(click());
        onView(withId(R.id.picture3)).perform(click());
        onView(withId(R.id.profileSelectBackBtn)).perform(click());
        Espresso.onView(withId(R.id.PFP)).check(matches(isDisplayed()));
    }

    @Test
    public void testNewPost(){
        Espresso.onView(withId(R.id.userPostBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.userPostBtn)).perform(click());
    }

    //Actual functionality of chats is tested elseware: Does not need to be tested here
    @Test
    public void testGymChatBTN(){
        onView(withId(R.id.myGymChat_userProfilePage)).perform(click());
        Espresso.onView(withId(R.id.myGymTxt)).check(matches(isDisplayed()));
    }

        @Test
    public void testPostMyGym() {
        Espresso.onView(withId(R.id.postMyGymEtx))
                .perform(ViewActions.typeText("StateGym"));
        onView(withId(R.id.postMyGymBtn)).perform(click());
    }
}
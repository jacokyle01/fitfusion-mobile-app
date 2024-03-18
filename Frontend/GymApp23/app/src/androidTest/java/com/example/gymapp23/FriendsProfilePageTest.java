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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FriendsProfilePageTest {
    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);
    @Before
    public void setUp() {
        //username: test and password: test is a real user
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("w"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("w"));

//        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());
//
        //Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        //Check if the new activity (HomePage) is displayed
        Espresso.onView(withId(R.id.searchFAB)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchFAB)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.searchName))
                .perform(ViewActions.typeText("ro"));
        onView(withId(R.id.searchBTN)).perform(click());

//        onView(withId(R.id.container)).perform(click());
        onView(withId(R.id.followBTN)).perform(click());
        Espresso.onView(withId(R.id.userName)).check(matches(isDisplayed()));
        //Navigate to Post Routines
//        Espresso.onView(withId(R.id.UserProfileFAB)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.PFP)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeBTN (){
        Espresso.onView(withId(R.id.homePage)).check(matches(isDisplayed()));
        onView(withId(R.id.homePage)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.searchFAB)).check(matches(isDisplayed()));
    }
    @Test
    public void testRoutine (){
        Espresso.onView(withId(R.id.showRoutines)).check(matches(isDisplayed()));
        onView(withId(R.id.showRoutines)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }
    @Test
    public void testPostBTN (){
        Espresso.onView(withId(R.id.showPosts)).check(matches(isDisplayed()));
        onView(withId(R.id.showPosts)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.messageTextView)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.titleTextView)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.likeBTN)).check(matches(isDisplayed()));
        onView(withId(R.id.likeBTN)).perform(ViewActions.click());
        onView(withId(R.id.likeBTN)).perform(ViewActions.click());
    }
    @Test
    public void testFriend (){
        Espresso.onView(withId(R.id.userFollowBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.userFollowBtn)).perform(ViewActions.click());
    }
}


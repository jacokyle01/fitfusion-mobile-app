package com.example.gymapp23;

import static org.junit.Assert.assertEquals;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.assertion.ViewAssertions;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;

import org.junit.Before;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class SearchPageTest {

//    private ActivityScenario<SearchPage> scenario;
    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Before
    public void setUp() {
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("w"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("w"));

        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        //Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        //Check if the new activity (HomePage) is displayed
        Espresso.onView(withId(R.id.UserProfileFAB)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchFAB)).perform(ViewActions.click());
    }

    @Test
    public void testNoResults() {
        Espresso.onView(ViewMatchers.withId(R.id.searchName))
                .perform(ViewActions.typeText("tasdfgadsdaf"));
        Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

    @Test
    public void testViewButton() {
        Espresso.onView(ViewMatchers.withId(R.id.searchName))
                .perform(ViewActions.typeText("roo"));
        onView(withId(R.id.searchBTN)).perform(click());

        onView(withId(R.id.container)).perform(click());
        onView(withId(R.id.followBTN)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.userName)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testHomeBTN(){
        onView(withId(R.id.homePage)).perform(click());
        Espresso.onView(ViewMatchers.withId(R.id.helloUserTxt)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}

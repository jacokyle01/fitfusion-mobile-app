package com.example.gymapp23;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

@LargeTest
public class UserPostTest
{
    private static final int SIMULATED_DELAY_MS = 500;


    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Test
    public void userPostTest() {
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("sully"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("pw"));

//        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.UserProfileFAB)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.userPostBtn)).perform(ViewActions.click());

//        Intent intent = new Intent();
//        intent.putExtra("username", "testingUserPosts");
//
//        activityRule.getScenario().onActivity(activity -> {
//            activity.setIntent(intent);
//        });

        Espresso.onView(withId(R.id.title)).perform(ViewActions.typeText("Auto Title"));
        Espresso.onView(withId(R.id.message)).perform(ViewActions.typeText("Auto Message"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.UserPostBtn)).perform(ViewActions.click());
        Espresso.closeSoftKeyboard();

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //Espresso.onView(withId(R.id.showRoutines)).check(matches(isDisplayed()));




    }
}


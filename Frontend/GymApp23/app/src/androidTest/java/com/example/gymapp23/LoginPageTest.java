package com.example.gymapp23;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;


import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import java.util.Random;

//import androidx.test.espresso.intent.rule.IntentsTestRule;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.IdlingRegistry;
//import androidx.test.espresso.IdlingResource;
//import androidx.test.rule.ActivityTestRule;




@LargeTest
public class LoginPageTest {

    private static final int SIMULATED_DELAY_MS = 1000;

//    @Rule
//    public IntentsTestRule<LoginPage> intentsTestRule = new IntentsTestRule<>(LoginPage.class);

    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Test
    public void loginSuccess() {
        // Type in the correct username and password
        //username: test and password: test is a real user
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("test"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("test"));

        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

         //Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //Check if the new activity (HomePage) is displayed
        Espresso.onView(withId(R.id.UserProfileFAB)).check(matches(isDisplayed()));
    }

    @Test
    public void loginFailure() {
        // Type in incorrect username and password
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("wrong"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("wrong"));

        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        // Check if the error message is displayed (change this based on your actual error message)
        Espresso.onView(withId(R.id.forgotpassword)).check(matches(isDisplayed()));
    }

    @Test
    public void toHomeBtnTest() {
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        // Type in incorrect username and password
        // Click on the login button
        Espresso.onView(withId(R.id.homebtn)).perform(ViewActions.click());

        // Check if the error message is displayed (change this based on your actual error message)
    }

    @Test
    public void userSignupNAVTest() {

        // Type in incorrect username and password
        // Click on the login button
        Espresso.onView(withId(R.id.signupbtn)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.userSignupBtn)).perform(ViewActions.click());

        Random random = new Random();

        int rand = random.nextInt();
        Espresso.onView(withId(R.id.usernameETxt)).perform(ViewActions.typeText("auto" + rand));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.passwordETxt)).perform(ViewActions.typeText("auto" + rand));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.homeGymETxt)).perform(ViewActions.typeText("auto" + rand));
        Espresso.closeSoftKeyboard();


        // Click on the create account button
        Espresso.onView(withId(R.id.createAccountBtn)).perform(ViewActions.click());



        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }


        Espresso.onView(withId(R.id.signupToLoginPageBtn)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.forgotpassword)).check(matches(isDisplayed()));


        // Check if the error message is displayed (change this based on your actual error message)
    }

    @Test
    public void homePageLikeTest() {
        // Type in the correct username and password
        //username: test and password: test is a real user
        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("homePageLike"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("homePageLike"));

        // Click on the login button
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        //Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //Check if the new activity (HomePage) is displayed
        Espresso.onView(withId(R.id.UserProfileFAB)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.likeBTN)).check(matches(isDisplayed()));


        Espresso.onView(withId(R.id.likeBTN)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        Espresso.onView(withId(R.id.likeBTN)).perform(ViewActions.click());


    }




}
package com.example.gymapp23;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//import androidx.test.espresso.intent.rule.IntentsTestRule;
//import androidx.test.espresso.intent.Intents;
//import androidx.test.espresso.IdlingRegistry;
//import androidx.test.espresso.IdlingResource;
//import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;




@LargeTest
public class WebSocketTests {

    private static final int SIMULATED_DELAY_MS = 1000;


    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

//    @Rule
//    public IntentsTestRule<LoginPage> intentsTestRule = new IntentsTestRule<>(LoginPage.class);

//    @Before
//    public void setUp() {
//        Intents.init();
//        // Additional setup if needed
//    }
//
//    @After
//    public void tearDown() {
//        Intents.release();
//        // Additional teardown if needed
//    }


    @Test
    public void WebSocketTest() {
        //Intents.init();
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

        //intended(IntentMatchers.hasComponent(HomePage.class.getName()));
        Espresso.onView(withId(R.id.UserProfileFAB)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.UserProfileFAB)).perform(ViewActions.click());

        //check profile page is up
        Espresso.onView(withId(R.id.myGymChat_userProfilePage)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.myGymChat_userProfilePage)).perform(ViewActions.click());

        //check if you are on group chat page
        Espresso.onView(withId(R.id.chattingWithGroupTxt)).check(matches(isDisplayed()));


        Espresso.onView(withId(R.id.userMsgEtx_GC)).perform(ViewActions.typeText("Automated Test Message"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.sendGCBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.sendGCBtn)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        Espresso.onView(withId(R.id.gcToHomeBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.gcToHomeBtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.helloUserTxt)).check(matches(isDisplayed()));


        Espresso.onView(withId(R.id.messagesFAB)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.messagesFAB)).perform(ViewActions.click());

        //check openDM page is up
        Espresso.onView(withId(R.id.otherUserETxt)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.otherUserETxt)).perform(ViewActions.typeText("test2"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.openDMBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.openDMBtn)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
            Thread.sleep(SIMULATED_DELAY_MS);
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //check if you are on group chat page
        Espresso.onView(withId(R.id.userMsgEtx)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.userMsgEtx)).perform(ViewActions.typeText("Automated Test Message"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.sendDMBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.sendDMBtn)).perform(ViewActions.click());



        try {
            Thread.sleep(SIMULATED_DELAY_MS);
            Thread.sleep(SIMULATED_DELAY_MS);
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        Espresso.onView(withId(R.id.dmToHomeBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.dmToHomeBtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.helloUserTxt)).check(matches(isDisplayed()));

        //Intents.release();
    }
//    @Test
//    public void testFollowBtn() {
////        Espresso.onView(ViewMatchers.withId(R.id.userFollowBtn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
////        onView(withId(R.id.userFollowBtn)).perform(click());
////        try {
////            Thread.sleep(SIMULATED_DELAY_MS);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        Espresso.onView(ViewMatchers.withId(R.id.profileBtn)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
////        //Espresso.onView(ViewMatchers.withId(R.id.container)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
//
//        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("test"));
//        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("test"));
//
//        // Click on the login button
//        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());
//
//        //Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }
//
//        //Check if the new activity (HomePage) is displayed
//
//        //intended(IntentMatchers.hasComponent(HomePage.class.getName()));
//        Espresso.onView(withId(R.id.UserProfileFAB)).check(matches(isDisplayed()));
//        Espresso.onView(withId(R.id.UserProfileFAB)).perform(ViewActions.click());
//
//        //check profile page is up
//        Espresso.onView(withId(R.id.userFollowBtn)).check(matches(isDisplayed()));
//        Espresso.onView(withId(R.id.userFollowBtn)).perform(ViewActions.click());
//
//        //check if you are on group chat page
//        Espresso.onView(withId(R.id.profileBtn)).check(matches(isDisplayed()));
//    }

}
package com.example.gymapp23;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class BusinessLoginTests {
    private static final long SIMULATED_DELAY_MS = 500;


    @Rule
    public ActivityScenarioRule<BusinessLogin> activityRule =
            new ActivityScenarioRule<>(BusinessLogin.class);

    @Test
    public void testBusinessLoginSuccess() {
        // Type in the business name and password
        Espresso.onView(withId(R.id.businessNameLogin)).perform(ViewActions.typeText("demo4"));
        Espresso.onView(withId(R.id.BusinessPassword)).perform(ViewActions.typeText("demo4"));

        // Click on the login button
        Espresso.onView(withId(R.id.businessLogin_Btn)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //Check if the new activity businessProfile is displayed
        //intended(IntentMatchers.hasComponent(BusinessProfilePage.class.getName()));
        Espresso.onView(withId(R.id.businessInfoTxt)).check(matches(isDisplayed()));


    }
    @Test
    public void testBusinessLoginNavigation() {
        // Type in the business name and password
        Espresso.onView(withId(R.id.businessNameLogin)).perform(ViewActions.typeText("demo4"));
        Espresso.onView(withId(R.id.BusinessPassword)).perform(ViewActions.typeText("demo4"));

        // Click on the login button
        Espresso.onView(withId(R.id.businessLogin_Btn)).perform(ViewActions.click());


        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //Check if the new activity businessProfile is displayed
        //intended(IntentMatchers.hasComponent(BusinessProfilePage.class.getName()));
        Espresso.onView(withId(R.id.businessInfoTxt)).check(matches(isDisplayed()));

        //click post btn
        Espresso.onView(withId(R.id.postEventButton)).perform(ViewActions.click());

        //check postevent class is displayed
        //intended(IntentMatchers.hasComponent(PostEvent.class.getName()));
        Espresso.onView(withId(R.id.postEventBtn)).check(matches(isDisplayed()));
        //post
        // Type in the title and message
        Espresso.onView(withId(R.id.title)).perform(ViewActions.typeText("Event Title"));
        Espresso.onView(withId(R.id.message)).perform(ViewActions.typeText("Event Message"));
        Espresso.closeSoftKeyboard();

        // Click on the post event button
        Espresso.onView(withId(R.id.postEventBtn)).perform(ViewActions.click());
    }


    @Test
    public void testBusinessLoginFailure() {
        // Type in incorrect business name and password
        Espresso.onView(withId(R.id.businessNameLogin)).perform(ViewActions.typeText("wrong_business"));
        Espresso.onView(withId(R.id.BusinessPassword)).perform(ViewActions.typeText("wrong_password"));

        // Click on the login button
        Espresso.onView(withId(R.id.businessLogin_Btn)).perform(ViewActions.click());

        //Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //Check if the new activity (HomePage) is displayed
        //intended(IntentMatchers.hasComponent(BusinessLogin.class.getName()));
        Espresso.onView(withId(R.id.businessLogin_Btn)).check(matches(isDisplayed()));
    }


}

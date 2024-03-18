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

public class BusinessProfileTests {
    private static final long SIMULATED_DELAY_MS = 500;



    @Rule
    public ActivityScenarioRule<BusinessProfilePage> activityRule =
            new ActivityScenarioRule<>(BusinessProfilePage.class);


    @Test
    public void testBusinessProfilePageUI() {
        // Check if the business name is displayed (adjust based on your actual UI)


        //Check if the new activity (HomePage) is displayed
        //intended(IntentMatchers.hasComponent(BusinessProfilePage.class.getName()));
        Espresso.onView(withId(R.id.businessInfoTxt)).check(matches(isDisplayed()));
    }

}

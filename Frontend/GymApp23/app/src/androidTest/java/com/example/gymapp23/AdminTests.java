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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;



public class AdminTests {
    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Test
    public void testBlacklistFunctionality() {

        Espresso.onView(withId(R.id.username)).perform(ViewActions.typeText("admin"));
        Espresso.onView(withId(R.id.passwrd)).perform(ViewActions.typeText("admin"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.loginbtn)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        // Type in the group name and search name
        Espresso.onView(withId(R.id.searchBTN)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchName)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchName)).perform(ViewActions.typeText("test2"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        // Now, interact with the ban button
        //Espresso.onView(withText("test2")).perform(ViewActions.click()); // Assuming "test" is the name you want to interact with
        Espresso.onView(withId(R.id.btn1)).perform(ViewActions.click());
        //Espresso.onView(allOf(withId(R.id.btn1), withText("BAN"))).perform(ViewActions.click());
        // Optionally, add some delay to allow time for the action to be performed
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Similarly, interact with the unban button
        //Espresso.onView(withText("test2")).perform(ViewActions.click()); // Assuming "test" is the name you want to interact with
        Espresso.onView(withId(R.id.btn2)).perform(ViewActions.click());
        //Espresso.onView(allOf(withId(R.id.btn2), withText("UNBAN"))).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }



        //type more stuff to get no results
        Espresso.onView(withId(R.id.searchName)).perform(ViewActions.typeText("lagdfkjak"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //next admin screen
        Espresso.onView(withId(R.id.businessDeleteNAVBtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.searchBTN)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.groupchatBanNAVBtn)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        Espresso.onView(withId(R.id.searchBTN)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchName)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.searchName)).perform(ViewActions.typeText("test2"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.groupNameETxt)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.groupNameETxt)).perform(ViewActions.typeText("demo4"));
        Espresso.closeSoftKeyboard();


        Espresso.onView(withId(R.id.viewBlacklistBtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //blacklists the user
        Espresso.onView(withId(R.id.btn1)).perform(ViewActions.click());
        //Espresso.onView(allOf(withId(R.id.btn1), withText("BAN"))).perform(ViewActions.click());
        // Optionally, add some delay to allow time for the action to be performed
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Similarly, interact with the unban button
        //Espresso.onView(withText("test2")).perform(ViewActions.click()); // Assuming "test" is the name you want to interact with
        Espresso.onView(withId(R.id.btn2)).perform(ViewActions.click());
        //Espresso.onView(allOf(withId(R.id.btn2), withText("UNBAN"))).perform(ViewActions.click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        //add more to search so there is no results
        Espresso.onView(withId(R.id.searchName)).perform(ViewActions.typeText("akisfgaskjf"));
        Espresso.closeSoftKeyboard();
        Espresso.onView(withId(R.id.groupNameETxt)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.groupNameETxt)).perform(ViewActions.typeText("asdfkuydfgak"));
        Espresso.closeSoftKeyboard();


        Espresso.onView(withId(R.id.viewBlacklistBtn)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());


        // Click on the search button
        //Espresso.onView(withId(R.id.searchBTN)).perform(ViewActions.click());

        // Click on the view blacklist button
        //Espresso.onView(withId(R.id.viewBlacklistBtn)).perform(ViewActions.click());

        // Check if the blacklist is displayed (you may need to adjust this based on your actual UI)
        //Espresso.onView(withText("test")).check(matches(isDisplayed()));
    }

}

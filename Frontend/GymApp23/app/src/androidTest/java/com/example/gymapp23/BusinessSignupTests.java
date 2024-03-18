package com.example.gymapp23;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
//import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class BusinessSignupTests {
    private static final long SIMULATED_DELAY_MS = 500;
    private String additionalStrToName = "4";

    @Rule
    public ActivityScenarioRule<LoginPage> activityRule =
            new ActivityScenarioRule<>(LoginPage.class);

    @Before
    public void setUp() {
        Espresso.onView(withId(R.id.signin)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.username)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.passwrd)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.loginbtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.forgotpassword)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.homebtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signupbtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.businessLoginBtn)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.signupbtn)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.userSignupBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.signupBusinessBtn)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.signupBusinessBtn)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.businessSignupTxt)).check(matches(isDisplayed()));
    }

    @Test
    public void testBusinessSignupSuccess() {
        Espresso.onView(withId(R.id.businessNameETxt)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.businessNameETxt)).perform(ViewActions.typeText("New_User1"+additionalStrToName));
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.passwordBusinessETxt)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.passwordBusinessETxt)).perform(ViewActions.typeText("new_password"));
        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.businessAddressETxt)).check(matches(isDisplayed()));
//        Espresso.onView(withId(R.id.businessAddressETxt)).perform(ViewActions.typeText("1234 maple st"));
//        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.businessCityETxt)).check(matches(isDisplayed()));
//        Espresso.onView(withId(R.id.businessCityETxt)).perform(ViewActions.typeText("ames"));
//        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.businessStateETxt)).check(matches(isDisplayed()));
//        Espresso.onView(withId(R.id.businessStateETxt)).perform(ViewActions.typeText("iowa"));
//        Espresso.closeSoftKeyboard();

        Espresso.onView(withId(R.id.createBusinessAccountBtn)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.businessSignupToLoginPageBtn)).check(matches(isDisplayed()));
        // Type in the business details
//        Espresso.onView(withId(R.id.businessNameETxt)).perform(ViewActions.typeText("new_business24"));
//        Espresso.onView(withId(R.id.passwordBusinessETxt)).perform(ViewActions.typeText("new_password"));
//        Espresso.closeSoftKeyboard();
//        Espresso.onView(withId(R.id.businessCityETxt)).perform(ViewActions.typeText("city"));
//        Espresso.closeSoftKeyboard();
//        Espresso.onView(withId(R.id.businessStateETxt)).perform(ViewActions.typeText("zipcode"));
//        Espresso.closeSoftKeyboard();
//        Espresso.onView(withId(R.id.businessAddressETxt)).perform(ViewActions.typeText("address"));
//        Espresso.closeSoftKeyboard();

        // Click on the create business button
        Espresso.onView(withId(R.id.createBusinessAccountBtn)).perform(ViewActions.click());


        // Check if the BusinessLogin activity is displayed (adjust based on your actual UI)
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {
//        }

//        Espresso.onView(withId(R.id.businessSignupToLoginPageBtn)).perform(ViewActions.click());
        //Check if the new activity (HomePage) is displayed
        //intended(IntentMatchers.hasComponent(BusinessLogin.class.getName()));
//        Espresso.onView(withId(R.id.createBusinessAccountBtn)).check(matches(isDisplayed()));

    }

    @Test
    public void testBusinessSignupAccountAlreadyCreated() {
        // Type in the business details
        Espresso.onView(withId(R.id.businessNameETxt)).perform(ViewActions.typeText("sonoco"));
        Espresso.closeSoftKeyboard();

        // Click on the create business button
        Espresso.onView(withId(R.id.createBusinessAccountBtn)).perform(ViewActions.click());


        // Check if the BusinessLogin activity is displayed (adjust based on your actual UI)
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }
        //Check if the new activity (Business Signup) is displayed
        //intended(IntentMatchers.hasComponent(BusinessSignup.class.getName()));
        Espresso.onView(withId(R.id.createBusinessAccountBtn)).check(matches(isDisplayed()));
    }

}

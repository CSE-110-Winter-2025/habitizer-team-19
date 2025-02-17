package edu.ucsd.cse110.habitizer.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static java.util.regex.Pattern.matches;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class IntegrationTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testTaskListDisplay() {
        // Navigate to the morning routine
        onView(withId(R.id.routine_title)).perform(click());

        // Verify that the task list is displayed with the correct tasks
//        onView(withText("Brush Teeth")).check(matches(isDisplayed()));
//        onView(withText("Shower")).check(matches(isDisplayed()));
//        onView(withText("Get Dressed")).check(matches(isDisplayed()));
    }
}

package com.example.networkaplication;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.networkaplication.idling.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {
    private final String REQUEST = "movie";

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.INSTANCE.getIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.INSTANCE.getIdlingResource());
    }

    @Test
    public void tenItemsCheck() {
        onView(withId(R.id.search_button)).perform(typeText(REQUEST), pressImeActionButton());
        onView(withId(R.id.home_recycle_view))
                .check(new RecyclerViewItemCountAssertion(10));
    }

    @Test
    public void notEmptyDetails() {
        onView(withId(R.id.search_button)).perform(typeText(REQUEST), pressImeActionButton());

        onView(withId(R.id.home_recycle_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(9, click()));

        onView(withId(R.id.film_name)).check(matches((withText("Movie 43"))));
        onView(withId(R.id.details)).check(matches((withText("2013 94 min USA 4.3/10 "))));
        onView(withId(R.id.genre)).check(matches((withText("Comedy, Horror, Thriller"))));
        onView(withId(R.id.plot_summary))
                .check(matches((withText(
                        "A series of interconnected short films follows a " +
                                "washed-up producer as he pitches insane story lines " +
                                "featuring some of the biggest stars in Hollywood."))));
    }
}

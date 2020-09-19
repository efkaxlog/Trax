package com.bajera.xlog.trax;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.bajera.xlog.trax.activities.ItemActivity.ItemActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class RecordActivityTest {
    private Context context = ApplicationProvider.getApplicationContext();

    @Rule
    public ActivityTestRule<ItemActivity> activityRule
            = new ActivityTestRule<>(ItemActivity.class);

//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        assertEquals("com.bajera.xlog.trax", context.getPackageName());
//    }
//
//    @Test
//    public void chartIsVisible(){
//        onView(ViewMatchers.withId(R.id.barChart)).check(matches(isDisplayed()));
//    }
//
//    @Test
//    public void snackbarItemAddSuccessTextMatches() {
//        activityRule.getActivity().showRecordAddSuccess();
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText(R.string.record_add_success)));
//    }
//
//    @Test
//    public void snackbarItemAddFailTextMatches() {
//        activityRule.getActivity().showRecordAddFail();
//        onView(withId(com.google.android.material.R.id.snackbar_text))
//                .check(matches(withText(R.string.record_exists)));
//    }
}

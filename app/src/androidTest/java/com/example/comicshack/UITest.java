package com.example.comicshack;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.core.AllOf.allOf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.comicshack.model.ComicLibrary;
import com.comicshack.model.entities.Comic;
import com.comicshack.view.MainActivity;
import com.comicshack.view.ReadComicActivity;
import com.comicshack.view.ui.dashboard.DashboardFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private static final int[] MENU_CONTENT_ITEM_IDS = {
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
    };
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.comicshack", appContext.getPackageName());
    }
    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
    @Test
    public void addsComicToDB() {
        onView(withId(R.id.editTextName)).perform(typeText("UI Auto Test"));
        onView(withId(R.id.editTextAuthor)).perform(typeText("UITest"));
        onView(withId(R.id.editTextSeries)).perform(typeText("1"));
        onView(withId(R.id.editTextYear)).perform(typeText(String.valueOf("2022")));
        onView(withId(R.id.chooseFile)).perform(click());

        intended(allOf(
                hasAction(Intent.ACTION_GET_CONTENT),
                hasData("Dung")));

        onView(withId(R.id.addComicToDb)).perform(click());
        onView(withText("Comic Saved!")).check(matches(isDisplayed()));
    }

    @Rule
    public ActivityTestRule<ReadComicActivity> activityTestRule2 =
            new ActivityTestRule<ReadComicActivity>(ReadComicActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
            Intent intent = new Intent(context, ReadComicActivity.class);
            List<Comic> comicList = ComicLibrary.getLibrary();
            comicList.get(intent.getIntExtra("index", 0));
            return intent;
        }

    };
    @Test
    public void viewComic() {
        onView(withId(R.id.photo_view)).check(matches(isDisplayed()));
    }

    @Test
    public void readComic() {
        onData(withId(R.id.viewPagerImageSlider)).atPosition(0).perform(click());

        onView(withId(R.id.photo_view)).check(matches(isDisplayed()));
        onView(withId(R.id.rightClick)).check(matches(isDisplayed()));
        onView(withId(R.id.leftClick)).check(matches(isDisplayed()));

        onView(withId(R.id.rightClick)).perform(click());
        onView(withId(R.id.rightClick)).perform(click());
        onView(withId(R.id.leftClick)).perform(click());
    }
}
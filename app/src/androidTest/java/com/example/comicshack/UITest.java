package com.example.comicshack;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UITest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule(MainActivity.class);

    private static final int[] MENU_CONTENT_ITEM_IDS = {
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
    };
//    private Map<Integer, String> menuStringContent;

//    private BottomNavigationView bottomNavigation = findViewById(R.id.nav_view);
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.comicshack", appContext.getPackageName());
    }

    @Test
    public void testAddComicForm() {
//        final Menu menu = bottomNavigation.getMenu();
//        assertNotNull("Menu should not be null", menu);
//        assertEquals("Should have matching number of items", MENU_CONTENT_ITEM_IDS.length, menu.size());
//        for (int i = 0; i < MENU_CONTENT_ITEM_IDS.length; i++) {
//            final MenuItem currItem = menu.getItem(i);
//            assertEquals("ID for Item #" + i, MENU_CONTENT_ITEM_IDS[i], currItem.getItemId());
//        }

        ViewInteraction nav_view = onView(ViewMatchers.withId(R.id.nav_view)); //androidx.test.espresso.ViewInteraction@2b94970
        //perform some automation here to click on buttons

    }
}
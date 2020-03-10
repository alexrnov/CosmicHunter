package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.activities.MainActivity
import android.os.Build
import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Espresso Intents enables validation and stubbing of intents sent out by an app.
 * With Espresso Intents, you can test an app, activity, or service in isolation by
 * intercepting outgoing intents, stubbing the result, and sending it back to the
 * component under test.
 */
@RunWith(AndroidJUnit4::class)
class IntentTest {
  /*
    * The IntentsTestRule class initializes Espresso Intents before each test,
    * terminates the host activity, and releases Espresso Intents after each test.
    */
  @get:Rule
  var intentsRule: IntentsTestRule<MainActivity>
          = IntentsTestRule(MainActivity::class.java)

  @Test
  @SdkSuppress(minSdkVersion = 18) // that device maintain OpenGL 3.0
  fun passExtraToLevelActivity() {
    onView(withId(R.id.levelGameButton)).perform(click())
    intended(allOf(hasComponent(hasShortClassName(".activities.LevelsActivity")),
            toPackage("alexrnov.cosmichunter"),
            hasExtra("versionGLES", 3)))
  }

  @Test
  @SdkSuppress(minSdkVersion = 18)
  fun passExtraToGameActivity() {
    onView(withId(R.id.startGameButton)).perform(click())
    intended(allOf(hasComponent(
            hasShortClassName(".activities.GameActivity")),
            toPackage("alexrnov.cosmichunter"),
            allOf(hasExtra("versionGLES", 3), hasExtra("Level", 1))))
  }
}
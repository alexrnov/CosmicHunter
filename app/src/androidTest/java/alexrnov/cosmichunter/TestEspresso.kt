package alexrnov.cosmichunter

import alexrnov.cosmichunter.activities.LevelsActivity
import alexrnov.cosmichunter.activities.MainActivity
import alexrnov.cosmichunter.activities.SettingsActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class TestEspresso {

  @get:Rule
  var activityRule: ActivityTestRule<SettingsActivity>
          = ActivityTestRule(SettingsActivity::class.java)

  @Test
  fun f() {
    onView(withId(R.id.radioGroup_music))
            .perform(click())
            .check(matches(isDisplayed()))
  }
}
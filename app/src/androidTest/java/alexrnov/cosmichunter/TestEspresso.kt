package alexrnov.cosmichunter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class TestEspresso {

  @Test
  fun f() {
    onView(withId(R.id.button_level1))
            .perform(click())
            .check(matches(isDisplayed()))
  }
}
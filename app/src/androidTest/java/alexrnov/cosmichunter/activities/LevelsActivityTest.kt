package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import alexrnov.cosmichunter.base.Level
import alexrnov.cosmichunter.base.LevelDao
import alexrnov.cosmichunter.base.LevelDatabase
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LevelsActivityTest {
  private lateinit var title: String
  private lateinit var exit: String

  @get:Rule
  var activityRule: ActivityTestRule<LevelsActivity>
          = ActivityTestRule(LevelsActivity::class.java)

  @Before
  fun initStrings() {
    title = activityRule.activity.getString(R.string.select_level)
    exit = activityRule.activity.getString(R.string.exit)
  }

  @Test
  fun displayedComponents() {
    // проверить видимость компонентов
    onView(withId(R.id.toolbar_level_title)).check(matches(isDisplayed()))
    onView(withId(R.id.action_exit)).check(matches(isDisplayed()))
  }

  @Test
  fun textComponents() {
    // проверить значения текста для компонентов
    onView(withId(R.id.toolbar_level_title)).check(matches(withText(title)))
    onView(withId(R.id.action_exit)).check(matches(withText(exit)))
  }
}
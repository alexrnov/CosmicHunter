package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
  private lateinit var appName: String
  private lateinit var startGame: String
  private lateinit var gameSettings: String
  private lateinit var levelGame: String
  private lateinit var aboutGame: String
  private lateinit var exit: String

  @get:Rule
  var activityRule: ActivityTestRule<MainActivity>
          = ActivityTestRule(MainActivity::class.java)

  @Before
  fun initStrings() {
    // инициализорвать значения строк здесь, чтобы небыло несоответствий при
    // тестировании на устройствах с другой локализацией
    appName = activityRule.activity.getString(R.string.app_name)
    startGame = activityRule.activity.getString(R.string.start_game)
    gameSettings = activityRule.activity.getString(R.string.settings_button)
    levelGame = activityRule.activity.getString(R.string.select_level)
    aboutGame = activityRule.activity.getString(R.string.about_game_button)
    exit = activityRule.activity.getString(R.string.exit)
  }

  @Test
  fun displayedComponents() {
    // проверить видимость компонентов
    onView(withId(R.id.toolbar_main_menu)).check(matches(isDisplayed()))
    onView(withId(R.id.toolbar_title)).check(matches(isDisplayed()))
    onView(withId(R.id.action_exit)).check(matches(isDisplayed()))

    onView(withId(R.id.startGameButton)).check(matches(isDisplayed()))
    onView(withId(R.id.settingsButton)).check(matches(isDisplayed()))
    onView(withId(R.id.levelGameButton)).check(matches(isDisplayed()))
    onView(withId(R.id.aboutGameButton)).check(matches(isDisplayed()))
    onView(withId(R.id.exitButton)).check(matches(isDisplayed()))
    onView(withId(R.id.toolbar_main_menu)).check(matches(isDisplayed()))
  }

  @Test
  fun textComponents() {
    // проверить значения текста для компонентов основного меню
    onView(withId(R.id.toolbar_title)).check(matches(withText(appName)))
    onView(withId(R.id.action_exit)).check(matches(withText(exit)))

    onView(withId(R.id.startGameButton)).check(matches(withText(startGame)))
    onView(withId(R.id.settingsButton)).check(matches(withText(gameSettings)))
    onView(withId(R.id.levelGameButton)).check(matches(withText(levelGame)))
    onView(withId(R.id.aboutGameButton)).check(matches(withText(aboutGame)))
    onView(withId(R.id.exitButton)).check(matches(withText(exit)))
  }
}
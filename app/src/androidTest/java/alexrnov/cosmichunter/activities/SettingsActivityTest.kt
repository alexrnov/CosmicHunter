package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class SettingsActivityTest {

  private lateinit var soundTextLabel: String
  private lateinit var musicTextLabel: String
  private lateinit var vibrationTextLabel: String
  private lateinit var on: String
  private lateinit var off: String
  private lateinit var exit: String
  private lateinit var back: String

  @get:Rule
  var activityRule: ActivityTestRule<SettingsActivity>
          = ActivityTestRule(SettingsActivity::class.java)

  @Before
  fun initStrings() {
    soundTextLabel = activityRule.activity.getString(R.string.settings_sound_label)
    musicTextLabel = activityRule.activity.getString(R.string.settings_music_label)
    vibrationTextLabel = activityRule.activity.getString(R.string.settings_vibration_label)
    on = activityRule.activity.getString(R.string.settings_on)
    off = activityRule.activity.getString(R.string.settings_off)
    back = activityRule.activity.getString(R.string.back)
    exit = activityRule.activity.getString(R.string.exit)
  }

  @Test
  fun displayedComponents() { // проверить отображение компонентов
    onView(withId(R.id.toolbar_settings)).check(matches(isDisplayed()))
    onView(withId(R.id.toolbar_settings_title)).check(matches(isDisplayed()))
    onView(withId(R.id.action_exit)).check(matches(isDisplayed()))

    onView(withId(R.id.text_sound_label)).check(matches(isDisplayed()))
    onView(withId(R.id.text_music_label)).check(matches(isDisplayed()))
    onView(withId(R.id.text_vibration_label)).check(matches(isDisplayed()))
    onView(withId(R.id.musicOn)).check(matches(isDisplayed()))
    onView(withId(R.id.musicOff)).check(matches(isDisplayed()))
    onView(withId(R.id.soundOn)).check(matches(isDisplayed()))
    onView(withId(R.id.soundOff)).check(matches(isDisplayed()))
    onView(withId(R.id.vibrationOn)).check(matches(isDisplayed()))
    onView(withId(R.id.vibrationOff)).check(matches(isDisplayed()))
    onView(withId(R.id.back_button)).check(matches(isDisplayed()))
  }

  @Test
  fun textComponents() { // проверить текст в компонентах
    onView(withId(R.id.action_exit)).check(matches(withText(exit)))
    onView(withId(R.id.text_sound_label)).check(matches(withText(soundTextLabel)))
    onView(withId(R.id.text_music_label)).check(matches(withText(musicTextLabel)))
    onView(withId(R.id.text_vibration_label)).check(matches(withText(vibrationTextLabel)))
    onView(withId(R.id.musicOn)).check(matches(withText(on)))
    onView(withId(R.id.musicOff)).check(matches(withText(off)))
    onView(withId(R.id.soundOn)).check(matches(withText(on)))
    onView(withId(R.id.soundOff)).check(matches(withText(off)))
    onView(withId(R.id.vibrationOn)).check(matches(withText(on)))
    onView(withId(R.id.vibrationOff)).check(matches(withText(off)))
    // одного идентификатора в некоторых случаях бывает недостаточно: например когда id являются не уникальными
    onView(allOf(withId(R.id.back_button), withContentDescription("back button from settings")))
            .check(matches(withText(back)))
  }

  @Test
  fun snackbarVisible() {
    // проверить появление снэкбара при нажатии на радиокнопку звука
    onView(withId(R.id.radioGroup_sound)).perform(click())
    onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE))) // matches(withText("text")

    // проверить появление снэкбара при нажатии на радиокнопку музыки
    onView(withId(R.id.radioGroup_music)).perform(click())
    onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))

    // проверить появление снэкбара при нажатии на радиокнопку вибрации
    onView(withId(R.id.radioGroup_vibration)).perform(click())
    onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
  }
}
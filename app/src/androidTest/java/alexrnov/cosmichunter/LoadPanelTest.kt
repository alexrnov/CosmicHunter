package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.UiObject
import com.google.common.truth.Truth.assertThat


private const val LAUNCH_TIMEOUT = 3000L
private const val BASIC_SAMPLE_PACKAGE = "alexrnov.cosmichunter"

@RunWith(AndroidJUnit4::class)
// аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
@SdkSuppress(minSdkVersion = 18)
class LoadPanelTest {
  private lateinit var device: UiDevice

  private lateinit var loadGameText: String
  private lateinit var levelNumberText: String

  @Before
  fun startMainActivityFromHomeScreen() {
    // Initialize UiDevice instance
    device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    device.pressHome() // Start from the home screen
    // ждем запуска
    val launcherPackage: String = device.launcherPackageName
    assertThat(launcherPackage, CoreMatchers.notNullValue())
    device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT)

    // Launch the app
    val context = ApplicationProvider.getApplicationContext<Context>()
    val intent = context.packageManager
            .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE)?.apply {
              // Clear out any previous instances
              addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
    context.startActivity(intent)
    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT ) // Wait

    loadGameText = context.getString(R.string.load_game_text)
    levelNumberText = context.getString(R.string.level)
  }

  @Test
  fun startGameByMainActivity() {
    val startGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/startGameButton"))
    startGameButton.click() // начать игру

    // проверить, что панель загрузки отображается
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // до Android 7
      onView(withId(R.id.load_panel_game)).check(matches(isDisplayed()))
    } else { // начиная с Android 7
      /* // поиск объекта по тексту
      val loadGame: UiObject = device
           .findObject(UiSelector().text(loadGameText))
       assertThat(loadGame.text).isEqualTo(loadGameText)
      */
      val loadGame: UiObject = device.findObject(UiSelector()
              .resourceId("${BASIC_SAMPLE_PACKAGE}:id/load_game_text"))
      assertThat(loadGame.text).isEqualTo(loadGameText)
    }

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait
  }

  @Test
  fun startGameByLevelActivity() {
    val levelGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/levelGameButton"))
    levelGameButton.clickAndWaitForNewWindow() // начать игру

    val level1Button: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/button_level1"))

    level1Button.click()
    val firstLevel = "$levelNumberText 1"
    // проверить, что панель загрузки отображается
    // (а именно надпись номера уровня на этой панели)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) { // до Android 7
      onView(withId(R.id.load_level_text)).check(matches(isDisplayed()))
      onView(withId(R.id.load_level_text)).check(matches(withText(firstLevel)))
    } else { // начиная с Android 7
      val levelN: UiObject = device.findObject(UiSelector()
              .resourceId("${BASIC_SAMPLE_PACKAGE}:id/load_level_text"))
      assertThat(levelN.text).isEqualTo(firstLevel)
    }
    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait
    device.pressBack()
  }
}
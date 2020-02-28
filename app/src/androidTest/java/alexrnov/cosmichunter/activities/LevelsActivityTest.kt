package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.R
import android.content.Context
import android.content.Intent
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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

private const val LAUNCH_TIMEOUT = 3000L
private const val BASIC_SAMPLE_PACKAGE = "alexrnov.cosmichunter"

/**
 * Для теста активити с выбором уровней приходится использовать uiautomator
 * поскольку, должен быть обязательно вызван класс инициализации Initialization.class
 * для того, чтобы создать базу данных
 */
@RunWith(AndroidJUnit4::class)
// аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
@SdkSuppress(minSdkVersion = 18)
class LevelsActivity2Test {
  private lateinit var device: UiDevice
  private lateinit var title: String
  private lateinit var exit: String

  private lateinit var level1: String
  private lateinit var level2: String
  private lateinit var level3: String
  private lateinit var level4: String
  private lateinit var level5: String
  private lateinit var back: String

  @Before
  fun startMainActivityFromHomeScreen() {
    // Initialize UiDevice instance
    device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    device.pressHome() // Start from the home screen
    // ждем запуска
    val launcherPackage: String = device.launcherPackageName
    ViewMatchers.assertThat(launcherPackage, CoreMatchers.notNullValue())
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


    // инициализорвать значения строк здесь, чтобы небыло несоответствий при
    // тестировании на устройствах с другой локализацией
    title = context.getString(R.string.select_level)
    exit = context.getString(R.string.exit)

    level1 = context.getString(R.string.level1)
    level2 = context.getString(R.string.level2)
    level3 = context.getString(R.string.level3)
    level4 = context.getString(R.string.level4)
    level5 = context.getString(R.string.level5)
    back = context.getString(R.string.back)
  }

  @Test
  fun visibleComponents() {
    val levelGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/levelGameButton"))
    levelGameButton.click() // перейти в выбор уровней
    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait

    // проверить видимость компонентов
    onView(withId(R.id.toolbar_level_title)).check(matches(isDisplayed()))
    onView(withId(R.id.action_exit)).check(matches(isDisplayed()))
    onView(withId(R.id.button_level1)).check(matches(isDisplayed()))
    onView(withId(R.id.button_level2)).check(matches(isDisplayed()))
    onView(withId(R.id.button_level3)).check(matches(isDisplayed()))
    onView(withId(R.id.button_level4)).check(matches(isDisplayed()))
    onView(withId(R.id.button_level5)).check(matches(isDisplayed()))
    onView(withId(R.id.back_button)).check(matches(isDisplayed()))

    // проверить значения текста для компонентов
    onView(withId(R.id.toolbar_level_title)).check(matches(withText(title)))
    onView(withId(R.id.action_exit)).check(matches(withText(exit)))
    onView(withId(R.id.button_level1)).check(matches(withText(level1)))
    onView(withId(R.id.button_level2)).check(matches(withText(level2)))
    onView(withId(R.id.button_level3)).check(matches(withText(level3)))
    onView(withId(R.id.button_level4)).check(matches(withText(level4)))
    onView(withId(R.id.button_level5)).check(matches(withText(level5)))
    onView(withId(R.id.back_button)).check(matches(withText(back)))
  }
}
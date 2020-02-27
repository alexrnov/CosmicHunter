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
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
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

    title = context.getString(R.string.select_level)
    exit = context.getString(R.string.exit)
  }

  @Test
  fun f() {
    val levelGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/levelGameButton"))
    levelGameButton.click() // перейти в выбор уровней
    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait

    // проверить видимость компонентов
    onView(ViewMatchers.withId(R.id.toolbar_level_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    onView(ViewMatchers.withId(R.id.action_exit)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    // проверить значения текста для компонентов
    onView(ViewMatchers.withId(R.id.toolbar_level_title)).check(ViewAssertions.matches(ViewMatchers.withText(title)))
    onView(ViewMatchers.withId(R.id.action_exit)).check(ViewAssertions.matches(ViewMatchers.withText(exit)))
  }
}
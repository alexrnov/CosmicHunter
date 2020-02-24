package alexrnov.cosmichunter

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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

@RunWith(AndroidJUnit4::class)
// аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
@SdkSuppress(minSdkVersion = 18)
class NavigationTest {
  private lateinit var device: UiDevice

  @Before
  fun startMainActivityFromHomeScreen() {
    // Initialize UiDevice instance
    device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())


    // Start from the home screen
    device.pressHome()

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
  }

  @Test
  fun launchApplication() {
    // Получить объект кнопки. Если вы хотите получить доступ к определенному
    // компоненту пользовательского интерфейса в приложении, используйте класс
    // UiSelector. Этот класс представляет запрос для определенных элементов в
    // отображаемом в настоящее время пользовательском интерфейсе.
    val settingsButton: UiObject = device.findObject(UiSelector()
            .resourceId("$BASIC_SAMPLE_PACKAGE:id/settingsButton"))
    settingsButton.click() // нажать на кнопку Espresso

    val backFromSettingsButton: UiObject = device.findObject(UiSelector()
            .resourceId("$BASIC_SAMPLE_PACKAGE:id/back_button"))
    backFromSettingsButton.click() // вернутся в меню

    val levelGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("$BASIC_SAMPLE_PACKAGE:id/levelGameButton"))
    levelGameButton.click()

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT ) // Wait

    val backFromLevelsButton: UiObject = device.findObject(UiSelector()
            .resourceId("$BASIC_SAMPLE_PACKAGE:id/back_button"))
    backFromLevelsButton.click() // нажать на кнопку Espresso

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT)

    val aboutGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("$BASIC_SAMPLE_PACKAGE:id/aboutGameButton"))
    aboutGameButton.click() // нажать на кнопку Espresso

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT)

    onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT)

  }

  @Test
  fun levelsActivity() {

  }
}
package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
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

@RunWith(AndroidJUnit4::class)
// аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
@SdkSuppress(minSdkVersion = 18)
class AboutActivityTest {
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

    // инициализорвать значения строк здесь, чтобы небыло несоответствий при
    // тестировании на устройствах с другой локализацией
    title = context.getString(R.string.about_game_button)
    exit = context.getString(R.string.exit)
  }

  @Test
  fun visibleComponent() {
    val aboutGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/aboutGameButton"))
    aboutGameButton.click() // перейти в выбор уровней
    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait

    // проверить видимость компонентов
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))
    onView(withId(R.id.action_exit)).check(matches(isDisplayed()))

    // проверить значения текста для компонентов
    onView(withId(R.id.toolbar_about_game_title)).check(matches(withText(title)))
    onView(withId(R.id.action_exit)).check(matches(withText(exit)))

    val musicLink: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/musicLink"))
    musicLink.click() // открыть ссылку
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val soundLink: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/soundLink"))
    soundLink.click()
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))


    val textureLink: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/textureLink"))
    textureLink.click()
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val pictureLink: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/pictureLink"))
    pictureLink.click()
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val blenderLink: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/blenderLink"))
    blenderLink.click()
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait
  }
}
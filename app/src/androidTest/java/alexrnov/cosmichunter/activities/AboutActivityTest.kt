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
import com.google.common.truth.Truth.assertThat

private const val LAUNCH_TIMEOUT = 3000L
private const val BASIC_SAMPLE_PACKAGE = "alexrnov.cosmichunter"

@RunWith(AndroidJUnit4::class)
// аннотация проверяет, что используется API минимум 18 версии, как требует фреймворк Automator
@SdkSuppress(minSdkVersion = 18)
class AboutActivityTest {
  private lateinit var device: UiDevice
  private lateinit var title: String
  private lateinit var exit: String

  private lateinit var musicText: String
  private lateinit var musicLink: String
  private lateinit var soundText: String
  private lateinit var soundLink: String
  private lateinit var textureText: String
  private lateinit var textureLink: String
  private lateinit var pictureText: String
  private lateinit var pictureLink: String
  private lateinit var blenderText: String
  private lateinit var blenderLink: String

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
    musicText = context.getString(R.string.music_text)
    musicLink = context.getString(R.string.music_link)
    soundText = context.getString(R.string.sound_text)
    soundLink = context.getString(R.string.sound_link)
    pictureText = context.getString(R.string.pictures_text)
    pictureLink = context.getString(R.string.pictures_link)
    textureText = context.getString(R.string.textures_text)
    textureLink = context.getString(R.string.textures_link)
    blenderText = context.getString(R.string.blender_text)
    blenderLink = context.getString(R.string.blender_link)


    val aboutGameButton: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/aboutGameButton"))
    aboutGameButton.click() // перейти в выбор уровней
    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT) // Wait
  }

  @Test
  fun visibleComponent() {
    // проверить видимость компонентов
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))
    onView(withId(R.id.action_exit)).check(matches(isDisplayed()))
    onView(withId(R.id.musicText)).check(matches(isDisplayed()))
    onView(withId(R.id.musicLink)).check(matches(isDisplayed()))
    onView(withId(R.id.soundText)).check(matches(isDisplayed()))
    onView(withId(R.id.soundLink)).check(matches(isDisplayed()))
    onView(withId(R.id.textureText)).check(matches(isDisplayed()))
    onView(withId(R.id.textureLink)).check(matches(isDisplayed()))
    onView(withId(R.id.pictureText)).check(matches(isDisplayed()))
    onView(withId(R.id.pictureLink)).check(matches(isDisplayed()))
    onView(withId(R.id.blenderText)).check(matches(isDisplayed()))
    onView(withId(R.id.blenderLink)).check(matches(isDisplayed()))

    // проверить значения текста для компонентов
    onView(withId(R.id.toolbar_about_game_title)).check(matches(withText(title)))
    onView(withId(R.id.action_exit)).check(matches(withText(exit)))
    onView(withId(R.id.musicText)).check(matches(withText(musicText)))
    onView(withId(R.id.musicLink)).check(matches(withText(musicLink)))
    onView(withId(R.id.soundText)).check(matches(withText(soundText)))
    onView(withId(R.id.soundLink)).check(matches(withText(soundLink)))
    onView(withId(R.id.textureText)).check(matches(withText(textureText)))
    onView(withId(R.id.textureLink)).check(matches(withText(textureLink)))
    onView(withId(R.id.pictureText)).check(matches(withText(pictureText)))
    onView(withId(R.id.pictureLink)).check(matches(withText(pictureLink)))
    onView(withId(R.id.blenderText)).check(matches(withText(blenderText)))
    onView(withId(R.id.blenderLink)).check(matches(withText(blenderLink)))
  }

  @Test
  fun checkLinks() { // проверить переход по ссылкам
    val musicLinkAuto: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/musicLink"))

    // открыть ссылку и ждать минимум времени, чтобы страница не успела выдать
    // какие-либо диалоговые окна
    musicLinkAuto.clickAndWaitForNewWindow(0L)
    var currentPackage: String = device.currentPackageName
    assertThat(currentPackage).isEqualTo("com.android.chrome")
    device.pressBack() // вернуться обратно в приложение
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val soundLinkAuto: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/soundLink"))
    soundLinkAuto.clickAndWaitForNewWindow(0L)
    currentPackage = device.currentPackageName
    // проверить что браузер открывается
    assertThat(currentPackage).isEqualTo("com.android.chrome")
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val textureLinkAuto: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/textureLink"))
    textureLinkAuto.clickAndWaitForNewWindow(0L)
    currentPackage = device.currentPackageName
    assertThat(currentPackage).isEqualTo("com.android.chrome")
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val pictureLinkAuto: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/pictureLink"))
    pictureLinkAuto.clickAndWaitForNewWindow(0L)
    currentPackage = device.currentPackageName
    assertThat(currentPackage).isEqualTo("com.android.chrome")
    device.pressBack()

    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    val blenderLinkAuto: UiObject = device.findObject(UiSelector()
            .resourceId("${BASIC_SAMPLE_PACKAGE}:id/blenderLink"))
    blenderLinkAuto.clickAndWaitForNewWindow(0L)
    currentPackage = device.currentPackageName
    assertThat(currentPackage).isEqualTo("com.android.chrome")
    device.pressBack()
    onView(withId(R.id.toolbar_about_game_title)).check(matches(isDisplayed()))

    device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT)
  }
}
package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import android.content.Context
import android.util.Log
import org.junit.Assert.*
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VibratorExplosionTest {
  // Given a Context object retrieved from Robolectric...
  private val context = ApplicationProvider.getApplicationContext<Context>()
  private val vibrator = VibratorExplosion(context, 500)

  @Test
  fun f() {
    vibrator.execute()
  }
}
package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.util.Log

/** Вывест на экран информацию по размерам экрана (в единицах dp) */
@SuppressLint("ObsoleteSdkInt")
fun printDPSizeScreen(activity: AppCompatActivity) {
  fun getScreenSizes(): Pair<Int, Int> {
    val display = activity.windowManager.defaultDisplay
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      val size = Point()
      display.getSize(size) // was introduced in Level API 13 and height
      Pair(size.x, size.y)
    } else @Suppress("DEPRECATION") Pair(display.width, display.height)
  }
  val (width, height) = getScreenSizes()
  val displayMetrics = activity.resources.displayMetrics
  // логическая плотность дисплея. Это мастштабирующий фактор, независящий
  // от плотности пикселей (added in API Level 1)
  val density:Float = displayMetrics.density
  val dpWidth: Float = width / density
  val dpHeight: Float = height / density
  Log.i(TAG, "density = $density")
  Log.i(TAG, "width = $width, height = $height")
  Log.i(TAG, "dpWidth = $dpWidth, dpHeight = $dpHeight")
}
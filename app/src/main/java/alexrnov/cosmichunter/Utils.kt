package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log

@SuppressLint("ObsoleteSdkInt")
fun printScreenSizes(activity: AppCompatActivity) {
  val display = activity.windowManager.defaultDisplay
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
    val size = Point()
    display.getSize(size) // was introduced in Level API 13 and height
    Log.i(TAG, "size.x = " + size.x + ", size.y = " + size.y)
  } else {
    @Suppress("DEPRECATION")
    Log.i(TAG, "width = " + display.width + ", height = " + display.height)
  }
  val metrics = DisplayMetrics()
  display.getMetrics(metrics)
  Log.i(TAG, "widthPixels = " + metrics.widthPixels + ", heightPixels = " + metrics.heightPixels)
}
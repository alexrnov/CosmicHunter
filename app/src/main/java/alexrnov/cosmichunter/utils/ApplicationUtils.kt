package alexrnov.cosmichunter.utils

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.R
import alexrnov.cosmichunter.activities.GameActivity
import alexrnov.cosmichunter.view.RocketView3D
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import org.jetbrains.annotations.NotNull

/*
interface CosmicRenderer : GLSurfaceView.Renderer {
  val widthDisplay: Int
  val heightDisplay: Int
  fun setPassXY(passX: Float, passY: Float)
  fun setGameActivity(@NotNull gameActivity: GameActivity)
}

*/

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
  Log.i(TAG, "density = $density, width = $width, height = $height, dpWidth = $dpWidth, dpHeight = $dpHeight")
}

/**
 * Показать снэкбар (уведомление).
 * [view] - корневой макет;
 * [message] - текст сообщения.
 */
fun showSnackbar(view: View, message: CharSequence) {
  val snackbar = Snackbar.make(view, message, 2000)
  snackbar.setAction("OK") { snackbar.dismiss() } // при нажатии на кнопку snackbar просто скрывается
  snackbar.setActionTextColor(Color.parseColor("#98d4c1")) // цвет кнопки
  val snackbarView = snackbar.view
  snackbarView.setBackgroundColor(Color.parseColor("#575757")) // цвет фона
  snackbarView.setPadding(0, 0, 0, 0)
  // установить ширину снэкбара по экрану - это необходимо, так как на tablet снекбар по умолчанию занимает только часть экрана
  val params = snackbarView.layoutParams as FrameLayout.LayoutParams
  // другой вариант расширить снэкбар
  //snackbarView.getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;
  params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
  params.width = FrameLayout.LayoutParams.MATCH_PARENT
  snackbarView.layoutParams = params

  // вызов для библиотеки поддержки android support
  //val textView = snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
  // вызов для библиотеки androidx
  val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
  textView.setTextColor(Color.parseColor("#e7e7e7")) // цвет сообщения
  snackbar.show()
}

/**
 * Показать toast.
 * [context] - контекст, из которого был вызван метод;
 * [message] - текст сообщения.
 */
fun showToast(context: Context, message: CharSequence) {
  val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
  //установить положение сообщения на экране: сверху, по центру, с отступом от верхенего края
  toast.setGravity(Gravity.BOTTOM or Gravity.CENTER,0,20)
  toast.show()
}

/**
 * Показать toast с настроенным View
 * [activity] - действие, в котором был вызван метод;
 * [message] - текст сообщения
 */
fun showCustomToast(activity: Activity, message: CharSequence) {
  val inflater = activity.layoutInflater
  //первый параметр - xml-файл с представлением, второй параметр
  //- корневой вьюер в этом файле
  val layout = inflater.inflate(R.layout.custom_toast,
          activity.findViewById(R.id.custom_toast_container))
  val text = layout.findViewById<View>(R.id.text_toast) as TextView
  text.text = message
  val toast = Toast(activity.applicationContext)
  toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 20)
  toast.duration = Toast.LENGTH_SHORT
  toast.view = layout
  toast.show()
}

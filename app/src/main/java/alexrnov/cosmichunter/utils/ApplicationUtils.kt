package alexrnov.cosmichunter.utils

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import android.view.WindowManager
import android.content.res.Configuration
import android.util.Log

/**
 * Получить размеры экрана не учитывая ширину навигационной панели.
 * Т.е. размеры экрана будут равныЖ физические размеры экрана + размеры нав. панели
 */
@SuppressLint("ObsoleteSdkInt")
fun getScreenSizeWithoutNavBar(activity: AppCompatActivity): Pair<Int, Int> {
  val display = activity.windowManager.defaultDisplay
  return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
    val size = Point()
    display.getSize(size) // was introduced in Level API 13 and height
    printDensityScreen(activity, size.x, size.y)
    Pair(size.x, size.y)
  } else {
    @Suppress("DEPRECATION")
    Pair(display.width, display.height)
  }
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

/** Получить размеры экрана с навигационной панелью */
/* solution offer by EC84B4: https://stackoverflow.com/questions/26674378/android-get-screen-size-including-the-size-of-status-bar-and-software-navigation */
fun getScreenSizeWithNavBar(context: Context): Pair<Int, Int> {
  val x: Int
  val y: Int
  val orientation = context.resources.configuration.orientation
  val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
  val display = wm.defaultDisplay
  val screenSize = Point()
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
    display.getRealSize(screenSize)
    x = screenSize.x
    y = screenSize.y
  } else {
    display.getSize(screenSize)
    x = screenSize.x
    y = screenSize.y
  }
  val width = getWidth(x, y, orientation)
  val height = getHeight(x, y, orientation)
  return Pair(width, height)
}

private fun getWidth(x: Int, y: Int, orientation: Int): Int {
  return if (orientation == Configuration.ORIENTATION_PORTRAIT) x else y
}

private fun getHeight(x: Int, y: Int, orientation: Int): Int {
  return if (orientation == Configuration.ORIENTATION_PORTRAIT) y else x
}

/** Вывест на экран информацию по размерам экрана (в единицах dp) */
private fun printDensityScreen(activity: AppCompatActivity, width: Int, height: Int) {
  val displayMetrics = activity.resources.displayMetrics
  // логическая плотность дисплея. Это мастштабирующий фактор,
  // независящий от плотности пикселей (added in API Level 1)
  val density: Float = displayMetrics.density
  val dpWidth: Float = width / density
  val dpHeight: Float = height / density
  Log.i(TAG, "density = $density, width = $width, height = $height, " +
          "dpWidth = $dpWidth, dpHeight = $dpHeight")
}

fun backToHome(activity : AppCompatActivity) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    activity.startActivity(intent) // выйти на рабочий стол системы
    // Android 5.0 (API 21) and higher
    // завершить все активити в этой задаче и удалить их из списка "недавние" (recent)
    // работает правильно, если в манифесте launchMode="singleTask" а не "standard"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) activity.finishAndRemoveTask()
    else activity.finish()
    System.exit(0)
  }

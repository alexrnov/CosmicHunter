package alexrnov.cosmichunter.utils

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.R
import alexrnov.cosmichunter.activities.GameActivity
import alexrnov.cosmichunter.view.RocketView3D
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Point
import android.opengl.GLSurfaceView
import android.os.Build
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast

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


/**
 * Интерфейс введен для случая, когда класс-наследник View3D работает
 * c экземпляром RocketGLES30 для разных версий OpenGL
 */
interface Rocket {
  val view: RocketView3D
  fun draw()
}
/*
 * java:
 * public interface Rocket {
 *   RocketView3D getView();
 *   void draw();
 * }
 */

/**
 * Интерфейс нужен для того, чтобы в классе OGLView (SurfaceView) можно
 * было использовать классы рендера как для OpenGL 2.0, так и для OpenGL 3.0
 */
interface SceneRenderer : GLSurfaceView.Renderer {
  val widthDisplay: Int
  val heightDisplay: Int
  fun setPassXY(passX: Float, passY: Float)
  fun setGameActivity(gameActivity: GameActivity)
}

/*
 * java:
 * public interface SceneRenderer extends GLSurfaceView.Renderer {
 *   int getWidthDisplay();
 *   int getHeightDisplay();
 *   void setPassXY(float passX, float passY);
 *   void setGameActivity(GameActivity gameActivity);
 * }
*/


fun showSnackbar(view: View, message: String) {
  val snackbar = Snackbar.make(view, message, 2000)
  snackbar.setAction("OK") {
    snackbar.dismiss() // при нажатии на кнопку snackbar просто скрывается
  }
  snackbar.setActionTextColor(Color.parseColor("#98d4c1")) // цвет кнопки
  val snackbarView = snackbar.view
  snackbarView.setBackgroundColor(Color.parseColor("#575757")) // цвет фона
  snackbarView.setPadding(0, 0, 0, 0)
  //snackbarView.getLayoutParams().width = AppBarLayout.LayoutParams.MATCH_PARENT;

  val params = snackbarView.layoutParams as FrameLayout.LayoutParams
  params.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
  params.width = FrameLayout.LayoutParams.MATCH_PARENT
  //params.setMargins(params.leftMargin - 100,
  //     params.topMargin,
  //    params.rightMargin,
  //   params.bottomMargin + 100);
  snackbarView.layoutParams = params

  val textView = snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
  textView.setTextColor(Color.parseColor("#e7e7e7")) // цвет сообщения
  snackbar.show()
}

/*
fun showToast(activity:AppCompatActivity, message: String) {
  val inflater = activity.layoutInflater
  //первый параметр - xml-файл с представлением, второй параметр
  //- корневой вьюер в этом файле
  val layout = inflater.inflate(R.layout.custom_toast,
          activity.findViewById(R.id.custom_toast_container) as ViewGroup)

  val text = layout.findViewById(R.id.text_toast) as TextView
  text.text = message
  val toast = Toast(activity.applicationContext)
  toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 20)
  toast.duration = Toast.LENGTH_SHORT
  toast.view = layout
  toast.show()

}*/

fun showToast(app: AppCompatActivity, message: String) {
  Log.i(TAG, "1")
  val inflater = app.layoutInflater
  Log.i(TAG, "2")
  //первый параметр - xml-файл с представлением, второй параметр
  //- корневой вьюер в этом файле
  val v2 = R.layout.custom_toast
  Log.i(TAG, "8")
  try {
    val v3: View? = app.findViewById<View>(R.id.custom_toast_container)
    Log.i(TAG, v3?.let {"v3 != null"} ?: "v3 == null")
    val v = app.findViewById<View>(R.id.custom_toast_container) as ViewGroup
  } catch(e: RuntimeException) {
    Log.i(TAG, e.message)
  }
  Log.i(TAG, "9")
  val layout = inflater.inflate(R.layout.custom_toast,
          app.findViewById<View>(R.id.custom_toast_container) as ViewGroup)
  Log.i(TAG, "3")
  val text = layout.findViewById<View>(R.id.text_toast) as TextView
  Log.i(TAG, "4")
  text.text = "OpenGL не поддерживается"
  Log.i(TAG, "5")
  val toast = Toast(app.applicationContext)
  Log.i(TAG, "6")
  toast.setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 20)
  Log.i(TAG, "7")
  toast.duration = Toast.LENGTH_SHORT
  Log.i(TAG, "8")
  toast.view = layout
  toast.show()
}

package alexrnov.cosmichunter.utils

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.activities.GameActivity
import alexrnov.cosmichunter.view.RocketView3D
import android.annotation.SuppressLint
import android.graphics.Point
import android.opengl.GLSurfaceView
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



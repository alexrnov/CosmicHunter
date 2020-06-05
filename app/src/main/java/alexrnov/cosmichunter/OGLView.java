package alexrnov.cosmichunter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;

import alexrnov.cosmichunter.gles.Level1;
import alexrnov.cosmichunter.gles.Level2;
import alexrnov.cosmichunter.gles.Level3;
import alexrnov.cosmichunter.gles.Level4;
import alexrnov.cosmichunter.gles.Level5;
import alexrnov.cosmichunter.gles.SceneRenderer;
import androidx.core.view.GestureDetectorCompat;

import android.util.AttributeSet;
//import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.cosmichunter.activities.GameActivity;
import alexrnov.cosmichunter.utils.commonGL.CoordinatesOpenGL;

//import static alexrnov.cosmichunter.Initialization.TAG;

/**
 * Используется при выводе рендера openGL в отдельный компонент интерфейса
 */
public class OGLView extends GLSurfaceView implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

  private SceneRenderer renderer;
  private GestureDetectorCompat mDetector;
  private CoordinatesOpenGL coordinatesOpenGL;
  private volatile float xPress; // переменные используются в другом потоке(OpenGL)
  private volatile float yPress;

  public OGLView(Context context) {
    super(context);
  }

  public OGLView(Context context, AttributeSet attributes) {
    super(context, attributes);
  }

  public void init(Context context, int versionGLES, int level) {
    setPreserveEGLContextOnPause(true); // сохранять контескт OpenGL
    renderer = createSceneRenderer(context, versionGLES, level);
    setRenderer(renderer);
    //осуществлять рендеринг только когда изминились данные для рисования
    //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    //определить детектор жестов с контекстом приложения и
    //реализацией GestureDetector.OnGestureListener
    mDetector = new GestureDetectorCompat(context, this);
    //установить детектор жестов как слушатель двойного нажатия
    mDetector.setOnDoubleTapListener(this);
    coordinatesOpenGL = new CoordinatesOpenGL();
  }

  // Проверка поддержки OpenGL 2.0 и OpenGL 3.0 в runtime
  private SceneRenderer createSceneRenderer(Context context, int versionGLES, int level) {
    // Сообщить контейнеру OGLView, что мы хотим создать OpenGL ES 2.0 (или 3.0)-совместимый
    // контекст, и установить OpenGL ES 2.0 (или 3.0)-совместимый рендер
    setEGLContextClientVersion(versionGLES);
    //Log.i(TAG, this.getClass().getSimpleName() + ": version GLES = " + versionGLES);

    switch (level) {
      case 1: return new Level1((double) versionGLES, context);
      case 2: return new Level2((double) versionGLES, context);
      case 3: return new Level3((double) versionGLES, context);
      case 4: return new Level4((double) versionGLES, context);
      case 5: return new Level5((double) versionGLES, context);
    }
    return new Level1((double) versionGLES, context);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent e) {
    if (mDetector.onTouchEvent(e)) return true;
    return super.onTouchEvent(e);
  }

  @Override
  public boolean onDown(MotionEvent event) {
    //преобразовать координаты экрана(пикселы) в координаты OpenGL
    coordinatesOpenGL.fromDisplay(renderer.getWidthDisplay(),
              renderer.getHeightDisplay(),
              event.getX(), event.getY());
    xPress = coordinatesOpenGL.getXGL();//координаты OpenGL
    yPress = coordinatesOpenGL.getYGL();
    renderer.setPassXY(xPress, yPress);

    return true;
  }

  @Override
  public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
    return true;
  }

  @Override
  public boolean onDoubleTap(MotionEvent motionEvent) {
    return true;
  }

  @Override
  public boolean onDoubleTapEvent(MotionEvent motionEvent) {
    return true;
  }

  @Override
  public void onShowPress(MotionEvent motionEvent) {

  }

  @Override
  public boolean onSingleTapUp(MotionEvent motionEvent) {
    return true;
  }

  @Override
  public boolean onScroll(MotionEvent event1, MotionEvent event2,
                          float distanceX, float distanceY) {
    // Log.v(TAG, "onScroll: " + event1.getX() + ", " + event1.getY()
    //       + "//" + event2.getX() + ", " + event2.getY());

    /*
    //преобразовать координаты экрана(пикселы) в координаты OpenGL
    coordinatesOpenGL.fromDisplay(renderer.getWidthDisplay(),
            renderer.getHeightDisplay(),
            event2.getX(), event2.getY());
    xPress = coordinatesOpenGL.getXGL();//координаты OpenGL
    yPress = coordinatesOpenGL.getYGL();
    renderer.setPassXY(xPress, yPress);
    */
    return true;

  }

  @Override
  public void onLongPress(MotionEvent motionEvent) {

  }

  @Override
  public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2,
                         float v1, float v2) {
    //сильное нажатие
    return true;
  }

  public void setGameActivity(GameActivity gameActivity) {
    renderer.setGameActivity(gameActivity);
  }

  public SceneRenderer getSceneRenderer() {
    return renderer;
  }
}

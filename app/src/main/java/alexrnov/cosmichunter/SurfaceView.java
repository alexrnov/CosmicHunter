package alexrnov.cosmichunter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;

import androidx.core.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import alexrnov.cosmichunter.utils.commonGL.CoordinatesOpenGL;

/** Может использоваться при полноэкранном режиме */
@SuppressWarnings("unused")
public class SurfaceView extends GLSurfaceView
        implements GestureDetector.OnGestureListener,
          GestureDetector.OnDoubleTapListener {

  private final Level1 renderer;
  private GestureDetectorCompat mDetector;
  private CoordinatesOpenGL coordinatesOpenGL;
  private volatile float xPress;//переменные используются в другом потоке(OpenGL)
  private volatile float yPress;

  public SurfaceView(Context context) {
    super(context);
    //установить приоритет потока main - как самый низкий
    //Thread.currentThread().setPriority(10);
    //Сообщить контейнеру mGLSurfaceView, что мы хотим создать
    //OpenGL ES 3.0-совместимый контекст, и установить
    //OpenGL ES 3.0-совместимый рендер
    setEGLContextClientVersion(3);
    renderer = new Level1(3.0, context);
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

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent e) {

    if (mDetector.onTouchEvent(e)) {
      return true;
    }

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

  public Level1 getRenderer() {
    return renderer;
  }
}

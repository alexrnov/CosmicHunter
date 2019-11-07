package alexrnov.cosmichunter;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class OGLView extends GLSurfaceView {

  private SceneRenderer renderer;

  public OGLView(Context context) {
    super(context);
    init(context);
  }

  public OGLView(Context context, AttributeSet attributes) {
    super(context, attributes);
    init(context);
  }

  private void init(Context context) {
    setEGLContextClientVersion(3);
    setPreserveEGLContextOnPause(true); // сохранять контескт OpenGL

    //Сообщить контейнеру mGLSurfaceView, что мы хотим создать
    //OpenGL ES 3.0-совместимый контекст, и установить
    //OpenGL ES 3.0-совместимый рендер
    setEGLContextClientVersion(3);
    renderer = new SceneRenderer(context);
    setRenderer(renderer);
    //осуществлять рендеринг только когда изминились данные для рисования
    //setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
  }
}

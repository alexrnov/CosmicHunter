package alexrnov.cosmichunter;

import android.opengl.GLSurfaceView;

/**
 * Интерфейс нужен для того, чтобы в классе OGLView (SurfaceView) можно
 * было использовать классы рендера как для OpenGL 2.0, так и для OpenGL 3.0
 */
public interface SceneRenderer extends GLSurfaceView.Renderer {
  int getWidthDisplay();
  int getHeightDisplay();
  void setPassXY(float passX, float passY);
  void setGameActivity(GameActivity gameActivity);
}

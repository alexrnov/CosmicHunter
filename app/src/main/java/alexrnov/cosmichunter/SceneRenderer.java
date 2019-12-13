package alexrnov.cosmichunter;

import android.opengl.GLSurfaceView;

public interface SceneRenderer extends GLSurfaceView.Renderer {
  int getWidthDisplay();
  int getHeightDisplay();
  void setPassXY(float passX, float passY);
  void setGameActivity(GameActivity gameActivity);
}

package alexrnov.cosmichunter.gles;

import android.opengl.GLSurfaceView;

import org.jetbrains.annotations.NotNull;

import alexrnov.cosmichunter.activities.GameActivity;

public interface SceneRenderer extends GLSurfaceView.Renderer  {
  /** установить ссылку на gameActivity, чтобы связываться с цифровым табло во время игры */
  void setGameActivity(@NotNull GameActivity gameActivity);

  /** передать координаты нажатия на экран */
  void setPassXY(float passX, float passY);

  /** @return ширина дисплея OpenGL (в пикселах) */
  int getWidthDisplay();

  /** @return высота дисплея OpenGL (в пикселах) */
  int getHeightDisplay();

}

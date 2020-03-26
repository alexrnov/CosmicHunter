package alexrnov.cosmichunter.gles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import alexrnov.cosmichunter.activities.GameActivity;
import alexrnov.cosmichunter.gles.objects.Asteroid;
import alexrnov.cosmichunter.gles.objects.BasaltAsteroid;
import alexrnov.cosmichunter.gles.objects.Explosion;
import alexrnov.cosmichunter.gles.objects.MetalAsteroid;

public interface SceneRenderer extends GLSurfaceView.Renderer  {
  /** установить ссылку на gameActivity, чтобы связываться с цифровым табло во время игры */
  void setGameActivity(@NotNull GameActivity gameActivity);

  /** передать координаты нажатия на экран */
  void setPassXY(Float passX, Float passY);

  /** @return ширина дисплея OpenGL (в пикселах) */
  int getWidthDisplay();

  /** @return высота дисплея OpenGL (в пикселах) */
  int getHeightDisplay();

  /**
   * Проверяет загружена ли игра (проинициализированы все объекты игры)
   * @return true - если игра загружена, false - в обратном случае
   */
  boolean isLoadGame();




  enum TypeExplosion { ROCK, ICE, METAL }



  /* К каждому астероиду привязать взрыв */
  default void bindExplosions(Asteroid asteroid, List<Explosion> activeExplosions,
                              double versionGL, Context context) {
    if (asteroid instanceof BasaltAsteroid) {
      asteroid.setExplosion(createExplosion(TypeExplosion.ROCK, versionGL, context));
    } else if (asteroid instanceof MetalAsteroid) {
      asteroid.setExplosion(createExplosion(TypeExplosion.METAL, versionGL, context));
    } else {
      asteroid.setExplosion(createExplosion(TypeExplosion.ICE, versionGL, context));
    }
    asteroid.getExplosion().setExplosions(activeExplosions);
  }

  default Explosion createExplosion(TypeExplosion type, double versionGL, Context context) {
    switch (type) {
      case ROCK: return new Explosion(versionGL, context, "explosion/rock.png");
      case ICE: return new Explosion(versionGL, context, "explosion/ice.png", new float[] {0.1f, 0.4f, 1.0f, 1.0f}); // синий
      case METAL: return new Explosion(versionGL,context, "explosion/metal.png", new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      default: return null;
    }
  }

}

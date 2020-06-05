package alexrnov.cosmichunter.gles;

import android.content.Context;
import android.opengl.GLSurfaceView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import alexrnov.cosmichunter.activities.GameActivity;
import alexrnov.cosmichunter.gles.objects.Asteroid;
import alexrnov.cosmichunter.gles.objects.BasaltAsteroid;
import alexrnov.cosmichunter.gles.objects.Explosion;
import alexrnov.cosmichunter.gles.objects.ExplosionGLES20;
import alexrnov.cosmichunter.gles.objects.ExplosionGLES30;
import alexrnov.cosmichunter.gles.objects.IceAsteroid;
import alexrnov.cosmichunter.gles.objects.MetalAsteroid;
import alexrnov.cosmichunter.gles.objects.VulcanAsteroid;

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

  enum TypeExplosion { ROCK, ICE, METAL, VULCAN, GAS }

  /* К каждому астероиду привязать взрыв */
  default void bindExplosions(Asteroid asteroid, List<Explosion> activeExplosions,
                              double versionGL, Context context) {
    if (asteroid instanceof BasaltAsteroid) {
      asteroid.setExplosion(createExplosion(TypeExplosion.ROCK, versionGL, context));
    } else if (asteroid instanceof MetalAsteroid) {
      asteroid.setExplosion(createExplosion(TypeExplosion.METAL, versionGL, context));
    } else if (asteroid instanceof IceAsteroid) {
      asteroid.setExplosion(createExplosion(TypeExplosion.ICE, versionGL, context));
    } else if (asteroid instanceof VulcanAsteroid) {
      asteroid.setExplosion(createExplosion(TypeExplosion.VULCAN, versionGL, context));
    } else {
      asteroid.setExplosion(createExplosion(TypeExplosion.GAS, versionGL, context));
    }
    asteroid.getExplosion().setExplosions(activeExplosions);
  }

  default Explosion createExplosion(TypeExplosion type, double versionGL, Context context) {
    switch (type) {
      case ROCK:
        if (versionGL == 3.0) return new ExplosionGLES30(context, "explosion/rock.png");
        else return new ExplosionGLES20(context, "explosion/rock.png");
      case ICE:
        if (versionGL == 3.0) return new ExplosionGLES30(context, "explosion/ice.png", new float[] {0.1f, 0.4f, 1.0f, 1.0f}); // синий
        else return new ExplosionGLES20(context, "explosion/ice.png", new float[] {0.1f, 0.4f, 1.0f, 1.0f}); // синий
      case METAL:
        if (versionGL == 3.0) return new ExplosionGLES30(context, "explosion/metal.png", new float[] {0.3f, 1.0f, 0.3f, 1.0f});
        else return new ExplosionGLES20(context, "explosion/metal.png", new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      case VULCAN:
        if (versionGL == 3.0) return new ExplosionGLES30(context, "explosion/vulcan.png", new float[] {1.0f, 0.5f, 0.1f, 1.0f});
        else return new ExplosionGLES20(context, "explosion/vulcan.png", new float[] {1.0f, 0.5f, 0.1f, 1.0f});
      case GAS:
        if (versionGL == 3.0) return new ExplosionGLES30(context, "explosion/ice.png", new float[] {0.1f, 0.7f, 0.9f, 1.0f});
        else return new ExplosionGLES20(context, "explosion/ice.png", new float[] {0.1f, 0.7f, 0.9f, 1.0f});
      default: return null;
    }
  }
}

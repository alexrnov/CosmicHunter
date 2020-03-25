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




  enum TypeExplosion { ROCK_BIG, ROCK_MIDDLE, ROCK_SMALL, ICE_BIG,
    ICE_MIDDLE, ICE_SMALL, METAL_BIG, METAL_MIDDLE, METAL_SMALL }



  /* К каждому астероиду привязать по три взрыва (большой, средний, маленький) */
  default void bindExplosions(Asteroid asteroid, List<Explosion> activeExplosions,
                              double versionGL, Context context) {
    if (asteroid instanceof BasaltAsteroid) {
      asteroid.setBigExplosion(createExplosion(TypeExplosion.ROCK_BIG, versionGL, context));
      asteroid.setMiddleExplosion(createExplosion(TypeExplosion.ROCK_MIDDLE, versionGL, context));
      asteroid.setSmallExplosion(createExplosion(TypeExplosion.ROCK_SMALL, versionGL, context));
    } else if (asteroid instanceof MetalAsteroid) {
      asteroid.setBigExplosion(createExplosion(TypeExplosion.METAL_BIG, versionGL, context));
      asteroid.setMiddleExplosion(createExplosion(TypeExplosion.METAL_MIDDLE, versionGL, context));
      asteroid.setSmallExplosion(createExplosion(TypeExplosion.METAL_SMALL, versionGL, context));
    } else {
      asteroid.setBigExplosion(createExplosion(TypeExplosion.ICE_BIG, versionGL, context));
      asteroid.setMiddleExplosion(createExplosion(TypeExplosion.ICE_MIDDLE, versionGL, context));
      asteroid.setSmallExplosion(createExplosion(TypeExplosion.ICE_SMALL, versionGL, context));
    }
    asteroid.getBigExplosion().setExplosions(activeExplosions);
    asteroid.getMiddleExplosion().setExplosions(activeExplosions);
    asteroid.getSmallExplosion().setExplosions(activeExplosions);
  }

  default Explosion createExplosion(TypeExplosion type, double versionGL, Context context) {
    switch (type) {
      case ROCK_BIG:
        return new Explosion(versionGL, context, "explosion/rock.png");
      case ROCK_MIDDLE:
        return new Explosion(versionGL, context, "explosion/rock.png", 0.001f,
                0.4f, 80.0f, 120, new float[] {1.0f, 0.7f, 0.1f, 1.0f});
      case ROCK_SMALL:
        return new Explosion(versionGL, context, "explosion/rock.png", 0.005f,
                0.2f, 60.0f, 110, new float[] {1.0f, 0.7f, 0.1f, 1.0f});
      case ICE_BIG:
        return new Explosion(versionGL, context, "explosion/ice.png", 0.05f,
                0.6f, 100.0f, 150, new float[] {0.1f, 0.4f, 1.0f, 1.0f}); // синий
      case ICE_MIDDLE:
        return new Explosion(versionGL, context, "explosion/ice.png", 0.001f,
                0.4f, 80.0f, 120, new float[] {0.1f, 0.4f, 1.0f, 1.0f});
      case ICE_SMALL:
        return new Explosion(versionGL, context, "explosion/ice.png", 0.005f,
                0.2f, 60.0f, 110, new float[] {0.1f, 0.4f, 1.0f, 1.0f});
      case METAL_BIG:
        return new Explosion(versionGL,context, "explosion/metal.png", 0.05f,
                0.6f, 100.0f, 150, new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      case METAL_MIDDLE:
        return new Explosion(versionGL, context, "explosion/metal.png", 0.001f,
                0.4f, 80.0f, 120, new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      case METAL_SMALL:
        return new Explosion(versionGL, context, "explosion/metal.png", 0.005f,
                0.2f, 60.0f, 110, new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      default:
        return null;
    }
  }


  }

package alexrnov.cosmichunter;

import alexrnov.cosmichunter.view.RocketView3D;

/**
 * Интерфейс введен для случая, когда класс-наследник View3D работает
 * c экземпляром RocketGLES30 для разных версий OpenGL
 */
public interface Rocket {
  RocketView3D getView();
  void draw();
}

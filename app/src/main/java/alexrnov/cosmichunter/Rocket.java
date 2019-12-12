package alexrnov.cosmichunter;

import alexrnov.cosmichunter.view.RocketView3D;

/**
 * Интерфейс введен для случая, когда класс-наследник View3D работает
 * c экземпляром RocketObject3D для разных версий OpenGL
 */
public interface Rocket {
  RocketView3D getView();
  void draw();
}

package alexrnov.cosmichunter.gles.objects;

import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.View3D;

public interface Asteroid {
  AsteroidView3D getView();
  void setView(View3D view);

  void setExplosion(Explosion explosion);
  Explosion getExplosion();

  void draw();
}

package alexrnov.cosmichunter.objects;

import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.View3D;

public interface Asteroid {
  AsteroidView3D getView();
  void setView(View3D view);

  void setBigExplosion(Explosion explosion);
  void setMiddleExplosion(Explosion explosion);
  void setSmallExplosion(Explosion explosion);

  Explosion getBigExplosion();
  Explosion getMiddleExplosion();
  Explosion getSmallExplosion();

  void draw();
}

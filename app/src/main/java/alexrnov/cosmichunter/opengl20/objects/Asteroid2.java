package alexrnov.cosmichunter.opengl20.objects;

import alexrnov.cosmichunter.objects.Explosion;
import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.View3D;

public interface Asteroid2 {
  AsteroidView3D getView();
  void setView(View3D view);

  void setBigExplosion(Explosion2 explosion);
  void setMiddleExplosion(Explosion2 explosion);
  void setSmallExplosion(Explosion2 explosion);

  Explosion2 getBigExplosion();
  Explosion2 getMiddleExplosion();
  Explosion2 getSmallExplosion();

  void draw();
}

package alexrnov.cosmichunter.gles30.objects;

import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.View3D;

public interface AsteroidGLES30 {
  AsteroidView3D getView();
  void setView(View3D view);

  void setBigExplosion(ExplosionGLES30 explosion);
  void setMiddleExplosion(ExplosionGLES30 explosion);
  void setSmallExplosion(ExplosionGLES30 explosion);

  ExplosionGLES30 getBigExplosion();
  ExplosionGLES30 getMiddleExplosion();
  ExplosionGLES30 getSmallExplosion();

  void draw();
}

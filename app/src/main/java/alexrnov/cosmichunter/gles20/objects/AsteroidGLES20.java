package alexrnov.cosmichunter.gles20.objects;

import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.View3D;

public interface AsteroidGLES20 {
  AsteroidView3D getView();
  void setView(View3D view);

  void setBigExplosion(ExplosionGLES20 explosion);
  void setMiddleExplosion(ExplosionGLES20 explosion);
  void setSmallExplosion(ExplosionGLES20 explosion);

  ExplosionGLES20 getBigExplosion();
  ExplosionGLES20 getMiddleExplosion();
  ExplosionGLES20 getSmallExplosion();

  void draw();
}

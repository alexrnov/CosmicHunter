package alexrnov.cosmichunter;

import alexrnov.cosmichunter.view.RocketView3D;

public interface Rocket {
  RocketView3D getView();
  void draw();
}

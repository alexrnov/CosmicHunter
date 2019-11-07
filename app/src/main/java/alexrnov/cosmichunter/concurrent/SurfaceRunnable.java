package alexrnov.cosmichunter.concurrent;

import android.opengl.GLSurfaceView;
import android.util.Log;

import alexrnov.cosmichunter.SurfaceView;

public class SurfaceRunnable implements Runnable {

  private GLSurfaceView surfaceView;

  public SurfaceRunnable(GLSurfaceView surfaceView) {
    this.surfaceView = surfaceView;
  }

  @Override
  public void run() {
    while(true) {
      //Log.v("P", "run = " + Thread.currentThread().getName());
      surfaceView.requestRender();
      if (Thread.currentThread().isInterrupted()) {
        Log.v("P", "Thread stop = " + Thread.currentThread().getName());
        break;
      }
    }
  }
}

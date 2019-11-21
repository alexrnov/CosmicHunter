package alexrnov.cosmichunter.concurrent;

import android.opengl.GLSurfaceView;
import android.util.Log;

import static alexrnov.cosmichunter.Initialization.TAG;

public class SurfaceRunnable implements Runnable {

  private GLSurfaceView surfaceView;

  public SurfaceRunnable(GLSurfaceView surfaceView) {
    this.surfaceView = surfaceView;
  }

  @Override
  public void run() {
    while(true) {
      //Log.i(TAG, "run = " + Thread.currentThread().getName());
      surfaceView.requestRender();
      if (Thread.currentThread().isInterrupted()) {
        Log.v("P", "Thread stop = " + Thread.currentThread().getName());
        break;
      }
    }
  }
}

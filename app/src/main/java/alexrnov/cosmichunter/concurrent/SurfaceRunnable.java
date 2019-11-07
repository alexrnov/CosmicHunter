package alexrnov.cosmichunter.concurrent;

import android.util.Log;

import alexrnov.cosmichunter.SurfaceView;

public class SurfaceRunnable implements Runnable {

  private SurfaceView surfaceView;

  public SurfaceRunnable(SurfaceView surfaceView) {
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

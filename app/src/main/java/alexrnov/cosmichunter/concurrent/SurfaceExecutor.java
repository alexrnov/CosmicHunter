package alexrnov.cosmichunter.concurrent;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static alexrnov.cosmichunter.Initialization.TAG;

public class SurfaceExecutor implements Executor {

  private List<Thread> threads = new ArrayList<>();

  @Override
  public void execute(@NonNull Runnable runnable) {
    Thread thread = new Thread(runnable);
    thread.setPriority(10);
    thread.start();
    threads.add(thread);
  }

  public void interrupt() {
    for (Thread t: threads) {
      t.interrupt();
    }
    threads.clear();
  }
}

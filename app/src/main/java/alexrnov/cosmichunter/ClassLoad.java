package alexrnov.cosmichunter;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static alexrnov.cosmichunter.Initialization.TAG;

public class ClassLoad extends AsyncTask<Void, Void, Void> {

  private WeakReference<ConstraintLayout> bPanel;
  private WeakReference<AppCompatActivity> activity;

  public ClassLoad(AppCompatActivity activity, ConstraintLayout bPanel) {
    this.activity = new WeakReference<>(activity);
    this.bPanel = new WeakReference<>(bPanel);
  }

  @Override
  protected void onPreExecute() {
    Log.i(TAG, "ONPREEXECUTE()");
    bPanel.get().setVisibility(View.VISIBLE);
  }

  @Override
  protected Void doInBackground(Void... voids) {
    activity.get().runOnUiThread(() -> {
      // AnimationDrawable animation = (AnimationDrawable) loadImage.getBackground();
      // animation.start(); // выполнить анимацию процесса загрузки
    });
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    //bPanel.setVisibility(View.GONE);
  }
}

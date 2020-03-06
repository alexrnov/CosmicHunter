package alexrnov.cosmichunter;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import static alexrnov.cosmichunter.Initialization.TAG;

public class ClassLoad extends AsyncTask<Void, Void, Void> {

  private ConstraintLayout bPanel;
  private AppCompatActivity activity;
  public ClassLoad(AppCompatActivity activity, ConstraintLayout bPanel) {
    this.activity = activity;
    this.bPanel = bPanel;
  }

  @Override
  protected void onPreExecute() {
    Log.i(TAG, "ONPREEXECUTE()");
    bPanel.setVisibility(View.VISIBLE);

  }

  @Override
  protected Void doInBackground(Void... voids) {
    activity.runOnUiThread(() -> {
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

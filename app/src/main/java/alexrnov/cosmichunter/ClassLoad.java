package alexrnov.cosmichunter;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ClassLoad extends AsyncTask<Void, Void, Void> {

  private WeakReference<ConstraintLayout> bPanel;
  private WeakReference<AppCompatActivity> activity;
  private WeakReference<ImageView> imageView;

  public ClassLoad(AppCompatActivity activity, ConstraintLayout bPanel, ImageView imageView) {
    this.activity = new WeakReference<>(activity);
    this.bPanel = new WeakReference<>(bPanel);
    this.imageView = new WeakReference<>(imageView);
  }

  @Override
  protected void onPreExecute() {
    bPanel.get().setVisibility(View.VISIBLE);
    bPanel.get().bringToFront();
    bPanel.get().requestLayout(); // чтобы работало на Android 4.1.1
  }

  @Override
  protected Void doInBackground(Void... voids) {
    activity.get().runOnUiThread(() -> {
      AnimationDrawable animation = (AnimationDrawable) imageView.get().getBackground();
      animation.start(); // выполнить анимацию процесса загрузки
    });
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    //bPanel.get().setVisibility(View.GONE);
  }
}

package alexrnov.cosmichunter;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Класс предназначен для отображения панели загрузки с надписями
 * и анимированным изображением загрузки. Нужен, поскольку начиная с
 * Android 7.0 API 24, главное меню отображается пока игра не загружена.
 * Поэтому приходится создавать асинхронную задачу, чтобы отобразить
 * окно загрузки в главном меню пока грузится игра.
 */
public class LoadingPanel extends AsyncTask<Void, Void, Void> {

  // weakReference - позволяет избежать утечки памяти
  private WeakReference<ConstraintLayout> loadPanel;
  private WeakReference<AppCompatActivity> mainActivity;
  private WeakReference<ImageView> loadImage;

  public LoadingPanel(AppCompatActivity mainActivity,
                      ConstraintLayout loadPanel,
                      ImageView loadImage) {
    this.mainActivity = new WeakReference<>(mainActivity);
    this.loadPanel = new WeakReference<>(loadPanel);
    this.loadImage = new WeakReference<>(loadImage);
  }

  @Override
  protected void onPreExecute() {
    loadPanel.get().setVisibility(View.VISIBLE);
    loadPanel.get().bringToFront(); // вывести панель загрузки на передний план
    loadPanel.get().requestLayout(); // чтобы работало на Android 4.1.1
  }

  @Override
  protected Void doInBackground(Void... voids) {
    mainActivity.get().runOnUiThread(() -> {
      AnimationDrawable animation = (AnimationDrawable) loadImage.get().getBackground();
      animation.start(); // выполнить анимацию процесса загрузки
    });
    return null;
  }

  @Override
  protected void onPostExecute(Void result) { }
}

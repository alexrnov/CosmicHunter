package alexrnov.cosmichunter.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import alexrnov.cosmichunter.Initialization;
import alexrnov.cosmichunter.R;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;

public class DialogCancelActivity extends Activity {

  private String className = this.getClass().getSimpleName() + ".class: ";

  //private SoundPool clickSound;
  //private SparseIntArray soundPoolMap = new SparseIntArray();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "DIALOG onCreate()");
    setContentView(R.layout.activity_dialog);
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    if (width < height) {
      getWindow().setLayout((int) (width * .8), (int) (height * .4));
    } else {
      getWindow().setLayout((int) (width * .5), (int) (height * .7));
    }

    /*
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      clickSound = new SoundPool.Builder()
              .setMaxStreams(1)
              .build();
    } else {
      clickSound = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
    }
    soundPoolMap.put(0, clickSound.load(this, R.raw.click_sound, 0));
     */
  }

  public void backToMainMenu(View view) {
    Initialization.clickSound.play(Initialization.soundPoolMap.get(0),
            1.0f, 1.0f, 0, 0, 1f);
    /*
    clickSound.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f);
    */
    startActivity(new Intent(this, MainActivity.class));

  }

  public void cancel(View view) {
    Initialization.clickSound.play(Initialization.soundPoolMap.get(0),
            1.0f, 1.0f, 0, 0, 1f);
    /*
    clickSound.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f);
    */
    spotFlagOpenDialogWindow(false);
    finish();
  }

  @Override
  public void onStop() {
    Log.i(TAG, className + "onStop()");
    super.onStop();
    finish();
  }

  @Override
  public void onDestroy() {
    Log.i(TAG, className + "onDestroy()");
    super.onDestroy();
  }

  /* нажатие на кнопку "назад", чтобы скрыть диалог */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == 0x00000004) { // KeyEvent.FLAG_KEEP_TOUCH_MODE; (API 3)
      Log.i(TAG, className + "onKeyDown(), keyCode = " + keyCode);
      spotFlagOpenDialogWindow(false);
      //finish();
    }
    return super.onKeyDown(keyCode, event);
  }

  // не реагировать на прикосновение пальцев, чтобы диалог не скрывался
  // когда нажимаешь на экран за пределами фрейма диалога
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return false;
  }

}

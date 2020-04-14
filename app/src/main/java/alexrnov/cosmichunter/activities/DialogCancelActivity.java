package alexrnov.cosmichunter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import alexrnov.cosmichunter.R;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;
import static alexrnov.cosmichunter.sound.ShortSounds.playClick;

public class DialogCancelActivity extends Activity {

  private String className = this.getClass().getSimpleName() + ".class: ";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i(TAG, "DIALOG onCreate()");
    setContentView(R.layout.activity_cancel_dialog);
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    if (width < height) {
      getWindow().setLayout((int) (width * .8), (int) (height * .4));
    } else {
      getWindow().setLayout((int) (width * .5), (int) (height * .7));
    }
  }

  public void backToMainMenu(View view) {
    playClick();

    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra("game", 1);
    startActivity(intent);
  }

  public void cancel(View view) {
    playClick();
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

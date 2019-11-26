package alexrnov.cosmichunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;

public class DialogActivity extends Activity {

  private String className = this.getClass().getSimpleName() + ".class: ";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

    //and watch for outside touch events too
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
    */
  }

  public void backToMainMenu(View view) {
    startActivity(new Intent(this, MainActivity.class));
  }

  public void cancel(View view) {
    spotFlagOpenDialogWindow(false);
    finish();
  }

  @Override
  public void onResume() {
    Log.i(TAG, className + "onResume()");
    super.onResume();
    //spotFlagOpenDialogWindow(true);
  }

  @Override
  public void onPause() {
    Log.i(TAG, className + "onPause()");
    super.onPause();
    //spotFlagOpenDialogWindow(false);
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

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
      Log.i(TAG, "onTouchEvent() = true");
      return true;
    } else {
      Log.i(TAG, "onTouchEvent() = false");
    }

    return false;
  }

}

package alexrnov.cosmichunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

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
  }

  public void backToMainMenu(View view) {
    startActivity(new Intent(this, MainActivity.class));
  }

  public void cancel(View view) {
    spotFlagOpenDialogWindow(false);
    finish();
  }

  @Override
  public void onStart() {
    Log.i(TAG, className + "onStart()");
    super.onStart();
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
    Log.i(TAG, className + "onKeyDown()");
    spotFlagOpenDialogWindow(false);
    //finish();
    return super.onKeyDown(keyCode, event);
  }
}

package alexrnov.cosmichunter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;
import static alexrnov.cosmichunter.Initialization.TAG;

public class LevelsActivity extends AppCompatActivity {

  private String className = this.getClass().getSimpleName() + ".class: ";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_level);
  }

  public void startLevel1(View view) {
    spotFlagOpenDialogWindow(false);
    Intent intent = new Intent(this, GameActivity.class);
    startActivity(intent);
  }

  public void backToMainMenu(View view) {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onStart() {
    Log.i(TAG, className + "onStart()");
    super.onStart();
    checkMusicForStartOtherActivity(this);
  }

  @Override
  protected void onResume() {
    Log.i(TAG, className + "onResume()");
    super.onResume();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, className + "onPause()");
    super.onPause();
  }

  @Override
  protected void onStop() {
    Log.i(TAG, className + "onStop()");
    super.onStop();
    checkMusicForStopOtherActivity();
  }
}

package alexrnov.cosmichunter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;

public class LevelsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
    super.onStart();
    checkMusicForStartOtherActivity(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    checkMusicForStopOtherActivity();
  }
}

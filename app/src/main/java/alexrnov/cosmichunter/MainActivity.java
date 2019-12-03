package alexrnov.cosmichunter;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartMainActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopMainActivity;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;
import static alexrnov.cosmichunter.Initialization.TAG;

public class MainActivity extends AppCompatActivity {

  private String className = this.getClass().getSimpleName() + ".class: ";

  @Override
  protected void onCreate(Bundle savedInstanceState) { //состояние "создано"
    Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  protected void onStart() { // состояние "запущено"
    Log.i(TAG, className + "onStart()");
    super.onStart();
    checkMusicForStartMainActivity(this);
  }

  @Override
  protected void onStop() { // состояние "остановлено"
    Log.i(TAG, className + "onStop()");
    super.onStop();
    checkMusicForStopMainActivity();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, className + "onPause()");
    super.onPause();
  }

  public void startGame(View view) {
    Intent intent = new Intent(this, GameActivity.class);
    startActivity(intent);
  }

  public void selectLevel(View view) {
    Intent intent = new Intent(this, LevelsActivity.class);
    startActivity(intent);
  }

  public void settingsMenu(View view) {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }

  /** выйти из приложения */
  public void exitFromApplication(View view) {
    backToHome();
  }

  /*
   * Если кнопка выхода нажата в главном меню, - выйти из приложения, а не
   * возвращаться к другим активити(в том числе к игровому активити).
   */
  @Override
  public void onBackPressed() {
    backToHome();
  }

  private void backToHome() {
    Intent intent = new Intent(Intent.ACTION_MAIN);
    intent.addCategory(Intent.CATEGORY_HOME);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent); // выйти на рабочий стол системы

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)  // Android 5.0 (API 21) and higher
      // завершить все активити в этой задаче и удалить их из списка "недавние" (recent)
      // работает правильно, если в манифесте launchMode="singleTask" а не "standard"
      finishAndRemoveTask();
     else
      finish();

    System.exit(0);

  }
}

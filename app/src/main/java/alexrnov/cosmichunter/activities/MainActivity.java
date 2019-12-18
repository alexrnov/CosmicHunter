package alexrnov.cosmichunter.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import alexrnov.cosmichunter.R;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartMainActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopMainActivity;
import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.printDPSizeScreen;

public class MainActivity extends AppCompatActivity {

  private String className = this.getClass().getSimpleName() + ".class: ";

  @Override
  protected void onCreate(Bundle savedInstanceState) { //состояние "создано"
    Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    printDPSizeScreen(this);
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
    //getToast();
    getSnackbar();
    /*
    int versionGLES = getSupportOpenGLES();
    if (versionGLES != 1) {
      Intent intent = new Intent(this, GameActivity.class);
      intent.putExtra("versionGLES", versionGLES);
      startActivity(intent);
    } else {

    }
    */
  }

  public void selectLevel(View view) {
    int versionGLES = getSupportOpenGLES();
    if (versionGLES != 1) {
      Intent intent = new Intent(this, LevelsActivity.class);
      intent.putExtra("versionGLES", versionGLES);
      startActivity(intent);
    } else {

    }
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

  // Проверка версии OpenGL на устройстве в рантайме. В манифесте объявляется
  // поддержка OpenGL 2, которая по умолчанию подразумевает поддержку OpenGL 2 и OpenGL 1.
  // Поскольку на самом деле, данное приложение поддерживает OpenGL 2 и OpenGL 3,
  // поддержиаемая версия проверяется в рантайме. Если поддерживается OpenGL 3, используется
  // третья версия, если поддерживается OpenGL 2, тогда используется вторая версия,
  // если поддерживается только версия OpenGL 1, тогда GameActivity или LevelsActivity не запускаются
  private int getSupportOpenGLES() {
    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    ConfigurationInfo info = am != null ? am.getDeviceConfigurationInfo() : null;
    if (info != null) {
      Double d = Double.parseDouble(info.getGlEsVersion());
      if (d >= 3.0) { // info.reqGlEsVersion >= 0x30000
        return 3;
      } else if (d >= 2.0) {
        return 2;
      } else {
        return 1;
      }
    } else return 1;
  }

  private void getToast() {
    LayoutInflater inflater = getLayoutInflater();
    //первый параметр - xml-файл с представлением, второй параметр
    //- корневой вьюер в этом файле
    View layout = inflater.inflate(R.layout.custom_toast,
            (ViewGroup) findViewById(R.id.custom_toast_container));

    TextView text = (TextView) layout.findViewById(R.id.text_toast);
    text.setText("OpenGL не поддерживается");
    Toast toast = new Toast(getApplicationContext());
    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 20);
    toast.setDuration(Toast.LENGTH_SHORT);
    toast.setView(layout);
    toast.show();
  }

  private void getSnackbar() {
    Log.i(TAG, "getSnckbar() 1, Build.VERSION = " + Build.VERSION.SDK_INT );
    CoordinatorLayout cr = findViewById(R.id.myCoordinatorLayout);
    Log.i(TAG, "getSnackbar() 2");
    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "text", 10);
    Log.i(TAG, "getSnackbar() 3");
    mySnackbar.show();
    Log.i(TAG, "getSnackbar() 4");
  }
}

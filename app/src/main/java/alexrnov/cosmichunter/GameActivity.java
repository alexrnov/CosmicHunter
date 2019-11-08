package alexrnov.cosmichunter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import alexrnov.cosmichunter.concurrent.SurfaceExecutor;
import alexrnov.cosmichunter.concurrent.SurfaceRunnable;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartGameActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopGameActivity;
import static alexrnov.cosmichunter.Initialization.sp;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;

public class GameActivity extends AppCompatActivity {
  private OGLView oglView;
  // private SurfaceView surfaceView; // используется в случае полноэкранного режима
  private SurfaceExecutor executor = new SurfaceExecutor();
  // флаг нужен для того, чтобы при возврате к приложению,
  // потоки не создавались два раза, так как из-за того, что
  // после возврата к приложению, выдается диалоговое окно,
  // методы жизненного цикла вызываются в следующем
  // порядке onResume(), onStop(), onResume(), что приводит к
  // лишнему циклу создания-установки потоков.
  private boolean createThreads = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.v("P", "invoke onCreate()");
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    if (detectOpenGLES30()) Log.v("P", "OpenGL ES 3.0 поддерживается на данном устройстве");
    else Log.v("P", "OpenGL ES 3.0 не поддерживается на данном устройстве");

    ActionBar ab = getSupportActionBar();
    if (ab != null) ab.hide(); //скрыть заголовок приложения
    /*
    // используется в случае полноэкранного режима
    //surfaceView = new SurfaceView(this);
    //setContentView(surfaceView);
    */
    // выводить рендер OpenGL в отдельном компоненте
    setContentView(R.layout.activity_gl); // загрузка ресурса XML
    oglView = findViewById(R.id.oglView);
  }

  private boolean detectOpenGLES30() {
    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    ConfigurationInfo info = null;
    if (am != null) {
      info = am.getDeviceConfigurationInfo();
    }
    return info != null && (info.reqGlEsVersion >= 0x3000);
  }

  @Override
  protected void onResume() {
    Log.v("P", "onResume()");
    super.onResume();
    if (createThreads) {
      //SurfaceRunnable sr = new SurfaceRunnable(surfaceView); // используется в случае полноэкранного режима
      SurfaceRunnable sr = new SurfaceRunnable(oglView);
      executor.execute(sr);
      executor.execute(sr);
      executor.execute(sr);
      executor.execute(sr);
    }
    //surfaceView.onResume();
  }

  @Override
  protected void onPause() {
    Log.v("P", "invoke onPause()");
    super.onPause();
    createThreads = true;
    executor.interrupt();
    //surfaceView.onPause();
  }

  @Override
  protected void onStart() {
    Log.v("P", "invoke onStart()");
    super.onStart();
    checkMusicForStartGameActivity(this);

    boolean dialogWasOpen = sp.getBoolean("dialog_open", false);
    if (dialogWasOpen) {
      createThreads = false;
      startActivity(new Intent(this, DialogActivity.class));
    }
  }

  @Override
  protected void onStop() {
    Log.v("P", "invoke onStop()");
    super.onStop();
    checkMusicForStopGameActivity();
    spotFlagOpenDialogWindow(true);
    executor.interrupt();
  }


  /* вызов метода не гарантирован */
  @Override
  protected void onDestroy() {
    Log.v("P", "invoke onDestroy()");
    super.onDestroy();

  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    Log.v("P", "invoke onSaveInstanceState()");
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == 0x00000004) { //KeyEvent.FLAG_KEEP_TOUCH_MODE; (API 3)
      startActivity(new Intent(this, DialogActivity.class));
    }
    return super.onKeyDown(keyCode, event);
  }
}

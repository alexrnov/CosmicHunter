package alexrnov.cosmichunter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import alexrnov.cosmichunter.concurrent.SurfaceExecutor;
import alexrnov.cosmichunter.concurrent.SurfaceRunnable;
import alexrnov.cosmichunter.concurrent.ViewHandler;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartGameActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopGameActivity;
import static alexrnov.cosmichunter.Initialization.sp;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;

import static alexrnov.cosmichunter.Initialization.TAG;
public class GameActivity extends AppCompatActivity {
  private OGLView oglView;
  private Button buttonGL;
  //private SurfaceView surfaceView; // используется в случае полноэкранного режима
  private SurfaceExecutor executor = new SurfaceExecutor();
  // флаг нужен для того, чтобы при возврате к приложению,
  // потоки не создавались два раза, так как из-за того, что
  // после возврата к приложению, выдается диалоговое окно,
  // методы жизненного цикла вызываются в следующем
  // порядке onResume(), onStop(), onResume(), что приводит к
  // лишнему циклу создания-установки потоков.
  private boolean createThreads = true;
  private Handler handler;
  private Handler handler2;
  private String className = this.getClass().getSimpleName() + ".class: ";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    if (detectOpenGLES30()) Log.v(TAG, className + "OpenGL ES 3.0 поддерживается на данном устройстве");
    else Log.v(TAG, className + "OpenGL ES 3.0 не поддерживается на данном устройстве");

    /*
    // hide the status bar
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
      Log.v("P", "VERSION < 16");
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else { // Android 4.1 и выше. Не работает так как надо, на смартфоне sony (android 5), status bar не скрывается,
      //а на samsung планшете (android 7 status bar скрывается с запозданием)
      Log.v("P", "VERSION >= 16");
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
      decorView.setSystemUiVisibility(uiOptions);
    }
    */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    ActionBar ab = getSupportActionBar();
    if (ab != null) ab.hide(); //скрыть заголовок приложения


    // используется в случае полноэкранного режима
    //surfaceView = new SurfaceView(this);
    //setContentView(surfaceView);


    // выводить рендер OpenGL в отдельном компоненте
    setContentView(R.layout.activity_gl); // загрузка ресурса XML
    oglView = findViewById(R.id.oglView);
    TextView hits = findViewById(R.id.hits);
    TextView rockets = findViewById(R.id.rockets);
    TextView message = findViewById(R.id.message);
    TextView time = findViewById(R.id.time);
    // определяет объект handler, присоединенный к потоку пользовательского интерфейса
    handler = new ViewHandler(Looper.getMainLooper(), hits, rockets, message, time);
    oglView.setGameActivity(this); // передать ссылку на GameActivity объекту oglView и далее объекту SceneRenderer

    handler2 = new Handler(Looper.getMainLooper()) {

      @Override
      public void handleMessage(Message inputMessage) {

      }
    };
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
    Log.i(TAG, className + "onResume()");
    super.onResume();
    //if (createThreads) {
      //SurfaceRunnable sr = new SurfaceRunnable(surfaceView); // используется в случае полноэкранного режима
      SurfaceRunnable sr = new SurfaceRunnable(oglView);
      executor.execute(sr);
      executor.execute(sr);
      executor.execute(sr);
      executor.execute(sr);
    //}
    //surfaceView.onResume();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, className + "onPause()");
    super.onPause();
    createThreads = true;
    executor.interrupt();
    //surfaceView.onPause();
  }

  @Override
  protected void onStart() {
    Log.i(TAG, className + "onStart()");
    super.onStart();
    checkMusicForStartGameActivity(this);

    boolean dialogWasOpen = sp.getBoolean("dialog_open", false);
    if (dialogWasOpen) {
      Log.i(TAG, "startActivity");
      createThreads = false;
      startActivity(new Intent(this, DialogActivity.class));
    }
  }

  @Override
  protected void onStop() {
    Log.i(TAG, className + "onStop()");
    super.onStop();
    checkMusicForStopGameActivity();
    //spotFlagOpenDialogWindow(true);
    executor.interrupt();
  }


  /* вызов метода не гарантирован */
  @Override
  protected void onDestroy() {
    Log.i(TAG, className + "onDestroy()");
    super.onDestroy();
  }


  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    Log.i(TAG, className + "onSaveInstanceState()");
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == 0x00000004) { //KeyEvent.FLAG_KEEP_TOUCH_MODE; (API 3)
      startActivity(new Intent(this, DialogActivity.class));
    }
    return super.onKeyDown(keyCode, event);
  }

  public void handleState(int state, String s) {
    Message completeMessage = handler.obtainMessage(state, s);
    completeMessage.sendToTarget();
  }
}

package alexrnov.cosmichunter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import alexrnov.cosmichunter.concurrent.SurfaceExecutor;
import alexrnov.cosmichunter.concurrent.SurfaceRunnable;
import alexrnov.cosmichunter.concurrent.ViewHandler;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartGameActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopGameActivity;
import static alexrnov.cosmichunter.Initialization.sp;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.concurrent.ViewHandlerKt.ROCKETS_CODE;
import static alexrnov.cosmichunter.concurrent.ViewHandlerKt.TIME_CODE;

public class GameActivity extends AppCompatActivity {
  private OGLView oglView; // используется в случае вывода рендера в отдельный компонент интерфейса
  //private SurfaceView surfaceView; // используется в случае полноэкранного режима
  private SurfaceExecutor executor = new SurfaceExecutor();
  private Handler handler;
  private String className = this.getClass().getSimpleName() + ".class: ";
  private Timer timer;
  private int time = 600;
  private View decorView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    // необходимо  в случае если приложение будет разрушено и опять будет
    // вызван метод onCreate(). Если флаг не сбрость, то если ранее был открыт
    // диалог, тогда возникнет ошибка, поскольку потоки рендера не будут созданы
    spotFlagOpenDialogWindow(false);
    // ориентация экрана определяется в файле манифеста, а не в коде -
    // это позволяет избежать повторной перезагрузки активити.Кроме того,
    // не нужно создавать лэйаут для портретной ориентации, поскольку
    // в начале будет загружаться именно он
    //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    // дополнительная проверка поддержки OpenGL на устройстве в рантайме
    if (detectOpenGLES30()) Log.i(TAG, className + "OpenGL ES 3.0 поддерживается на данном устройстве");
    else Log.i(TAG, className + "OpenGL ES 3.0 не поддерживается на данном устройстве");

    /*
    // hide the status bar
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
      Log.v("P", "VERSION < 16");
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } else { // Android 4.1 и выше. Не работает так как надо, на смартфоне sony (android 5), status bar не скрывается,
      // а на samsung планшете (android 7 status bar скрывается с запозданием)
      Log.v("P", "VERSION >= 16");
      View decorView = getWindow().getDecorView();
      int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
      decorView.setSystemUiVisibility(uiOptions);
    }
    */

    // скрыть строку статуса
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    //скрыть заголовок приложения
    ActionBar ab = getSupportActionBar();
    if (ab != null) ab.hide();

    // используется в случае полноэкранного режима
    //surfaceView = new SurfaceView(this);
    //setContentView(surfaceView);

    // выводить рендер OpenGL в отдельном компоненте
    setContentView(R.layout.activity_gl); // загрузка ресурса XML
    oglView = findViewById(R.id.oglView);
    oglView.setGameActivity(this); // передать ссылку на GameActivity объекту oglView и далее объекту SceneRendererGLES30

    TextView hits = findViewById(R.id.hits);
    TextView rockets = findViewById(R.id.rockets);
    TextView message = findViewById(R.id.message);
    TextView time = findViewById(R.id.time);

    bringViewsToFront(hits, rockets, message, time);
    // определяет объект handler, присоединенный к потоку пользовательского интерфейса
    handler = new ViewHandler(Looper.getMainLooper(), hits, rockets, message, time);

    decorView = getWindow().getDecorView();


    decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
      @Override
      public void onSystemUiVisibilityChange(int visibility) {
        Log.i(TAG, "decorView, visibility = " + visibility);
        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
          int ioOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_IMMERSIVE
                  // navigation bar распологается поверх контента, чтобы при
                  // появлении, размер контента не менялся (Android 4.1 и выше)
                  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
          decorView.setSystemUiVisibility(ioOptions);
        }
      }
    });


    int ioOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

    decorView.setSystemUiVisibility(ioOptions);

    boolean hasMenuKey = ViewConfiguration.get(this.getBaseContext()).hasPermanentMenuKey();
    boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
    // проверить имеет ли девайс навигационную панель
    if (!hasMenuKey && !hasBackKey) { // если нет кнопок - тогда панель рисуется на экране
      Log.i(TAG, "the device has a navigation bar");
    } else {
      Log.i(TAG, "the device has not a navigation bar");
    }
  }

  private boolean detectOpenGLES30() {
    ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    ConfigurationInfo info = null;
    if (am != null) {
      info = am.getDeviceConfigurationInfo();
    }
    Log.i(TAG, "Double version = " + Double.parseDouble(info.getGlEsVersion()));
    return info != null && (info.reqGlEsVersion >= 0x30000);
  }

  @Override
  protected void onResume() {
    Log.i(TAG, className + "onResume()");
    super.onResume();
    /*
     * Проверка нужна для того, чтобы при возврате к приложению, когда открыто
     * диалоговое окно, не происходил лишний цикл создания-остановки потоков,
     * поскольку методы жизненного цикла GameActivity будут вызываться в следующем
     * порядке onResume(), onStop(), onResume(). При этом, даже если потоки не запускаются,
     * реализация openGL все-равно сделает один кадр, поэтому при возвращении к
     * приложению, объекты будут немного смещаться
     */
    boolean dialogWasOpen = sp.getBoolean("dialog_open", false);
    Log.i(TAG, "dialogWasOpen = " + dialogWasOpen);
    if (!dialogWasOpen) {
      Log.i(TAG, className + "threads run");
      //SurfaceRunnable sr = new SurfaceRunnable(surfaceView); // используется в случае полноэкранного режима
      SurfaceRunnable sr = new SurfaceRunnable(oglView);
      executor.execute(sr); // загрузить рендером четыре ядра
      executor.execute(sr);
      executor.execute(sr);
      executor.execute(sr);

      // запустить отдельный поток для таймера
      timer = new Timer(true); // true - запустить поток как демон
      timer.schedule(new TimerTask() {
        @Override
        public void run() {
          int min = time / 60; // получить количество минут
          int sec = time % 60; // получить количество секунд
          // для создания формата времени 00:00, вместо String.format()
          // используется тернарный оператор (в целях производительности)
          handleState(TIME_CODE, ((min < 10) ? "0" : "") + min + ":" + ((sec < 10) ? "0" : "") + sec);
          //String minS = String.format("%02d", min);
          //String secS = String.format("%02d", sec);
          time--;
        }
      }, 1000, 100); // delay = 1000, чтобы после возврата к приложению время сразу не уменьшалось на секунду
    }

    // используется в различных примерах, но эффект от этого метода не определил
    //surfaceView.onResume(); // полноэкранный режим
    //oglView.onResume(); // рендер в  компонент
  }

  @Override
  protected void onPause() {
    Log.i(TAG, className + "onPause()");
    super.onPause();
    executor.interrupt(); // остановить рендер
    timer.cancel(); // остановить время
    // используется в различных примерах, но эффект от этого метода не определил
    //surfaceView.onPause(); // полноэкранный режим
    //oglView.onPause(); // рендер в компонент
  }

  @Override
  protected void onStart() {
    Log.i(TAG, className + "onStart()");
    super.onStart();
    checkMusicForStartGameActivity(this);

    boolean dialogWasOpen = sp.getBoolean("dialog_open", false);
    if (dialogWasOpen) {
      startActivity(new Intent(this, DialogCancelActivity.class));
    }
  }

  @Override
  protected void onStop() {
    Log.i(TAG, className + "onStop()");
    super.onStop();
    checkMusicForStopGameActivity();
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

  // нажатие на андроид-кнопку "назад"
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == 0x00000004) { // KeyEvent.FLAG_KEEP_TOUCH_MODE; (API 3)
      Log.i(TAG, className + "onKeyDown()");
      startActivity(new Intent(this, DialogCancelActivity.class));
      spotFlagOpenDialogWindow(true);
    }
    return super.onKeyDown(keyCode, event);
  }

  /**
   * Принимает сообщения из других потоков и передает их обработчику,
   * который управляет отображением элементов интерфейса
   * @param state - код сообщения
   * @param message - сообщение
   */
  public synchronized void handleState(int state, String message) {
    if (state == ROCKETS_CODE && Integer.valueOf(message) == 0) {
      Log.i(TAG, "message = " + message);
      startActivity(new Intent(this, DialogCancelActivity.class));
    }

    if (state == TIME_CODE && message.equals("00:00")) {
      startActivity(new Intent(this, DialogCancelActivity.class));
    }

    Message completeMessage = handler.obtainMessage(state, message);
    completeMessage.sendToTarget();
  }

  /*
  * Перемещает элементы интерфейса на передний план - чтобы они были
  * размещены перед слоем openGL. Вообще эти элементы и так размещаются
  * впереди, поскольку в xml-файле они объявляются после слоя openGL,
  * но здесь эти элементы перемещаются на передний план программно для
  * дополнительной подстраховки
  */
  private void bringViewsToFront(View... views) {
    for (View view: views) {
      view.bringToFront();
      view.requestLayout(); // чтобы работало на Android 4.1.1
    }
  }

  /*
  @Override
  public boolean onTouchEvent(MotionEvent event) {

    decorView = getWindow().getDecorView();
    int ioOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE
            // navigation bar распологается поверх контента, чтобы при
            // появлении, размер контента не менялся (Android 4.1 и выше)
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

    decorView.setSystemUiVisibility(ioOptions);
    Log.i(TAG, "onTouchEvent()");

    return true;
  }
  */

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    Log.i("P", "hasFocus = " + hasFocus);
    super.onWindowFocusChanged(hasFocus);
  }
}

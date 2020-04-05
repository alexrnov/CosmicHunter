package alexrnov.cosmichunter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.SparseIntArray;

import java.util.List;
import java.util.concurrent.Executors;

import alexrnov.cosmichunter.base.Level;
import alexrnov.cosmichunter.base.LevelDao;
import alexrnov.cosmichunter.base.LevelDatabase;
import alexrnov.cosmichunter.sound.BackgroundMusic;
import alexrnov.cosmichunter.sound.ShortSounds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Объект класса создается при запуске приложения. Содержит статические
 * методы для управления проигрыванием фоновой музыки в приложении.
 */
public class Initialization extends Application {
  public static final String TAG = "Application";
  public static SharedPreferences sp;
  private static final String MAIN_ACTIVITY = "main_activity";
  private static final String OTHER_ACTIVITY = "other_activity";
  private static final String GAME_ACTIVITY = "game_activity";

  private static String defaultStateMusic;
  private static String defaultVibration;

  /*
  public static SoundPool clickSound;
  public static SoundPool explosionSound;
  public static SoundPool gunSound;
  // SparseIntArray дает лучшую производительность по сравнению с
  // HashMap<Integer, Integer>()
  public static SparseIntArray soundPoolMap = new SparseIntArray();
   */

  private LevelDatabase dbLevels;

  @Override
  public void onCreate() {
    super.onCreate();

    ShortSounds.init(this);

    final String packageName = this.getApplicationContext().getPackageName();
    sp = this.getSharedPreferences(packageName, MODE_PRIVATE);

    defaultStateMusic = getResources().getString(R.string.default_music);
    defaultVibration = getResources().getString(R.string.default_vibration);
    //сбросить логические переменные для управления музыкой при
    //навигации по активити в исходное положение. Это необходимо,
    //поскольку с прошлого запуска значения переменных могут остаться
    //в измененном состоянии
    openingActivity(MAIN_ACTIVITY, false);
    openingActivity(OTHER_ACTIVITY, false);
    openingActivity(GAME_ACTIVITY, false);

    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
    spotStartVolumeLevel(am);

    AsyncTask.execute(() -> {
      // .allowMainThreadQueries() - разрешить создавать БД в потоке пользовательского интерфейса
      // val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
      dbLevels = Room.databaseBuilder(this.getApplicationContext(), LevelDatabase.class, "levels-database").addCallback(dbCallback).build();
      LevelDao dao = dbLevels.levelDao();
      int size = dao.getAll().size(); // фактически база будет создана при этой инструкции

      if (size != 0) {
        List<Level> levels = dao.getAll();
        for (Level level: levels) Log.i(TAG, "id = " + level.id + ", number = " + level.number + ", levelName = "
                + level.levelName + ", isOpen = " + level.isOpen + ";");
      }
    });
  }

  /**
   * Метод проверяет нужно ли включать музыку при запуске main activity
   * Если метод onStart() активити main вызван из неглавного активити
   * - не выключать музыку; если вызван из игрового активити - остановить
   * музыку игры, и включить музыку для меню. Если метод onStart()
   * активити main вызван после отановки приложения - включить музыку.
   * @param activity ссылка на main activity
   */
  public static void checkMusicForStartMainActivity(AppCompatActivity activity) {
    if (isMusicOff()) {
      return;
    }

    openingActivity(MAIN_ACTIVITY, true);
    boolean returnFromGameActivity = sp.getBoolean(GAME_ACTIVITY, false);
    if (returnFromGameActivity) {
      BackgroundMusic.freeResourcesForPlayer();
      BackgroundMusic.createMenuPlayer(activity);
      openingActivity(GAME_ACTIVITY, false);
      return;
    }

    boolean returnFromOtherActivity = sp.getBoolean(OTHER_ACTIVITY, false);
    if (!returnFromOtherActivity) {
      BackgroundMusic.createMenuPlayer(activity);
    } else {
      openingActivity(OTHER_ACTIVITY, false);
    }
  }

  /**
   * Метод проверяет нужно ли выключать музыку при остановке main activity.
   * Если метод onStop() активити main вызван после метода onStart()
   * неглавного акивити(что значит, что открывается неглавное активити)
   * - не выключать музыку. Если метод onStop() активити main вызван
   * после метода onStart() игрового активити(что значит, что открывается
   * игровое активити), ничего не делать, т.к. остановка и запуск плеера
   * производятся в методе onStart() game activity. Если метод onStop()
   * активити main вызван при остановке приложения - выключить музыку.
   */
  public static void checkMusicForStopMainActivity() {
    if (isMusicOff()) {
      return;
    }
    boolean processOpeningGameActivity = sp.getBoolean(GAME_ACTIVITY, false);
    //если открывается game activity ничего не делать
    if (processOpeningGameActivity) {
      return;
    }
    boolean processOpeningOtherActivity = sp.getBoolean(OTHER_ACTIVITY, false);
    if (!processOpeningOtherActivity) {
      BackgroundMusic.freeResourcesForPlayer();
    }
  }

  /**
   * Метод проверяет нужно ли включать музыку при запуске неглавного activity.
   * Если метод onStart() неглавного активити вызван из main активити
   * - не выключать музыку; если вызван из игрового активити - остановить
   * музыку игры и включить музыку для меню. Если метод onStart() неглавного
   * main вызван после отановки приложения - включить музыку.
   * @param activity ссылка на неглавный activity
   */
  public static void checkMusicForStartOtherActivity(AppCompatActivity activity) {
    if (isMusicOff()) {
      return;
    }
    openingActivity(OTHER_ACTIVITY, true);
    boolean returnFromGameActivity = sp.getBoolean(GAME_ACTIVITY, false);
    if (returnFromGameActivity) {
      BackgroundMusic.freeResourcesForPlayer();
      BackgroundMusic.createMenuPlayer(activity);
      openingActivity(GAME_ACTIVITY, false);
      return;
    }
    boolean returnFromMainActivity = sp.getBoolean(MAIN_ACTIVITY, false);
    if (!returnFromMainActivity) {
      BackgroundMusic.createMenuPlayer(activity);
    } else {
      openingActivity(MAIN_ACTIVITY,false);
    }
  }

  /**
   * Метод проверяет нужно ли выключать музыку при остановке неглавного
   * activity. Если метод onStop() неглавного активити вызван после
   * метода onStart() главного активити (что значит, что осуществляется
   * возврат к главному активити) - не выключать музыку. Если метод onStop()
   * неглавного активити вызван при остановке приложения - выключить музыку.
   */
  public static void checkMusicForStopOtherActivity() {
    if (isMusicOff()) {
      return;
    }
    boolean processOpeningGameActivity = sp.getBoolean(GAME_ACTIVITY, false);
    //если открывается game activity ничего не делать, т.к. остановка
    //и запуск плеера производятся в методе onStart() game activity
    if (processOpeningGameActivity) {
      return;
    }

    boolean processOpeningMainActivity = sp.getBoolean(MAIN_ACTIVITY, false);
    if (!processOpeningMainActivity) {
      BackgroundMusic.freeResourcesForPlayer();
    }
  }

  /**
   * Метод управляет запуском музыки в игровом activity.
   * Если метод onStart() игрового активити вызван из main активити
   * или other activity - остановить музыку, играющую в меню.
   * Метод при любом условии(приход из другого активити,
   * возвращение в приложение) включает музыку для игры.
   * @param activity ссылка на неглавный activity
   */
  public static void checkMusicForStartGameActivity(AppCompatActivity activity, int levelNumber) {
    if (isMusicOff()) {
      return;
    }
    openingActivity(GAME_ACTIVITY, true);

    boolean returnFromMainActivity = sp.getBoolean(MAIN_ACTIVITY, false);
    if (returnFromMainActivity) { //если игра вызвана из главного активити
      openingActivity(MAIN_ACTIVITY,false);
      BackgroundMusic.freeResourcesForPlayer();
    } else { //если игра вызвана из неглавного активити(select level)
      boolean returnFromOtherActivity = sp.getBoolean(OTHER_ACTIVITY, false);
      if (returnFromOtherActivity) {
        openingActivity(OTHER_ACTIVITY, false);
        BackgroundMusic.freeResourcesForPlayer();
      }
    }
    //при любом условии включить музыку для игры
    BackgroundMusic.createGamePlayer(activity, levelNumber);
  }

  /**
   * Управляет проигрыванием музыки при остановке игры. Если открывается
   * main активити или other activity(select level) ничего не делать,
   * т.к. остаовка и запуск плеера производятся в методе onStart()
   * main activity или other activity. Если метод запускается при
   * остановке приложения, останавливает проигрывание мелодии в игре.
   */
  public static void checkMusicForStopGameActivity() {
    if (isMusicOff()) {
      return;
    }
    boolean processOpeningOtherActivity = sp.getBoolean(OTHER_ACTIVITY, false);
    //если открывается other activity(select level) ничего не делать
    if (processOpeningOtherActivity) {
      return;
    }
    boolean processOpeningMainActivity = sp.getBoolean(MAIN_ACTIVITY, false);
    if (!processOpeningMainActivity) {
      BackgroundMusic.freeResourcesForPlayer();
    }
    //если открывается main activity(select level) ничего не делать
  }

  /**
   * Устанавливает свойство, которое определяет, идет ли процесс открытия
   * главного (неглавного, игрового) активити
   * @param activity - активити, для которого устанавливется флаг открытия
   * @param isOpeningProcess флаг для активити:
   * открывается(true)/не открывается(false)
   */
  private static void openingActivity(String activity, boolean isOpeningProcess) {
    if (sp != null) {
      SharedPreferences.Editor editor = sp.edit();
      editor.putBoolean(activity, isOpeningProcess);
      editor.apply();
    }
  }

  private static boolean isMusicOff() {
    String stateMusic = sp.getString("music", defaultStateMusic);
    return stateMusic.equalsIgnoreCase("off");
  }

  /*
   * Установить ориентацию экрана в соответсвии с настройками для
   * текущего активити.
   * @param activity текущий активити, для которого определяется ориентация
  public static void spotOrientationScreen(AppCompatActivity activity) {
    String currentVibration = sp.getString("vibration", defaultVibration);
    if (currentOrientation.equalsIgnoreCase("portrait")) {
      activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } else {
      activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
  }
   */
  public static void spotFlagOpenDialogWindow(boolean b) {
    if (sp != null) {
      SharedPreferences.Editor editor;
      editor = sp.edit();
      editor.putBoolean("dialog_open", b);
      editor.apply();
    }
  }

  /**
   * Определить уровень громкости приложения при старте(STREAM_MUSIC),
   * - в зависимости от уровня громкости в системе (STREAM_SYSTEM)
   */
  private static void spotStartVolumeLevel(AudioManager am) {
    if (am != null) {
      // Уровень громкости в приложении - в два раза больше чем в системе.
      // Если в системе звук отключен - то и в приложении при старте не будет звука.
      int volumeLevel = am.getStreamVolume(AudioManager.STREAM_SYSTEM) * 2;
      am.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0);
    }
  }

  RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {
    /** метод вызывается при создании базы данных */
    public void onCreate(@NonNull SupportSQLiteDatabase db) {
      Executors.newSingleThreadScheduledExecutor().execute(() -> {
        Log.i(TAG, "CREATE DATABASE LEVELS");
        Level level1 = new Level(0, 1, "level1", true);
        Level level2 = new Level(1, 2, "level2", false);
        Level level3 = new Level(2, 3, "level3", false);
        Level level4 = new Level(3, 4, "level4", false);
        Level level5 = new Level(4, 5, "level5", false);
        dbLevels.levelDao().insertAll(level1, level2, level3, level4, level5);
      });
    }
  };
}

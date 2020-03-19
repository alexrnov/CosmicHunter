package alexrnov.cosmichunter.sound;

import android.app.Application;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

import alexrnov.cosmichunter.R;

import static alexrnov.cosmichunter.Initialization.sp;

/**
 * Класс для воспроизведения коротких звуков в приложении. Используются статические
 * поля и методы, поскольку если в отдельных активити создавать отдельные
 * soundPoolMap, при воспроизведении треков начинают происходить ошибки - треки не проигрываются
 */
public class ShortSounds {

  private static SoundPool clickSound; // звук происходит при нажатии на кнопки интерфейса
  private static SoundPool explosionSound; // звук воспроизводится при взрыве астрероидов
  private static SoundPool gunSound; // звук воспроизводится при пуске ракеты
  // SparseIntArray дает лучшую производительность по сравнению с
  // HashMap<Integer, Integer>()
  private static SparseIntArray soundPoolMap = new SparseIntArray();

  /**
   * Инициализация и загрузка треков для коротких звуков
   * @param application - экземпляр класса, который запускается при
   * инициализации приложения
   */
  public static void init(Application application) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      clickSound = new SoundPool.Builder()
              .setMaxStreams(2) // для кликов достаточно двух потоков
              .build();
      explosionSound = new SoundPool.Builder()
              .setMaxStreams(3) // использовать три отдельных потока для взрывов, чтобы они могли накладываться друг на друга
              .build();
      gunSound = new SoundPool.Builder()
              .setMaxStreams(2) // для звуков пуска ракет достаточно двух потоков, поскольку звуки достаточно короткие
              .build();
    } else {
      clickSound = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
      explosionSound = new SoundPool(3, AudioManager.STREAM_MUSIC, 100);
      gunSound = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
    }

    soundPoolMap.put(0, clickSound.load(application, R.raw.click_sound, 0));
    soundPoolMap.put(1, explosionSound.load(application, R.raw.explosion_sound, 0));
    soundPoolMap.put(2, gunSound.load(application, R.raw.gun_sound, 0));
  }

  /** воспризвести звук нажатия на кнопку меню */
  public static void playClick() {
    String stateSound = sp.getString("sound", "on");
    if (stateSound.equals("on")) {
      clickSound.play(soundPoolMap.get(0),
              1.0f, 1.0f, 0, 0, 1f);
    }
  }

  /** воспроизвести звук взрыва */
  public static void playExplosion() {
    // проверка вклчения опции звука проводится при запуске рендера
    explosionSound.play(soundPoolMap.get(1), 1.0f, 1.0f, 0, 0, 1f);
  }

  /** воспроизвести звук пуска ракеты */
  public static void playGun() {
    // проверка вклчения опции звука проводится при запуске рендера
    gunSound.play(soundPoolMap.get(2), 0.2f, 0.2f, 0, 0, 1f);
  }
}
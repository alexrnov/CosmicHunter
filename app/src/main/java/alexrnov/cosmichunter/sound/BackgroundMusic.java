package alexrnov.cosmichunter.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.getVolumeForApplication;
import static java.io.File.separator;

/** Класс для фоновой музыки в приложении */
public class BackgroundMusic {
  private static MediaPlayer player;
  private static final String resourceFolder = "raw";

  /** Запускает мелодию в меню */
  public static void createMenuPlayer(AppCompatActivity activity) {
    final String musicFile = "menu_music";
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + resourceFolder + separator + musicFile);
    startPlayer(activity, uri);
  }

  /** Запускает мелодию в самой игре */
  public static void createGamePlayer(AppCompatActivity activity, int levelNumber) {
    final String musicFile;
    switch (levelNumber) {
      case 2:
        musicFile = "level2_music";
        break;
      case 3:
        musicFile = "level3_music";
        break;
      case 4:
        musicFile = "level4_music";
        break;
      case 5:
        musicFile = "level5_music";
        break;
      default:
        musicFile = "level1_music";
        break;
    }

    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + resourceFolder + separator + musicFile);
    startPlayer(activity, uri);
  }

  private static void startPlayer(AppCompatActivity activity, Uri uri) {
    // получить настройки громкости в системе
    float[] volume = getVolumeForApplication(activity);

    player = new MediaPlayer();
    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player.setVolume(volume[0], volume[1]);
    player.setLooping(true);
    try {
      player.setDataSource(activity.getApplicationContext(), uri);
      player.prepareAsync();//асинхронная загрузка мелодии
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }
    //музыка начнется после завершения подготовки mp3 файла
    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer player) {
        player.start();
      }
    });
  }

  /** Освобождает ресурсы для плеера (музыка останавливается) */
  public static void freeResourcesForPlayer() {
    if (player != null) {
      player.release(); // освободить системные ресурсы, выделенные для плеера
      player = null;
    }
  }
}

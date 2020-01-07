package alexrnov.cosmichunter;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import static alexrnov.cosmichunter.Initialization.TAG;
import static java.io.File.separator;

/** Класс для фоновой музыки в приложении */
public class BackgroundMusic {
  private static MediaPlayer player;
  private static final String resourceFolder = "raw";

  /** Запускает мелодию в меню */
  public static void createMenuPlayer(AppCompatActivity activity) {
    final String musicFile = "crystal_waters";
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + resourceFolder + separator + musicFile);
    startPlayer(activity, uri);
  }

  /** Запускает мелодию в самой игре */
  public static void createGamePlayer(AppCompatActivity activity) {
    final String musicFile = "infinite_ocean";
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + resourceFolder + separator + musicFile);
    startPlayer(activity, uri);
  }

  private static void startPlayer(AppCompatActivity activity, Uri uri) {
    player = new MediaPlayer();
    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player.setVolume(1, 1);
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
      player.release();//освободить системные ресурсы, выделенные для плеера
      player = null;
    }
  }
}

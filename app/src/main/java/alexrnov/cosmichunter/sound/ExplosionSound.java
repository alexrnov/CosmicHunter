package alexrnov.cosmichunter.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import static alexrnov.cosmichunter.Initialization.TAG;
import static java.io.File.separator;

/** Класс для фоновой музыки в приложении */
public class ExplosionSound {
  private MediaPlayer player1;
  private MediaPlayer player2;
  private MediaPlayer player3;
  //private ArrayDeque<MediaPlayer> queue = new ArrayDeque<>();
  private Queue<MediaPlayer> queue = new LinkedList<>();

  public ExplosionSound(AppCompatActivity activity) {
    final String musicFile = "explosion";
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + "raw" + separator + musicFile);

    player1 = new MediaPlayer();
    player1.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player1.setVolume(1, 1);
    player1.setLooping(false);

    player2 = new MediaPlayer();
    player2.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player2.setVolume(1, 1);
    player2.setLooping(false);

    player3 = new MediaPlayer();
    player3.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player3.setVolume(1, 1);
    player3.setLooping(false);
    try {
      player1.setDataSource(activity.getApplicationContext(), uri);
      player1.prepare(); // асинхронная загрузка мелодии

      player2.setDataSource(activity.getApplicationContext(), uri);
      player2.prepare(); // асинхронная загрузка мелодии

      player3.setDataSource(activity.getApplicationContext(), uri);
      player3.prepare(); // асинхронная загрузка мелодии

      queue.add(player1);
      queue.add(player2);
      //queue.add(player3);
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }
  }

  public void createExplosion() {
    MediaPlayer currentPlayer = queue.poll(); // извлечь объект из головы очереди
    if (currentPlayer != null) {
      if (!currentPlayer.isPlaying()) currentPlayer.start();
      queue.add(currentPlayer); // добавить элемент в конце очереди
    }


    /* Освободить ресурсы для плеера (когда трек заканчивается) */

    /*
    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion");
        mp.reset();
        mp.release();// освободить системные ресурсы, выделенные для плеера
        mp = null;
      }
    });
    */
  }
}
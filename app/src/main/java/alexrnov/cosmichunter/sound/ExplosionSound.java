package alexrnov.cosmichunter.sound;

import android.media.AsyncPlayer;
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
  private MediaPlayer player4;
  //private AsyncPlayer player1a;
  //private ArrayDeque<MediaPlayer> queue = new ArrayDeque<>();
  private Queue<MediaPlayer> queue = new LinkedList<>();


  private Queue<Integer> queue2 = new LinkedList<>();


  private AppCompatActivity activity;
  private Uri uri;

  public ExplosionSound(AppCompatActivity activity) {
    final String musicFile = "explosion";
    uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + "raw" + separator + musicFile);
    this.activity = activity;
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

    player4 = new MediaPlayer();
    player4.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player4.setVolume(1, 1);
    player4.setLooping(false);

    //player1a = new AsyncPlayer("1");
    try {
      player1.setDataSource(activity.getApplicationContext(), uri);
      player1.prepareAsync(); // асинхронная загрузка мелодии

      player2.setDataSource(activity.getApplicationContext(), uri);
      player2.prepareAsync(); // асинхронная загрузка мелодии

      player3.setDataSource(activity.getApplicationContext(), uri);
      player3.prepareAsync(); // асинхронная загрузка мелодии

      player4.setDataSource(activity.getApplicationContext(), uri);
      player4.prepareAsync(); // асинхронная загрузка мелодии

      queue.add(player1);
      queue.add(player2);
      queue.add(player3);
      queue.add(player4);

      queue2.add(1);
      queue2.add(2);
      queue2.add(3);
      queue2.add(4);
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }
  }

  public void createExplosion() {
    Log.i(TAG, "queue2 = " + queue2.toString());
    Integer currentPlayer = queue2.poll();
    if (currentPlayer != null) {
      /*
      switch (currentPlayer) {
        case 1:
          Log.i("TAG", "ExplosionSound = 1");
          ExplosionSound1.play(activity);
          break;
        case 2:
          Log.i("TAG", "ExplosionSound = 2");
          ExplosionSound2.play(activity);
          break;
        case 3:
          Log.i("TAG", "ExplosionSound = 3");
          ExplosionSound3.play(activity);
          break;
        case 4:
          Log.i("TAG", "ExplosionSound = 4");
          ExplosionSound4.play(activity);
          break;
      }

       */
      queue2.add(currentPlayer); // добавить элемент в конце очереди

    }
    /*
    MediaPlayer currentPlayer = queue.poll(); // извлечь объект из головы очереди
    if (currentPlayer != null) {
      //if (!currentPlayer.isPlaying()) currentPlayer.start();




      //new Thread(() -> {
        //currentPlayer.start();
        //queue.add(currentPlayer); // добавить элемент в конце очереди
      //}).start();


      currentPlayer.start();
      queue.add(currentPlayer); // добавить элемент в конце очереди
      Log.i("TAG", "music thread = " + Thread.currentThread().getName());

  }

  */
  }

  /** Освободить ресурсы для плеера (когда трек заканчивается) */
  public void freeResourcesForPlayer() {
    for (MediaPlayer mp: queue) {
      if (mp != null) {
        mp.reset(); // освободить системные ресурсы, выделенные для плеера
        mp.release();
      }
    }
  }
}
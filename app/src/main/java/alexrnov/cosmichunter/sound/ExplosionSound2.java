package alexrnov.cosmichunter.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import static alexrnov.cosmichunter.Initialization.TAG;
import static java.io.File.separator;

public class ExplosionSound2 {

  private MediaPlayer player2;
  //private static Uri uri;
  //private static AppCompatActivity activity;


  public void play(AppCompatActivity activity) {
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + "raw" + separator + "explosion");
    player2 = new MediaPlayer();
    player2.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player2.setVolume(1, 1);
    player2.setLooping(false);

    try {
      player2.setDataSource(activity.getApplicationContext(), uri);
      player2.prepareAsync(); // асинхронная загрузка мелодии
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }

    player2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        player2.start();
      }
    });

    player2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        player2.reset();
        player2.release();
      }
    });
  }

  public void stop() {
    if (player2 != null) {
      player2.reset();
      player2.release();
      player2 = null;
    }
  }
}

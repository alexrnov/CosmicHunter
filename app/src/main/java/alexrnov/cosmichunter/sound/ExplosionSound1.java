package alexrnov.cosmichunter.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import static alexrnov.cosmichunter.Initialization.TAG;
import static java.io.File.separator;

public class ExplosionSound1 {




  private MediaPlayer player1;
  //private static Uri uri;
  //private static AppCompatActivity activity;

  public void play(AppCompatActivity activity) {
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + "raw" + separator + "explosion");
    player1 = new MediaPlayer();
    player1.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player1.setVolume(1, 1);
    player1.setLooping(false);

    try {
      player1.setDataSource(activity.getApplicationContext(), uri);
      player1.prepareAsync(); // асинхронная загрузка мелодии
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }

    player1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        player1.start();
      }
    });

    /*
    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        player.reset();
        player.release();
      }
    });
    */
  }

  public void stop() {
    if (player1 != null) {
      player1.reset();
      player1.release();
      player1 = null;
    }
  }


}

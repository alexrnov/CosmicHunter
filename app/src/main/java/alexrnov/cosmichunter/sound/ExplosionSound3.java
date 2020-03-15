package alexrnov.cosmichunter.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import static alexrnov.cosmichunter.Initialization.TAG;
import static java.io.File.separator;

public class ExplosionSound3 {

  private MediaPlayer player3;
  //private static Uri uri;
  //private static AppCompatActivity activity;


  public void play(AppCompatActivity activity) {
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + "raw" + separator + "explosion");
    player3 = new MediaPlayer();
    player3.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player3.setVolume(1, 1);
    player3.setLooping(false);

    try {
      player3.setDataSource(activity.getApplicationContext(), uri);
      player3.prepareAsync(); // асинхронная загрузка мелодии
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }

    player3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        player3.start();
      }
    });

    player3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        player3.reset();
        player3.release();
      }
    });
  }

  public void stop() {
    if (player3 != null) {
      player3.reset();
      player3.release();
      player3 = null;
    }
  }
}

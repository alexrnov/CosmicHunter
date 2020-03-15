package alexrnov.cosmichunter.sound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import static alexrnov.cosmichunter.Initialization.TAG;
import static java.io.File.separator;

public class ExplosionSound4 {

  private MediaPlayer player4;
  //private static Uri uri;
  //private static AppCompatActivity activity;


  public void play(AppCompatActivity activity) {
    Uri uri = Uri.parse("android.resource://" + activity.getPackageName() + separator
            + "raw" + separator + "explosion");
    player4 = new MediaPlayer();
    player4.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player4.setVolume(1, 1);
    player4.setLooping(false);

    try {
      player4.setDataSource(activity.getApplicationContext(), uri);
      player4.prepareAsync(); // асинхронная загрузка мелодии
    } catch(IOException e) {
      Log.e(TAG, "Error load music");
    }

    player4.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        player4.start();
      }
    });

    player4.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        player4.reset();
        player4.release();
      }
    });
  }

  public void stop() {
    if (player4 != null) {
      player4.reset();
      player4.release();
      player4 = null;
    }
  }
}

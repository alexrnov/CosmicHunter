package alexrnov.cosmichunter.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import alexrnov.cosmichunter.base.LevelDao;
import alexrnov.cosmichunter.base.LevelDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Objects;

import alexrnov.cosmichunter.R;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;
import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.backToHome;

public class LevelsActivity extends AppCompatActivity {

  private String className = this.getClass().getSimpleName() + ".class: ";
  private int versionGLES;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_level);

    Toolbar toolbar = findViewById(R.id.toolbar_level);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // enable the Up button
    getSupportActionBar().setTitle("");

    versionGLES = getIntent().getIntExtra("versionGLES", 2);
  }

  public void startLevel1(View view) {
    startLevel(0);
  }

  public void startLevel2(View view) {
    startLevel(1);
  }

  public void startLevel3(View view) {
    startLevel(2);
  }

  public void startLevel4(View view) {
    startLevel(3);
  }

  public void startLevel5(View view) {
    startLevel(4);
  }

  private void startLevel(int level) {
    LevelDatabase dbLevels = Room.databaseBuilder(this.getApplicationContext(), LevelDatabase.class, "levels-database").allowMainThreadQueries().build();
    LevelDao dao = dbLevels.levelDao();
    if (dao.findById(level).isOpen) {
      spotFlagOpenDialogWindow(false);
      Intent intent = new Intent(this, GameActivity.class);
      intent.putExtra("versionGLES", versionGLES);
      intent.putExtra("Level", level);
      startActivity(intent);
    }
  }

  public void backToMainMenu(View view) {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onStart() {
    Log.i(TAG, className + "onStart()");
    super.onStart();
    checkMusicForStartOtherActivity(this);
  }

  @Override
  protected void onResume() {
    Log.i(TAG, className + "onResume()");
    super.onResume();
  }

  @Override
  protected void onPause() {
    Log.i(TAG, className + "onPause()");
    super.onPause();
  }

  @Override
  protected void onStop() {
    Log.i(TAG, className + "onStop()");
    super.onStop();
    checkMusicForStopOtherActivity();
  }

  /** Слушатель для правой кнопки activity bar */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.action_exit) {
      backToHome(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_layout, menu);
    return super.onCreateOptionsMenu(menu);
  }
}

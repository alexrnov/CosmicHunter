package alexrnov.cosmichunter.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import alexrnov.cosmichunter.LoadingPanel;
import alexrnov.cosmichunter.base.LevelDao;
import alexrnov.cosmichunter.base.LevelDatabase;
import androidx.appcompat.app.AppCompatActivity;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Objects;

import alexrnov.cosmichunter.R;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;
import static alexrnov.cosmichunter.Initialization.spotFlagOpenDialogWindow;
//import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.sound.ShortSounds.playClick;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.backToHome;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.changeHeaderColorInRecentApps;

public class LevelsActivity extends AppCompatActivity {

  //private String className = this.getClass().getSimpleName() + ".class: ";

  private Toolbar toolbar;
  // окно с черным фоном, в котором отображаются надписи и
  // анимированное изображение загрузки
  private ConstraintLayout loadPanel;

  private int versionGLES;

  private Button buttonLevel2;
  private Button buttonLevel3;
  private Button buttonLevel4;
  private Button buttonLevel5;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //Log.i(TAG, className + "onCreate()");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_level);

    loadPanel = findViewById(R.id.load_panel_level);
    toolbar = findViewById(R.id.toolbar_level);
    setSupportActionBar(toolbar);

    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // enable the Up button
    getSupportActionBar().setTitle("");

    Intent intent = getIntent();
    versionGLES = intent.getIntExtra("versionGLES", 2);
    @SuppressWarnings("unchecked")
    HashMap<String, Boolean> levels =
            (HashMap<String, Boolean>) intent.getSerializableExtra("levels");
    /*
    Log.i(TAG, "hashMap = "
            + levels.get("level2") + " "
            + levels.get("level3") + " "
            + levels.get("level4") + " "
            + levels.get("level5") + " ");
    */
    buttonLevel2 = findViewById(R.id.button_level2);
    buttonLevel3 = findViewById(R.id.button_level3);
    buttonLevel4 = findViewById(R.id.button_level4);
    buttonLevel5 = findViewById(R.id.button_level5);
    if (levels != null) {
      activateButtonsForOpenedLevels(levels);
    }
  }

  public void startLevel1(View view) { startLevel(1); }

  public void startLevel2(View view) {
    startLevel(2);
  }

  public void startLevel3(View view) {
    startLevel(3);
  }

  public void startLevel4(View view) {
    startLevel(4);
  }

  public void startLevel5(View view) {
    startLevel(5);
  }

  private void startLevel(int level) {
    playClick();
    LevelDatabase dbLevels = Room.databaseBuilder(this.getApplicationContext(), LevelDatabase.class, "levels-database").allowMainThreadQueries().build();
    LevelDao dao = dbLevels.levelDao();
    if (dao.findByNumber(level).isOpen) { // если уровень открыт
      spotFlagOpenDialogWindow(false);
      showLoadPanel(level);
      Intent intent = new Intent(this, GameActivity.class);
      intent.putExtra("versionGLES", versionGLES);
      intent.putExtra("Level", level);
      startActivity(intent);
    }
  }

  public void backToMainMenu(View view) {
    playClick();
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onStart() {
    //Log.i(TAG, className + "onStart()");
    super.onStart();
    hideLoadPanel(); // скрыть панель загрузки
    checkMusicForStartOtherActivity(this);
  }

  @Override
  protected void onResume() {
    //Log.i(TAG, className + "onResume()");
    super.onResume();
    changeHeaderColorInRecentApps(this);
  }

  @Override
  protected void onPause() {
    //Log.i(TAG, className + "onPause()");
    super.onPause();
  }

  @Override
  protected void onStop() {
    //Log.i(TAG, className + "onStop()");
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

  /** активировать кнопки выбора уровней, если уровни открыты */
  private void activateButtonsForOpenedLevels(HashMap<String, Boolean> levels) {
    if (Objects.requireNonNull(levels.get("level2"))) buttonLevel2.setBackgroundResource(R.drawable.toggle_button_shape);
    if (Objects.requireNonNull(levels.get("level3"))) buttonLevel3.setBackgroundResource(R.drawable.toggle_button_shape);
    if (Objects.requireNonNull(levels.get("level4"))) buttonLevel4.setBackgroundResource(R.drawable.toggle_button_shape);
    if (Objects.requireNonNull(levels.get("level5"))) buttonLevel5.setBackgroundResource(R.drawable.toggle_button_shape);
    /*
    AsyncTask.execute(() -> {
      LevelDatabase dbLevels = Room.databaseBuilder(this.getApplicationContext(),
              LevelDatabase.class, "levels-database").build();
      LevelDao dao = dbLevels.levelDao();
      // изменить фон кнопки в потоке пользовательского интерфейса
      if (dao.findByNumber(2).isOpen) runOnUiThread(() -> buttonLevel2.setBackgroundResource(R.drawable.toggle_button_shape));
      if (dao.findByNumber(3).isOpen) runOnUiThread(() -> buttonLevel3.setBackgroundResource(R.drawable.toggle_button_shape));
      if (dao.findByNumber(4).isOpen) runOnUiThread(() -> buttonLevel4.setBackgroundResource(R.drawable.toggle_button_shape));
      if (dao.findByNumber(5).isOpen) runOnUiThread(() -> buttonLevel5.setBackgroundResource(R.drawable.toggle_button_shape));
    });
    */
  }

  private void hideLoadPanel() {
    // не работать с панелью загрузки в MainActivity для API 14-23, поскольку
    // для ранних версий панель загрузки отображается в GameActivity
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return;
    // при старте активити сделать окно загрузки невидимым
    loadPanel.setVisibility(View.INVISIBLE);
    // сделать тулбар видимым
    toolbar.setVisibility(View.VISIBLE);
    // сделать видимой панель статуса
    this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
  }

  private void showLoadPanel(int level) {
    // не работать с панелью загрузки в MainActivity для API 14-23, поскольку
    // для ранних версий панель загрузки отображается в GameActivity
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return;
    // убрать строку статуса вверху
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    loadPanel.bringToFront(); // переместить панель загрузки на передний план
    loadPanel.requestLayout(); // чтобы работало на Android 4.1.1

    toolbar.setVisibility(View.INVISIBLE); // сделать тулбар невидимым

    // установить для надписи загружаемого уровня - текущий уровень
    TextView loadLevelText = findViewById(R.id.load_level_text);
    String currentLevel = getString(R.string.level) + " " + level;
    loadLevelText.setText(currentLevel); // вывести на экран загрузки название текущего уровня

    ImageView loadImage = findViewById(R.id.image_process);
    loadImage.setBackgroundResource(R.drawable.animation_process);
    // отобразить окно загрузки в отдельном AsyncTask
    new LoadingPanel(this, loadPanel, loadImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
  }
}

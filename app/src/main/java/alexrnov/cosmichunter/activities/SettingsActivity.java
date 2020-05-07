package alexrnov.cosmichunter.activities;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

import alexrnov.cosmichunter.sound.BackgroundMusic;
import alexrnov.cosmichunter.R;
import androidx.appcompat.widget.Toolbar;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;

import static alexrnov.cosmichunter.Initialization.sp;
import static alexrnov.cosmichunter.sound.ShortSounds.playClick;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.backToHome;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.changeHeaderColorInRecentApps;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.showSnackbar;
import static alexrnov.cosmichunter.Initialization.TAG;
/** Активити-класс управляет отображением и поведением меню настроек */
public class SettingsActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

  private RadioButton vibrationRadioButton;
  private RadioButton soundRadioButton;
  private RadioButton musicRadioButton;
  private TextView particlesLabel;
  private SeekBar seekBar;
  private CheckBox checkBox;

  private View view;

  private String vibrationText = "Vibration";
  private String soundText = "Sound";
  private String musicText = "Music";
  private String onText = "on";
  private String offText = "off";
  private String particlesText = "Particles level";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    Toolbar toolbar = findViewById(R.id.toolbar_settings);
    setSupportActionBar(toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // enable the Up button
    getSupportActionBar().setTitle("");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    particlesText = getString(R.string.particles_level);
    particlesLabel = findViewById(R.id.particles_label);

    defineViewRadioButtons();

    checkBox = (CheckBox) findViewById(R.id.smog_checkbox);
    boolean fogChecked = sp.getBoolean("fog", true);
    checkBox.setChecked(fogChecked);

    addListeners();

    view = findViewById(R.id.background_settings);
    vibrationText = getString(R.string.settings_vibration_label);
    soundText = getString(R.string.settings_sound_label);
    musicText = getString(R.string.settings_music_label);
    onText = getString(R.string.settings_on);
    offText = getString(R.string.settings_off);

  }


  /**
   * Определить положение радио-кнопок в зависимости от того, как они
   * были расположены в прошлом рабочем сеансе. Если это первый запуск
   * приложения, тогда положение устанавливется в зависимости от
   * значений строковых переменных, расположенных в файле strings.xml
   */
  private void defineViewRadioButtons() {
    String defaultValue;
    String currentValue;
    RadioGroup group;

    defaultValue = getResources().getString(R.string.default_vibration);
    currentValue = sp.getString("vibration", defaultValue);
    group = findViewById(R.id.radioGroup_vibration);
    if (currentValue.equalsIgnoreCase("off")) {
      group.check(R.id.vibrationOff);
    } else {
      group.check(R.id.vibrationOn);
    }

    defaultValue = getResources().getString(R.string.default_music);
    currentValue = sp.getString("music", defaultValue);
    group = findViewById(R.id.radioGroup_music);
    if (currentValue.equalsIgnoreCase("off")) {
      group.check(R.id.musicOff);
    } else {
      group.check(R.id.musicOn);
    }

    defaultValue = getResources().getString(R.string.default_sound);
    currentValue = sp.getString("sound", defaultValue);
    group = findViewById(R.id.radioGroup_sound);
    if (currentValue.equalsIgnoreCase("off")) {
      group.check(R.id.soundOff);
    } else {
      group.check(R.id.soundOn);
    }
  }

  /** Метод добавляет слушателей к радио-кнопкам */
  private void addListeners() {
    vibrationRadioButton = findViewById(R.id.vibrationOff);
    if (vibrationRadioButton != null) {
      vibrationRadioButton.setOnCheckedChangeListener(this);
    }
    soundRadioButton = findViewById(R.id.soundOff);
    if (soundRadioButton != null) {
      soundRadioButton.setOnCheckedChangeListener(this);
    }
    musicRadioButton = findViewById(R.id.musicOff);
    if (musicRadioButton != null) {
      musicRadioButton.setOnCheckedChangeListener(this);
    }


    int particlesValue = sp.getInt("particles", 300);
    String s = particlesText + ": " + particlesValue;
    particlesLabel.setText(s);
    seekBar = findViewById(R.id.number_particle);
    seekBar.setProgress(particlesValue); // установить текущее значение seekbar
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      private int currentValue;
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.i(TAG, "progress = " + progress);
        double d = Math.round(100 + progress * 0.857);
        currentValue = (int) d;
        String s = particlesText + ": " + currentValue;
        particlesLabel.setText(s);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "seekbar is starter");
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i(TAG, "seekbar is stopped");
        //currentValue = Math.round(currentValue);
        //int i = (int) currentValue;
        if (sp != null) {
          SharedPreferences.Editor editor;
          editor = sp.edit();
          editor.putInt("particles", currentValue);
          editor.apply();
        }
        String s = particlesText + ": " + currentValue;
        particlesLabel.setText(s);
        showSnackbar(view, particlesText + ": " + currentValue);
      }
    });


    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        boolean check;
        if (buttonView.isChecked()) {
          Log.i(TAG, "checked");
          check = true;
        } else {
          Log.i(TAG, "non checked");
          check = false;
        }

        if (sp != null) {
          SharedPreferences.Editor editor;
          editor = sp.edit();
          editor.putBoolean("fog", check);
          editor.apply();
        }
      }
    });

  }

  @Override
  public void onCheckedChanged(CompoundButton button, boolean b) {
    if (button.getId() == vibrationRadioButton.getId()) {
      playClick();
      establishValue("vibration", b ? "off" : "on");
      showSnackbar(view, vibrationText + ": " + (b ? offText : onText));
    } else if (button.getId() == musicRadioButton.getId()) {
      playClick();
      establishValue("music", b ? "off" : "on");
      manageMusic(b);
      showSnackbar(view, musicText + ": "+ (b ? offText : onText));
    } else if (button.getId() == soundRadioButton.getId()) {
      playClick();
      establishValue("sound", b ? "off" : "on");
      showSnackbar(view, soundText + ": " + (b ? offText : onText));
    }
  }

  private void manageMusic(boolean b) {
    if (b) {
      BackgroundMusic.freeResourcesForPlayer(); //отключить проигрывание музыки
    } else {
      checkMusicForStartOtherActivity(this); //влючить проигрывание музыки
    }
  }

  private void establishValue(String key, String value) {
    if (sp != null) {
      SharedPreferences.Editor editor;
      editor = sp.edit();
      editor.putString(key, value);
      editor.apply();
    }
  }

  public void backToMainMenu(View view) {
    playClick();
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onStart() {
    super.onStart();
    checkMusicForStartOtherActivity(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    checkMusicForStopOtherActivity();
  }

  protected void onResume() {
    super.onResume();
    changeHeaderColorInRecentApps(this);
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

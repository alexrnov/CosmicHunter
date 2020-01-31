package alexrnov.cosmichunter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import alexrnov.cosmichunter.BackgroundMusic;
import alexrnov.cosmichunter.R;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;
import static alexrnov.cosmichunter.Initialization.sp;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.showSnackbar;

/** Активити-класс управляет отображением и поведением меню настроек */
public class SettingsActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

  private RadioButton vibrationRadioButton;
  private RadioButton soundRadioButton;
  private RadioButton musicRadioButton;

  private View view;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    defineViewRadioButtons();
    addListeners();
    view = findViewById(R.id.background_settings);
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
  }

  @Override
  public void onCheckedChanged(CompoundButton button, boolean b) {
    if (button.getId() == vibrationRadioButton.getId()) {
      establishValue("vibration", b ? "off" : "on");
      showSnackbar(view, "Вибрация: " + (b ? "off" : "on"));
    } else if (button.getId() == musicRadioButton.getId()) {
      establishValue("music", b ? "off" : "on");
      manageMusic(b);
      showSnackbar(view, "Музыка: " + (b ? "off" : "on"));
    } else if (button.getId() == soundRadioButton.getId()) {
      establishValue("sound", b ? "off" : "on");
      manageMusic(b);
      showSnackbar(view, "Звук: " + (b ? "off" : "on"));
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

}

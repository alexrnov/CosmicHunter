package alexrnov.cosmichunter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import alexrnov.cosmichunter.BackgroundMusic;
import alexrnov.cosmichunter.R;
import alexrnov.cosmichunter.activities.MainActivity;

import static alexrnov.cosmichunter.Initialization.checkMusicForStartOtherActivity;
import static alexrnov.cosmichunter.Initialization.checkMusicForStopOtherActivity;
import static alexrnov.cosmichunter.Initialization.sp;
import static alexrnov.cosmichunter.utils.ApplicationUtilsKt.showSnackbar;

/** Активити-класс управляет отображением и поведением меню настроек */
public class SettingsActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {

  private RadioButton vibrationRadioButton;
  private RadioButton languageRadioButton;
  private RadioButton soundRadioButton;
  private RadioButton musicRadioButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    defineViewRadioButtons();
    addListeners();
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

    defaultValue = getResources().getString(R.string.default_language);
    currentValue = sp.getString("language", defaultValue);
    group = findViewById(R.id.radioGroup_language);

    if (currentValue.equalsIgnoreCase("russian")) {
      group.check(R.id.russian);
    } else {
      group.check(R.id.english);
    }

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
    languageRadioButton = findViewById(R.id.english);
    if (languageRadioButton != null) {
      languageRadioButton.setOnCheckedChangeListener(this);
    }
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
    if (button.getId() == languageRadioButton.getId()) {
      establishValue("language", b ? "english" : "russian");
      showSnackbar(button.getRootView(), "Нет поддержки OpenGL");
    } else if (button.getId() == vibrationRadioButton.getId()) {
      establishValue("vibration", b ? "off" : "on");
    } else if (button.getId() == musicRadioButton.getId()) {
      establishValue("music", b ? "off" : "on");
      manageMusic(b);
    } else if (button.getId() == soundRadioButton.getId()) {
      establishValue("sound", b ? "off" : "on");
      manageMusic(b);
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

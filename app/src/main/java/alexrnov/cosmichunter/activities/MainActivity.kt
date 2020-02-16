package alexrnov.cosmichunter.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity // пакет v7 также поддерживает минимальный уровень API 14 (Android 4.0)
import android.os.Bundle
import android.util.Log
import android.view.View

import alexrnov.cosmichunter.R

import alexrnov.cosmichunter.Initialization.checkMusicForStartMainActivity
import alexrnov.cosmichunter.Initialization.checkMusicForStopMainActivity
import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.utils.showSnackbar
import android.text.Html
import android.view.Menu
import android.view.MenuItem

class MainActivity: AppCompatActivity() {
  private val className = this.javaClass.simpleName + ".class: "

  // Проверка версии OpenGL на устройстве в рантайме. В манифесте объявляется поддержка
  // OpenGL 2, которая по умолчанию подразумевает поддержку OpenGL 2 и OpenGL 1.
  // Поскольку на самом деле, данное приложение поддерживает OpenGL 2 и OpenGL 3,
  // поддержиаемая версия проверяется в рантайме. Если поддерживается OpenGL 3, используется
  // третья версия, если поддерживается OpenGL 2, тогда используется вторая версия,
  // если поддерживается только версия OpenGL 1, тогда GameActivity или LevelsActivity не запускаются
  private val supportOpenGLES: Int
    get() {
      val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      val info = am.deviceConfigurationInfo ?: return 1
      val version = java.lang.Double.parseDouble(info.glEsVersion)
      return when {
        version >= 3.0 -> 3 // info.reqGlEsVersion >= 0x30000
        version >= 2.0 -> 2
        else -> 1
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) { //состояние "создано"
    // ориентация экрана определяется в файле манифеста, а не в коде - это позволяет избежать
    // повторной перезагрузки активити. Кроме того, не нужно создавать лэйаут для портретной
    // ориентации, поскольку в начале будет загружаться именно он
    super.onCreate(savedInstanceState)
    Log.i(TAG, className + "onCreate()")
    setContentView(R.layout.activity_main)
    setSupportActionBar(findViewById(R.id.toolbar_main_menu))
    supportActionBar?.setDisplayHomeAsUpEnabled(true) // enable the Up button

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Log.i(TAG, "SDK_INT >= N")
      supportActionBar?.title = Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>", Html.FROM_HTML_MODE_LEGACY)
    } else {
      @Suppress("DEPRECATION")
      Log.i(TAG, "SDK_INT < N")
      supportActionBar?.title = Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>")
    }
  }

  override fun onStart() { // состояние "запущено"
    super.onStart()
    Log.i(TAG, className + "onStart()")
    checkMusicForStartMainActivity(this)
  }

  override fun onStop() { // состояние "остановлено"
    super.onStop()
    Log.i(TAG, className + "onStop()")
    checkMusicForStopMainActivity()
  }

  override fun onPause() {
    super.onPause()
    Log.i(TAG, className + "onPause()")
  }

  fun startGame(view: View) = startActivityForGame(GameActivity::class.java, view)
  fun selectLevel(view: View) = startActivityForGame(LevelsActivity::class.java, view)

  fun settingsMenu(view: View) {
    val intent = Intent(this, SettingsActivity::class.java)
    startActivity(intent)
  }

  fun aboutGame(view: View) {
    val intent = Intent(this, AboutGameActivity::class.java)
    startActivity(intent)
  }

  fun exitFromApplication(view: View) = backToHome() // выйти из приложения

  /*
   * Если кнопка выхода в навигационном меню, нажата в главном активити, - выйти из приложения, а не
   * возвращаться к другим активити (в том числе к игровому активити).
   */
  override fun onBackPressed() = backToHome()

  private fun backToHome() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_HOME)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent) // выйти на рабочий стол системы
    // Android 5.0 (API 21) and higher
    // завершить все активити в этой задаче и удалить их из списка "недавние" (recent)
    // работает правильно, если в манифесте launchMode="singleTask" а не "standard"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) finishAndRemoveTask()
    else finish()
    System.exit(0)
  }

  /**
   * Если нет поддержки второй или третьей версии OpenGL - вывести соответствующее сообщение,
   * иначе запустить GameActivity или LevelsActivity.
   */
  private fun startActivityForGame(activity: Class<out AppCompatActivity>, view: View) {
    if (supportOpenGLES != 1) {
      val intent = Intent(this, activity)
      intent.putExtra("versionGLES", supportOpenGLES)
      startActivity(intent)
    } else {
      showSnackbar(view, getString(R.string.opengl_not_support))
      //showToast(getApplicationContext(), "Нет поддержки OpenGL");
      //showCustomToast(this, "Нет поддержки OpenGL");
    }
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_settings -> {
      Log.i("TAG", "1")
      true
    }
    R.id.action_favorite -> {
      Log.i("TAG", "2")
      true
    }
    else -> {
      Log.i("TAG", "3")
      super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_layout, menu)
    return super.onCreateOptionsMenu(menu)
  }
}

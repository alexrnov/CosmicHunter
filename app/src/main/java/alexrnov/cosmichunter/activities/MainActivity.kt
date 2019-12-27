package alexrnov.cosmichunter.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ConfigurationInfo
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import alexrnov.cosmichunter.R

import alexrnov.cosmichunter.Initialization.checkMusicForStartMainActivity
import alexrnov.cosmichunter.Initialization.checkMusicForStopMainActivity
import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.utils.printDPSizeScreen
import alexrnov.cosmichunter.utils.showSnackbar

class MainActivity : AppCompatActivity() {

  private val className = this.javaClass.simpleName + ".class: "

  // Проверка версии OpenGL на устройстве в рантайме. В манифесте объявляется
  // поддержка OpenGL 2, которая по умолчанию подразумевает поддержку OpenGL 2 и OpenGL 1.
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
    Log.i(TAG, className + "onCreate()")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    printDPSizeScreen(this)
  }

  override fun onStart() { // состояние "запущено"
    Log.i(TAG, className + "onStart()")
    super.onStart()
    checkMusicForStartMainActivity(this)
  }

  override fun onStop() { // состояние "остановлено"
    Log.i(TAG, className + "onStop()")
    super.onStop()
    checkMusicForStopMainActivity()
  }

  override fun onPause() {
    Log.i(TAG, className + "onPause()")
    super.onPause()
  }

  fun startGame(view: View) {
    val v1: Class<GameActivity> = GameActivity::class.java
    val v2: Class<LevelsActivity> = LevelsActivity::class.java
    f(v1)
    if (supportOpenGLES != 1) {
      val intent = Intent(this, GameActivity::class.java)
      intent.putExtra("versionGLES", supportOpenGLES)
      startActivity(intent)
    } else showSnackbar(view, "Нет поддержки OpenGL")
    //showToast(getApplicationContext(), "Нет поддержки OpenGL");
    //showCustomToast(this, "Нет поддержки OpenGL");
  }

  fun selectLevel(view: View) {
    if (supportOpenGLES != 1) {
      val intent = Intent(this, LevelsActivity::class.java)
      intent.putExtra("versionGLES", supportOpenGLES)
      startActivity(intent)
    } else showSnackbar(view, "Нет поддержки OpenGL")
  }

  fun settingsMenu(view: View) {
    val intent = Intent(this, SettingsActivity::class.java)
    startActivity(intent)
  }

  /** выйти из приложения  */
  fun exitFromApplication(view: View) = backToHome()

  /*
   * Если кнопка выхода нажата в главном меню, - выйти из приложения, а не
   * возвращаться к другим активити(в том числе к игровому активити).
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
  fun f(a: Class<out AppCompatActivity>) {

  }
}

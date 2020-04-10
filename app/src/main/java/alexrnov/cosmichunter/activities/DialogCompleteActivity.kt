package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.Initialization
import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.R
import alexrnov.cosmichunter.sound.ShortSounds.playClick
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View

class DialogCompleteActivity: Activity() {

  val className = this.javaClass.simpleName + ".class: "

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_complete_dialog)

    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)
    val width = dm.widthPixels
    val height = dm.heightPixels
    if (width < height) {
      window.setLayout((width * .8).toInt(), (height * .4).toInt())
    } else {
      window.setLayout((width * .5).toInt(), (height * .7).toInt())
    }
    Log.i(TAG, "$className onCreate()")
  }

  override fun onStop() {
    Log.i(TAG, className + "onStop()")
    super.onStop()
    finish()
  }

  fun nextLevel(view: View) {
    playClick()
    Initialization.spotFlagOpenDialogWindow(false)

    val intent = Intent(this, GameActivity::class.java)
    val versionGLES = getIntent().getIntExtra("versionGLES", 2)
    val levelNumber = getIntent().getIntExtra("Level", 1) + 1
    intent.putExtra("versionGLES", versionGLES)
    intent.putExtra("Level", levelNumber)
    startActivity(intent)
  }

  fun backToMainMenu(view: View?) {
    playClick()
    startActivity(Intent(this, MainActivity::class.java))
  }

  /* нажатие на кнопку "назад", чтобы скрыть диалог */
  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if (keyCode == 0x00000004) { // KeyEvent.FLAG_KEEP_TOUCH_MODE; (API 3)
      Log.i(TAG, className + "onKeyDown(), keyCode = " + keyCode)
      Initialization.spotFlagOpenDialogWindow(false)
      //finish();
    }
    return super.onKeyDown(keyCode, event)
  }

  // не реагировать на прикосновение пальцев, чтобы диалог не скрывался
  // когда нажимаешь на экран за пределами фрейма диалога
  override fun onTouchEvent(event: MotionEvent?): Boolean {
    return false
  }
}
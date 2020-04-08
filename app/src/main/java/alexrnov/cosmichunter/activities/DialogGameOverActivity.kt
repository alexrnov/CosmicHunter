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

class DialogGameOverActivity: Activity() { // AppCompatActivity()

  val className = this.javaClass.simpleName + ".class: "

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_gameover_dialog)

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

  fun repeatGame(view: View) {
    playClick()
    Initialization.spotFlagOpenDialogWindow(false)

    //showLoadPanel(1)

    val intent = Intent(this, GameActivity::class.java)
    intent.putExtra("versionGLES", 3.0)
    intent.putExtra("Level", 1)
    startActivity(intent)

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

  /*
  private fun showLoadPanel(level: Int) {
    // не работать с панелью загрузки в MainActivity для API 14-23, поскольку
    // для ранних версий панель загрузки отображается в GameActivity
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
    // убрать строку статуса вверху
    this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    loadPanel.bringToFront() // переместить панель загрузки на передний план
    loadPanel.requestLayout() // чтобы работало на Android 4.1.1
    toolbar.setVisibility(View.INVISIBLE) // сделать тулбар невидимым

    // установить для надписи загружаемого уровня - текущий уровень
    val loadLevelText = findViewById<TextView>(R.id.load_level_text)
    val currentLevel = getString(R.string.level) + " " + level
    loadLevelText.text = currentLevel // вывести на экран загрузки название текущего уровня
    val loadImage = findViewById<ImageView>(R.id.image_process)
    loadImage.setBackgroundResource(R.drawable.animation_process)
    // отобразить окно загрузки в отдельном AsyncTask
    LoadingPanel(this, loadPanel, loadImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
  }
   */
}
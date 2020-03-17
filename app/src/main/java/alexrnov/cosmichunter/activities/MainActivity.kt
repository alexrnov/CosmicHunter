package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.Initialization
import alexrnov.cosmichunter.Initialization.*
import alexrnov.cosmichunter.LoadingPanel
import alexrnov.cosmichunter.R
import alexrnov.cosmichunter.base.LevelDatabase
import alexrnov.cosmichunter.utils.backToHome
import alexrnov.cosmichunter.utils.showSnackbar
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room

class MainActivity: AppCompatActivity() {
  private val className = this.javaClass.simpleName + ".class: "
  private var toolbar: Toolbar? = null
  // окно с черным фоном, в котором отображаются надписи и
  // анимированное изображение загрузки
  private var loadPanel: ConstraintLayout? = null
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

  //private var clickSound: SoundPool? = null
  //private val soundPoolMap = SparseIntArray()

  override fun onCreate(savedInstanceState: Bundle?) { //состояние "создано"
    // ориентация экрана определяется в файле манифеста, а не в коде - это позволяет избежать
    // повторной перезагрузки активити. Кроме того, не нужно создавать лэйаут для портретной
    // ориентации, поскольку в начале будет загружаться именно он
    super.onCreate(savedInstanceState)
    Log.i(TAG, className + "onCreate()")

    setContentView(R.layout.activity_main)

    loadPanel = findViewById(R.id.load_panel_main)
    toolbar = findViewById(R.id.toolbar_main_menu)

    setSupportActionBar(toolbar)
    supportActionBar?.title="" // текст и стиль заголовка определяется в лэйауте
    // программно изменить цвет текст в activity bar
    /*
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      Log.i(TAG, "SDK_INT >= N")
      supportActionBar?.title = Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>", Html.FROM_HTML_MODE_LEGACY)
    } else {
      @Suppress("DEPRECATION")
      Log.i(TAG, "SDK_INT < N")
      supportActionBar?.title = Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>")
    }
    */

    /*
    clickSound = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      SoundPool.Builder()
              .setMaxStreams(1)
              .build()
    } else {
      @Suppress("DEPRECATION")
      SoundPool(1, AudioManager.STREAM_MUSIC, 100)
    }
    val i: Int = clickSound?.load(this, R.raw.click_sound, 0)?: 0
    soundPoolMap.put(0, i)

     */
  }

  override fun onStart() { // состояние "запущено"
    super.onStart()
    hideLoadPanel() // скрыть панель загрузки

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

  fun startGame(view: View) {

    /*
    clickSound?.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f)

     */
    if (supportOpenGLES != 1) {
      showLoadPanel() // показать панель загрузки

      val intent = Intent(this, GameActivity::class.java)
      intent.putExtra("versionGLES", supportOpenGLES)
      intent.putExtra("Level", getCurrentOpenLevel())
      startActivity(intent)
    } else {
      showSnackbar(view, getString(R.string.opengl_not_support))
      //showToast(getApplicationContext(), "Нет поддержки OpenGL");
      //showCustomToast(this, "Нет поддержки OpenGL");
    }
  }

  fun selectLevel(view: View) {
    Initialization.clickSound.play(Initialization.soundPoolMap.get(0),
            1.0f, 1.0f, 0, 0, 1f)
    /*
    clickSound?.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f)

     */
    if (supportOpenGLES != 1) {
      val intent = Intent(this, LevelsActivity::class.java)
      intent.putExtra("versionGLES", supportOpenGLES)
      startActivity(intent)
    } else {
      showSnackbar(view, getString(R.string.opengl_not_support))
      //showToast(getApplicationContext(), "Нет поддержки OpenGL");
      //showCustomToast(this, "Нет поддержки OpenGL");
    }
  }

  fun settingsMenu(view: View) {
    /*
    clickSound?.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f)

     */
    val intent = Intent(this, SettingsActivity::class.java)
    startActivity(intent)
  }

  fun aboutGame(view: View) {
    /*
    clickSound?.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f)

     */
    val intent = Intent(this, AboutGameActivity::class.java)
    startActivity(intent)
  }

  // выйти из приложения при нажатии кнопки "Выход"
  fun exitFromApplication(view: View) {
    /*
    clickSound?.play(soundPoolMap.get(0), 1.0f, 1.0f,
            0, 0, 1f)

     */
    backToHome(this)
  }

  /*
   * Если кнопка выхода в навигационном меню, нажата в главном активити, - выйти из приложения, а не
   * возвращаться к другим активити (в том числе к игровому активити).
   */
  override fun onBackPressed() = backToHome(this)

  /**
   * Если нет поддержки второй или третьей версии OpenGL - вывести
   * соответствующее сообщение, иначе запустить GameActivity или LevelActivity.
   */
  private fun startGameActivity(activity: Class<out AppCompatActivity>, view: View) {
    if (supportOpenGLES != 1) {
      val intent = Intent(this, activity)
      intent.putExtra("versionGLES", supportOpenGLES)
      intent.putExtra("Level", 1)
      startActivity(intent)
    } else {
      showSnackbar(view, getString(R.string.opengl_not_support))
      //showToast(getApplicationContext(), "Нет поддержки OpenGL");
      //showCustomToast(this, "Нет поддержки OpenGL");
    }
  }
  // метод можно и пользовать для запуска различных активити
  // private fun startActivityForGame(activity: Class<out AppCompatActivity>, view: View) {}

  /** слушатель для правой кнопки activity bar */
  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_exit -> {
      backToHome(this)
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  /** добавить меню - в данном слуае это просто кнопка exit справа */
  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_layout, menu)
    return super.onCreateOptionsMenu(menu)
  }

  /** При нажатии на кнопку начала игры будет загружаться максимальный открытый уровень */
  private fun getCurrentOpenLevel(): Int {
    // подключиться к базе в потоке пользовательского интерфейса
    val dbLevels = Room.databaseBuilder(this.applicationContext, LevelDatabase::class.java, "levels-database").allowMainThreadQueries().build()
    val dao = dbLevels.levelDao()
    return when {
      dao.findByNumber(5).isOpen -> 5
      dao.findByNumber(4).isOpen -> 4
      dao.findByNumber(3).isOpen -> 3
      dao.findByNumber(2).isOpen -> 2
      dao.findByNumber(1).isOpen -> 1
      else -> 1
    }
  }

  private fun hideLoadPanel() {
    // не работать с панелью загрузки в MainActivity для API 14-23, поскольку
    // для ранних версий панель загрузки отображается в GameActivity
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
    // при старте активити сделать окно загрузки невидимым
    loadPanel?.setVisibility(View.INVISIBLE)
    // сделать тулбар видимым
    toolbar?.visibility = View.VISIBLE
    // сделать видимой панель статуса
    this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
  }

  private fun showLoadPanel() {
    // не работать с панелью загрузки в MainActivity для API 14-23, поскольку
    // для ранних версий панель загрузки отображается в GameActivity
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) return
    // убрать строку статуса вверху
    this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    loadPanel?.bringToFront() // переместить панель загрузки на передний план
    loadPanel?.requestLayout() // чтобы работало на Android 4.1.1

    toolbar?.visibility = View.INVISIBLE // сделать тулбар невидимым

    // установить для надписи загружаемого уровня - текущий уровень
    val loadLevelText = findViewById<TextView>(R.id.load_level_text)
    val currentLevel = getString(R.string.level) + " " + getCurrentOpenLevel()
    loadLevelText.text = currentLevel // вывести на экран загрузки название текущего уровня

    val loadImage = findViewById<ImageView>(R.id.image_process)
    loadImage.setBackgroundResource(R.drawable.animation_process)
    // отобразить окно загрузки в отдельном AsyncTask
    LoadingPanel(this, loadPanel, loadImage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
  }
}
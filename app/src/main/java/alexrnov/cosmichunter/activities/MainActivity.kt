package alexrnov.cosmichunter.activities

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity // пакет v7 также поддерживает минимальный уровень API 14 (Android 4.0)
import android.os.Bundle
import android.util.Log
import android.view.View

import alexrnov.cosmichunter.R

import alexrnov.cosmichunter.Initialization.checkMusicForStartMainActivity
import alexrnov.cosmichunter.Initialization.checkMusicForStopMainActivity
import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.base.AppDatabase
import alexrnov.cosmichunter.base.Level
import alexrnov.cosmichunter.base.LevelDatabase
import alexrnov.cosmichunter.base.User
import alexrnov.cosmichunter.utils.backToHome
import alexrnov.cosmichunter.utils.showSnackbar
import android.os.AsyncTask
import android.view.Menu
import android.view.MenuItem
import androidx.room.Room
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

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

    AsyncTask.execute {
      // .allowMainThreadQueries() - разрешить создавать БД в потоке пользовательского интерфейса
      // val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()

      val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").build()
      val v = db.userDao()

      val user: User? = v.findByName("Bob", "N2")

      /*
      if (user == null) {
        Log.i(TAG, "user == null")
        val newUser = User()
        newUser.uid = 2
        newUser.firstName = "Bob"
        newUser.lastName = "N2"
        v.insertAll(newUser)
      } else {
        Log.i(TAG, "user != null")
        Log.i(TAG, "firstName = " + user.firstName + " lastName = " + user.lastName)
        //user.uid = 3
        //user.firstName = "Bob2"
        //user.lastName = "N4"
        //v.insertAll(user)
      }
      */
      val users: MutableList<User> = v.getAll()
      for (u in users) {
        Log.i(TAG, "u = ${u.firstName} ${u.lastName} ${u.uid}")
        //u.uid = 10
      }
      //v.updateUser(3, "Bernard")

      val currentDBPath = getDatabasePath("levels-database.db").absolutePath
      val file = File(currentDBPath)
      if (file.exists()) {
        Log.i(TAG, "file is create")
      } else {
        Log.i(TAG, "file is not create")
      }
      /*
      val pathes = Paths.get(currentDBPath)
      if (Files.exists()) {

      }
      */

      Log.i(TAG, "currentDBPath = $currentDBPath")
      val db2 = Room.databaseBuilder(applicationContext, LevelDatabase::class.java, "levels-database").build()

      val v2 = db2.levelDao()
      val size = v2.all.size
      if (size == 0) { // база создается в первый раз
        val level1 = Level(0, "level1", true)
        val level2 = Level(1, "level2", false)
        val level3 = Level(2, "level3", false)
        val level4 = Level(3, "level4", false)
        val level5 = Level(4, "level5", false)
        v2.insertAll(level1, level2, level3, level4, level5)
      } else {
        //v2.updateLevel("level2", true)
        val levels = v2.all
        for (lev in levels) {
          Log.i(TAG, "${lev.id} ${lev.levelName} ${lev.isOpen}")
        }
      }
      Log.i(TAG, "size = $size")
    }
    /*
    // .allowMainThreadQueries() - разрешить создавать БД в потоке пользовательского интерфейса
    val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-name").allowMainThreadQueries().build()
    val v = db.userDao()


    val user: User? = v.findByName("Bob", "N2")

    if (user == null) {
      Log.i(TAG, "user == null")
      val newUser = User()
      newUser.uid = 2
      newUser.firstName = "Bob"
      newUser.lastName = "N2"
      v.insertAll(newUser)
    } else {
      Log.i(TAG, "user != null")
      Log.i(TAG, "firstName = " + user.firstName + " lastName = " + user.lastName)
      //user.uid = 3
      //user.firstName = "Bob2"
      //user.lastName = "N4"
      //v.insertAll(user)
    }

    val users: MutableList<User> = v.getAll()
    for (u in users) {
      Log.i(TAG, "u = ${u.firstName} ${u.lastName} ${u.uid}")
      //u.uid = 10
    }
    */
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

  fun exitFromApplication(view: View) = backToHome(this) // выйти из приложения

  /*
   * Если кнопка выхода в навигационном меню, нажата в главном активити, - выйти из приложения, а не
   * возвращаться к другим активити (в том числе к игровому активити).
   */
  override fun onBackPressed() = backToHome(this)

  /**
   * Если нет поддержки второй или третьей версии OpenGL - вывести соответствующее сообщение,
   * иначе запустить GameActivity или LevelsActivity.
   */
  private fun startActivityForGame(activity: Class<out AppCompatActivity>, view: View) {
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

  /** Слушатель для правой кнопки activity bar */
  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.action_exit -> {
      backToHome(this)
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_layout, menu)
    return super.onCreateOptionsMenu(menu)
  }
}

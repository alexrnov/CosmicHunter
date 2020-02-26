package alexrnov.cosmichunter.base

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class LevelDaoTest {
  private lateinit var levelDao: LevelDao
  private lateinit var db: LevelDatabase

  @Before
  fun createDb() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, LevelDatabase::class.java).build()
    levelDao = db.levelDao()
  }

  @After
  @Throws(IOException::class)
  fun closeDb() {
    db.close()
  }

  @Test
  @Throws(Exception::class)
  fun writeAndReadLevel() {
    val level5 = Level(4, 5, "level5", true)
    levelDao.insertAll(level5)
    val retrieverLevel = levelDao.findByNumber(5)
    assertThat(level5).isEqualTo(retrieverLevel)
    // утверждение выше заменяет набор утверждений ниже
    /*
    assertEquals(4, retrieverLevel.id)
    assertEquals(5, retrieverLevel.number)
    assertEquals("level5", retrieverLevel.levelName)
    assertEquals(true, retrieverLevel.isOpen)
    */
  }

  @Test
  @Throws(Exception::class)
  fun updateLevel() {
    val level5 = Level(4, 5, "level5", true)
    levelDao.insertAll(level5)
    levelDao.updateLevel("level5", false)
    level5.isOpen = false
    val retrieverLevel = levelDao.findByNumber(5)
    assertThat(level5).isEqualTo(retrieverLevel)
  }

  @Test
  @Throws(Exception::class)
  fun getAll() {
    val level1 = Level(0, 1, "level1", true)
    val level2 = Level(1, 2, "level2", false)
    val level3 = Level(2, 3, "level3", false)
    val level4 = Level(3, 4, "level4", false)
    val level5 = Level(4, 5, "level5", false)
    levelDao.insertAll(level1, level2, level3, level4, level5)
    assertThat(levelDao.all.size).isEqualTo(5)
    assertThat(levelDao.all[0]).isEqualTo(level1)
    assertThat(levelDao.all[1]).isEqualTo(level2)
    assertThat(levelDao.all[2]).isEqualTo(level3)
    assertThat(levelDao.all[3]).isEqualTo(level4)
    assertThat(levelDao.all[4]).isEqualTo(level5)
  }
}
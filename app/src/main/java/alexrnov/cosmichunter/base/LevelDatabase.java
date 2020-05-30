package alexrnov.cosmichunter.base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// exportSchema = false чтобы не возникала ошибка при сборке apk
@Database(entities = {Level.class}, version = 1, exportSchema = false)
public abstract class LevelDatabase extends RoomDatabase {
  public abstract LevelDao levelDao();
}

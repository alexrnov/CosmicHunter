package alexrnov.cosmichunter.base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Level.class}, version = 1)
public abstract class LevelDatabase extends RoomDatabase {
  public abstract LevelDao levelDao();
}

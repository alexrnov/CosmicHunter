package alexrnov.cosmichunter.base;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LevelDao {
  @Query("SELECT * FROM Level")
  List<Level> getAll();

  @Query("SELECT * FROM level WHERE id IN (:levelIds)")
  List<Level> loadAllByIds(int[] levelIds);

  @Query("SELECT * FROM level WHERE id LIKE :levelId AND " +
          "level_name LIKE :nameLevel LIMIT 1")
  Level findByName(int levelId, String nameLevel);

  @Query("SELECT * FROM level WHERE number LIKE :numberLevel")
  Level findByNumber(int numberLevel);

  @Insert
  void insertAll(Level... levels);

  @Delete
  void delete(Level level);

  @Query("UPDATE level SET is_open = :isOpen WHERE level_name = :levelName")
  void updateLevel(String levelName, boolean isOpen);
}

package alexrnov.cosmichunter.base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Level {
  @PrimaryKey
  public int id;

  @ColumnInfo(name = "level_name")
  public String levelName;

  @ColumnInfo(name = "is_open")
  public boolean isOpen;

  public Level(int id, String levelName, boolean isOpen) {
    this.id = id;
    this.levelName = levelName;
    this.isOpen = isOpen;
  }
}

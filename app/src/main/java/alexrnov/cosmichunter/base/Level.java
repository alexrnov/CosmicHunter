package alexrnov.cosmichunter.base;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Level {
  @PrimaryKey
  public int id;

  @ColumnInfo(name = "number")
  public int number;

  @ColumnInfo(name = "level_name")
  public String levelName;

  @ColumnInfo(name = "is_open")
  public boolean isOpen;

  public Level(int id, int number, String levelName, boolean isOpen) {
    this.id = id;
    this.levelName = levelName;
    this.isOpen = isOpen;
    this.number = number;
  }

  @Override
  public boolean equals(Object otherObject) {
    if (this == otherObject) return true;
    if (otherObject == null) return false;
    if (getClass() != otherObject.getClass()) return false;

    Level otherLevel = (Level) otherObject;

    return (id == otherLevel.id) && (levelName.equals(otherLevel.levelName))
            && (isOpen == otherLevel.isOpen) && (number == otherLevel.number);
  }
}

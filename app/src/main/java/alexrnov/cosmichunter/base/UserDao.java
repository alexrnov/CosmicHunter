package alexrnov.cosmichunter.base;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
  @Query("SELECT * FROM User")
  List<User> getAll();

  @Query("SELECT * FROM user WHERE uid IN (:userIds)")
  List<User> loadAllByIds(int[] userIds);

  @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
          "last_name LIKE :last LIMIT 1")
  User findByName(String first, String last);

  @Insert
  void insertAll(User... users);

  @Delete
  void delete(User user);

  @Query("UPDATE user SET first_name = :name WHERE uid = :userIds")
  void updateUser(int userIds, String name);
}

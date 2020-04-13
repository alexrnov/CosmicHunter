package alexrnov.cosmichunter;

import android.os.AsyncTask;

import java.util.HashMap;

import alexrnov.cosmichunter.activities.AsyncResponse;
import alexrnov.cosmichunter.base.LevelDao;
import alexrnov.cosmichunter.base.LevelDatabase;
import androidx.room.Room;

public class DefineOpenLevels extends AsyncTask<Void, Void, Void> {

  private AsyncResponse delegate;
  private HashMap<String, Boolean> levels = new HashMap<>();

  public DefineOpenLevels(AsyncResponse delegate) {
    this.delegate = delegate;
  }

  @Override
  protected void onPreExecute() {
  }

  @Override
  protected Void doInBackground(Void... voids) {
    LevelDatabase dbLevels = Room.databaseBuilder(delegate.getContext(), LevelDatabase.class, "levels-database").build();
    LevelDao dao = dbLevels.levelDao();
    levels.put("level5", dao.findByNumber(5).isOpen);
    levels.put("level4", dao.findByNumber(4).isOpen);
    levels.put("level3", dao.findByNumber(3).isOpen);
    levels.put("level2", dao.findByNumber(2).isOpen);
    levels.put("level1", dao.findByNumber(1).isOpen);
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    delegate.processFinish(levels);
  }
}

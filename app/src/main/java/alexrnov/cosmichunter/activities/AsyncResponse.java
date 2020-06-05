package alexrnov.cosmichunter.activities;

import android.content.Context;
import java.util.HashMap;

/**
 * Интерфейс для передачи коллекции, где ключи - это названия уровней,
 * значения - статус: уровень открыт(true)/закрыт(false)
 */
public interface AsyncResponse {
  void processFinish(HashMap<String, Boolean> levels);
  Context getContext();
}

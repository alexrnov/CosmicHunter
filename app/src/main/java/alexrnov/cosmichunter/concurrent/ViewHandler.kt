package alexrnov.cosmichunter.concurrent

import alexrnov.cosmichunter.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

/** Количество попаданий */
const val HITS_CODE = 0
/** Количество оставшихся ракет */
const val ROCKETS_CODE = 1
/** Сообщение внизу экрана уровень пройден или уровень не пройден */
const val MESSAGE_CODE = 2
/** Оставшееся время */
const val TIME_CODE = 3
/** Панель загрузки */
const val LOAD_GAME_CODE = 4

class ViewHandler(
        looper: Looper,
        private val context: Context,
        private val loadPanel: ConstraintLayout,
        private val hits: TextView,
        private val rockets: TextView,
        private val message: TextView,
        private val time: TextView): Handler(looper) {

  /*
   * система Android вызывает этот метод, когда получает новое сообщение
   * для потока, которым управляет. Все объекты Handler для определенного
   * потока получают одно и то же сообщение.
   */
  @SuppressLint("SetTextI18n")
  override fun handleMessage(inputMessage: Message) {
    // получение значения из входящего сообщения
    val text: String = inputMessage.obj as String
    // обновить один из объектов пользовательского интерфейса
    when (inputMessage.what) {
      HITS_CODE -> hits.text = "h:$text/50" // обновить количество попаданий
      ROCKETS_CODE -> rockets.text = "r:$text" // обновить количество оставшихся ракет
      MESSAGE_CODE -> {
        if (text == "level passed") { // уровень пройден
          message.text = context.getString(R.string.level_passed)
          message.setTextColor(Color.parseColor("#00a8f3")) // синий цвет
        } else { // уровень не пройден
          message.text = context.getString(R.string.level_not_passed)
          // если уровень не пройден - выделить текст красным цветом
          message.setTextColor(Color.parseColor("#f37500"))
        }
      }
      TIME_CODE -> time.text = text // обновить показатели времени
      LOAD_GAME_CODE -> {
        // убрать панель загрузки игры после загрузки игры
        loadPanel.visibility = View.INVISIBLE
      }
    }
  }
}
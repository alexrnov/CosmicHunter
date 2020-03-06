package alexrnov.cosmichunter.concurrent

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

const val HITS_CODE = 0
const val ROCKETS_CODE = 1
const val MESSAGE_CODE = 2
const val TIME_CODE = 3
const val LOAD_GAME_CODE = 4

class ViewHandler(
        looper: Looper,
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
    // обновить один из объектов интерфейса
    when (inputMessage.what) {
      HITS_CODE -> hits.text = "h:$text/50" // обновить количество попаданий
      ROCKETS_CODE -> rockets.text = "r:$text" // обновить количество оставшихся ракет
      MESSAGE_CODE -> {
        message.text = text
        message.setTextColor(Color.parseColor("#00a8f3"))
      }
      TIME_CODE -> time.text = text // обновить показатели времени
      LOAD_GAME_CODE -> {
        loadPanel.visibility = View.INVISIBLE
      } // убрать (сделать прозрачной) панель загрузки игры
    }
  }
}
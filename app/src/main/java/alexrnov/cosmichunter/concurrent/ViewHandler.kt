package alexrnov.cosmichunter.concurrent

import alexrnov.cosmichunter.base.LevelDatabase
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import androidx.room.Room

const val HITS_CODE = 0
const val ROCKETS_CODE = 1
const val MESSAGE_CODE = 2
const val TIME_CODE = 3

class ViewHandler(
        looper: Looper,
        private val hits: TextView,
        private val rockets: TextView,
        private val message: TextView,
        private val time: TextView): Handler(looper) {

  /*
   * система Android вызывает этот метод, когда получает новое сообщение
   * для потока, которым управляет. Все объекты Handler для определенного
   * потока получают одно и то же сообщение.
   */
  override fun handleMessage(inputMessage: Message) {
    // получение значения из входящего сообщения
    val text: String = inputMessage.obj as String
    // обновить один из объектов интерфейса
    when (inputMessage.what) {
      HITS_CODE -> hits.text = text
      ROCKETS_CODE -> rockets.text = text
      MESSAGE_CODE -> {
        message.text = text
        message.setTextColor(Color.parseColor("#00a8f3"))
      }
      TIME_CODE -> time.text = text
    }
  }
}
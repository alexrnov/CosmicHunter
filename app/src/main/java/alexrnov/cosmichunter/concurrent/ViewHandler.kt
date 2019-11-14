package alexrnov.cosmichunter.concurrent

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView

const val POINTS_CODE = 0
const val ROCKETS_CODE = 1

class ViewHandler(looper: Looper, val points: TextView, val rockets: TextView):
        Handler(looper) {
  /*
   * система Android вызывает этот метод, когда получает новое сообщение
   * для потока, которым управляет. Все объекты Handler для определенного
   * потока получают одно и то же сообщение.
   */
  override fun handleMessage(inputMessage: Message) {
    // получение значения из входящего сообщения
    val text: String = inputMessage.obj as String
    when (inputMessage.what) {
      POINTS_CODE -> points.text = text
      ROCKETS_CODE -> rockets.text = text
    }
  }
}
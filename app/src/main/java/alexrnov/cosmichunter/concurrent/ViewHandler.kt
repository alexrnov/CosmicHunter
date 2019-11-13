package alexrnov.cosmichunter.concurrent

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.TextView

class ViewHandler(looper: Looper, val textView: TextView): Handler(looper) {

  /*
   * система Android вызывает этот метод, когда получает новое сообщение
   * для потока, которым управляет. Все объекты Handler для определенного
   * потока получают одно и то же сообщение.
   */
  override fun handleMessage(inputMessage: Message) {
    // получение значения из входящего сообщения
    Log.v("P", "handleMessage")
    val s: String = inputMessage.obj as String
    val v: Int = inputMessage.what as Int
    Log.v("P", "s = $s, v = $v")
    textView.text = s
  }
}
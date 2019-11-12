package alexrnov.cosmichunter.concurrent

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log

class ViewHandler(looper: Looper): Handler(looper) {
  override fun handleMessage(inputMessage: Message) {
    Log.v("P", "handleMessage")
  }
}
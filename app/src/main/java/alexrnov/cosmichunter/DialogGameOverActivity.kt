package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import android.app.Activity
import android.os.Bundle
import android.util.Log

class DialogGameOverActivity: Activity() {

  val className = (this.javaClass.simpleName?:"DialogGameOverActivity") + ".class: "

  override fun onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "$className onCreate()")
  }
}
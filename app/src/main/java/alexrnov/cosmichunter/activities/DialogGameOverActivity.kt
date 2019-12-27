package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.R
import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_dialog.*

class DialogGameOverActivity: Activity() {

  val className = this.javaClass.simpleName + ".class: "

  override fun onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_dialog)
    button_continue_game.text = ""
    Log.i(TAG, "$className onCreate()")
  }
}
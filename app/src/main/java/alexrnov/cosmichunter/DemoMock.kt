package alexrnov.cosmichunter

import android.content.Context

class ClassForDemoMock(val context: Context) {
  fun printApplicationInfo() = context.getString(R.string.app_name)
}
package alexrnov.cosmichunter

import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.Initialization.sp
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import android.util.Log

@Suppress("DEPRECATION")
class VibratorExplosion(context: Context, var time: Long = 50) {

  var v: Vibrator =
          context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

  // пустая функция выполняется, если вибрация отключена в настройках приложения
  var f: () -> Unit = { }

  init {
    val defaultValue: String = context.resources.getString(R.string.default_vibration)
    val currentVibration = sp.getString("vibration", defaultValue)
    Log.i(TAG, "${this.javaClass.simpleName + ".class"}: vibration = $currentVibration")

    if (currentVibration == defaultValue) { // если вибрация включена
      // если текущая версия - 26 (VERSION_CODES.O) или более поздняя
      f = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        { v.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE)) }
      } else { // если текущая версия меньше 26
        { v.vibrate(time) } //deprecated в API 26 и более поздних
      }
    }
  }

  /**
   * Произвести вибрацию. Если вибрация отключена, тогда выполняется
   * пустая функция. Использование лямда-выражения, в данном случае,
   * позваляет избежать условной проверки включения вибрации при
   * перерисовке каждого кадра
   */
  fun execute() { f() }
}
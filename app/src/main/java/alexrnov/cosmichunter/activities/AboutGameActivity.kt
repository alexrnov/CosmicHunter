package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.utils.getScreenSize
import alexrnov.cosmichunter.utils.printDPSizeScreen
import android.os.Build
import android.view.ViewTreeObserver


class AboutGameActivity: AppCompatActivity() {
  private var textView: TextView? = null
  private var textView2: TextView? = null
  private var textView3: TextView? = null
  private var middleWidth: Int = 0

  override fun onCreate(bundle: Bundle?) {
    super.onCreate(bundle)
    setContentView(R.layout.activity_about)
    textView = findViewById(R.id.textView_about)
    textView2 = findViewById(R.id.textView_about2)
    textView3 = findViewById(R.id.textView_about3)

    //getScreenSize(this.applicationContext)
    val (width, height) = printDPSizeScreen(this)
    middleWidth = width / 2

    Log.i(TAG, "width = $width, height = $height, middleWidth = $middleWidth")
    textView3?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          Log.i(TAG, "onGlobalLayout 1")
          textView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        } else {
          Log.i(TAG, "onGlobalLayout 2")
          @Suppress("DEPRECATION")
          textView?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
        }

        var w = textView?.width //height is ready
        Log.i(TAG, "w2 = $w")
        w = textView2?.width
        Log.i(TAG, "w3 = $w")
        w = textView3?.width
        Log.i(TAG, "w4 = $w")

        animationViews(middleWidth)
      }
    })
  }

  private fun animationViews(middleWidth: Int) {

    val animation: ValueAnimator = ValueAnimator.ofFloat(0f, middleWidth - 129f)
    animation.duration = 300
    animation.addUpdateListener { updateAnimation ->
      textView?.translationX = updateAnimation.animatedValue as Float
    }


    val animation2: ValueAnimator = ValueAnimator.ofFloat(0f, middleWidth - 129f)
    animation2.duration = 300
    animation2.addUpdateListener { updateAnimation ->
      textView2?.translationX = updateAnimation.animatedValue as Float
    }

    val animation3: ValueAnimator = ValueAnimator.ofFloat(0f, middleWidth - 129f)
    animation3.duration = 300
    animation3.addUpdateListener { updateAnimation ->
      textView3?.translationX = updateAnimation.animatedValue as Float
    }

    val animatorSet = AnimatorSet()
    animatorSet.play(animation).before(animation2)
    animatorSet.play(animation3).after(animation2)
    animatorSet.start()
    //animation.start()
    //animation2.start()
  }
}
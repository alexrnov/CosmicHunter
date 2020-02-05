package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import alexrnov.cosmichunter.Initialization.TAG
import alexrnov.cosmichunter.utils.getScreenSizeWithNavBar
import alexrnov.cosmichunter.utils.getScreenSizeWithoutNavBar
import android.os.Build
import android.view.ViewTreeObserver


class AboutGameActivity: AppCompatActivity() {
  private var musicText: TextView? = null
  private var musicLink: TextView? = null
  private var textureText: TextView? = null
  private var textureLink: TextView? = null
  private var blenderText: TextView? = null
  private var blenderLink: TextView? = null

  private var middleWidth: Int = 0

  override fun onCreate(bundle: Bundle?) {
    super.onCreate(bundle)
    setContentView(R.layout.activity_about)
    musicText = findViewById(R.id.musicText)
    musicLink = findViewById(R.id.musicLink)
    textureText = findViewById(R.id.textureText)
    textureLink = findViewById(R.id.textureLink)
    blenderText = findViewById(R.id.blenderText)
    blenderLink = findViewById(R.id.blenderLink)
    //getScreenSize(this.applicationContext)
    val (width, _) = getScreenSizeWithoutNavBar(this)
    middleWidth = width / 2
    //Log.i(TAG, "width = $width, height = $height, middleWidth = $middleWidth")
    blenderText?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          musicText?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        } else {
          @Suppress("DEPRECATION")
          musicText?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
        }
        animationViews(middleWidth)
      }
    })
  }

  private fun animationViews(middleWidth: Int) {
    val animation = ValueAnimator.ofFloat(0f, middleWidth - halfView(musicText))
    animation.duration = 150
    animation.addUpdateListener { updateAnimation ->
      musicText?.translationX = updateAnimation.animatedValue as Float
    }

    val animation2 = ValueAnimator.ofFloat(0f, middleWidth - halfView(textureText))
    animation2.duration = 150
    animation2.addUpdateListener { updateAnimation ->
      textureText?.translationX = updateAnimation.animatedValue as Float
    }

    val animation3 = ValueAnimator.ofFloat(0f, middleWidth - halfView(blenderText))
    animation3.duration = 150
    animation3.addUpdateListener { updateAnimation ->
      blenderText?.translationX = updateAnimation.animatedValue as Float
    }

    val animatorSet = AnimatorSet()
    animatorSet.play(animation).before(animation2)
    animatorSet.play(animation3).after(animation2)
    animatorSet.start()
    //animation.start()
    //animation2.start()
  }

  private fun halfView(view: TextView?) = ((view?.width ?: 0).toFloat() / 2)
}
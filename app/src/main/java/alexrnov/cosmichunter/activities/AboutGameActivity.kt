package alexrnov.cosmichunter.activities

import alexrnov.cosmichunter.R
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import alexrnov.cosmichunter.utils.getScreenSizeWithoutNavBar
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.ViewTreeObserver

class AboutGameActivity: AppCompatActivity() {
  private var musicText: TextView? = null
  private var musicLink: TextView? = null
  private var soundText: TextView? = null
  private var soundLink: TextView? = null
  private var textureText: TextView? = null
  private var textureLink: TextView? = null
  private var pictureText: TextView? = null
  private var pictureLink: TextView? = null
  private var blenderText: TextView? = null
  private var blenderLink: TextView? = null

  private var middleWidth: Int = 0

  override fun onCreate(bundle: Bundle?) {
    super.onCreate(bundle)
    // в манифесте не указывается ориентация для этого активити, что бы
    // не было ощибки illegalstateexception-only-fullscreen-opaque-activities-can-reques
    // в версиях Oreo и более поздних. Вместо этого ориентация указывается в коде
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
    setContentView(R.layout.activity_about)
    musicText = findViewById(R.id.musicText)
    musicLink = findViewById(R.id.musicLink)
    soundText = findViewById(R.id.soundText)
    soundLink = findViewById(R.id.soundLink)
    textureText = findViewById(R.id.textureText)
    textureLink = findViewById(R.id.textureLink)
    pictureText = findViewById(R.id.pictureText)
    pictureLink = findViewById(R.id.pictureLink)
    blenderText = findViewById(R.id.blenderText)
    blenderLink = findViewById(R.id.blenderLink)
    val (width, _) = getScreenSizeWithoutNavBar(this)
    middleWidth = width / 2
    blenderLink?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
          blenderLink?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        } else {
          @Suppress("DEPRECATION")
          blenderLink?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
        }
        animationViews()
      }
    })
  }

  private fun animationViews(d: Long = 270) {
    val animationMusicText = moveRightToCenter(musicText)

    val animationMusicLink = ValueAnimator.ofFloat(0f, halfView(musicLink))
    animationMusicLink.duration = d
    animationMusicLink.addUpdateListener {
      musicLink?.translationX = - (it.animatedValue as Float)
    }

    val alphaMusic = ValueAnimator.ofFloat(0f, 1f)
    alphaMusic.duration = d
    alphaMusic.addUpdateListener {
      musicText?.alpha = it.animatedValue as Float
      musicLink?.alpha = it.animatedValue as Float
    }

    val animationSoundText = moveRightToCenter(soundText)
    val animationSoundLink = ValueAnimator.ofFloat(0f, halfView(soundLink))
    animationSoundLink.duration = d
    animationSoundLink.addUpdateListener { soundLink?.translationX = - (it.animatedValue as Float) }

    val alphaSound = ValueAnimator.ofFloat(0f, 1f)
    alphaSound.duration = d
    alphaSound.addUpdateListener {
      soundText?.alpha = it.animatedValue as Float
      soundLink?.alpha = it.animatedValue as Float
    }

    val animationTextureText = moveRightToCenter(textureText)
    val animationTextureLink = ValueAnimator.ofFloat(0f, halfView(textureLink))
    animationTextureLink.duration = d
    animationTextureLink.addUpdateListener { textureLink?.translationX = - (it.animatedValue as Float) }

    val alphaTexture = ValueAnimator.ofFloat(0f, 1f)
    alphaTexture.duration = d
    alphaTexture.addUpdateListener {
      textureText?.alpha = it.animatedValue as Float
      textureLink?.alpha = it.animatedValue as Float
    }


    val animationPictureText = moveRightToCenter(pictureText)
    val animationPictureLink = ValueAnimator.ofFloat(0f, halfView(pictureLink))
    animationPictureLink.duration = d
    animationPictureLink.addUpdateListener { pictureLink?.translationX = - (it.animatedValue as Float) }

    val alphaPicture = ValueAnimator.ofFloat(0f, 1f)
    alphaPicture.duration = d
    alphaPicture.addUpdateListener {
      pictureText?.alpha = it.animatedValue as Float
      pictureLink?.alpha = it.animatedValue as Float
    }

    val animationBlenderText = moveRightToCenter(blenderText)
    val animationBlenderLink = ValueAnimator.ofFloat(0f, halfView(blenderLink))
    animationBlenderLink.duration = d
    animationBlenderLink.addUpdateListener { blenderLink?.translationX = - (it.animatedValue as Float) }

    val alphaBlender = ValueAnimator.ofFloat(0f, 1f)
    alphaBlender.duration = d
    alphaBlender.addUpdateListener {
      blenderText?.alpha = it.animatedValue as Float
      blenderLink?.alpha = it.animatedValue as Float
    }

    val animatorSet = AnimatorSet()
    animatorSet.play(animationMusicText).with(animationMusicLink)
    animatorSet.playTogether(animationMusicText, alphaMusic)
    animatorSet.play(animationMusicText).before(animationSoundText)

    animatorSet.play(animationSoundText).with(animationSoundLink)
    animatorSet.playTogether(animationSoundText, alphaSound)

    animatorSet.play(animationTextureText).after(animationSoundText)
    animatorSet.play(animationTextureText).with(animationTextureLink)
    animatorSet.playTogether(animationTextureText, alphaTexture)


    animatorSet.play(animationPictureText).after(animationTextureText)
    animatorSet.play(animationPictureText).with(animationPictureLink)
    animatorSet.playTogether(animationPictureText, alphaPicture)

    animatorSet.play(animationBlenderText).after(animationPictureText)
    animatorSet.play(animationBlenderText).with(animationBlenderLink)
    animatorSet.playTogether(animationBlenderText, alphaBlender)

    animatorSet.start()
  }

  private fun moveRightToCenter(textView: TextView?): ValueAnimator {
    val animation = ValueAnimator.ofFloat(0f, halfView(textView))
    animation.duration = 270L
    animation.addUpdateListener { textView?.translationX = it.animatedValue as Float }
    return animation
  }

  private fun halfView(view: TextView?) = middleWidth - ((view?.width ?: 0).toFloat() / 2)
}
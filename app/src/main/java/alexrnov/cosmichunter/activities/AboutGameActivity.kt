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
  private val duration = 270L

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

    /**
     * Провести анимацию после измерения и отображения на экране.
     * После этого, можно измерять размеры самих элементов интерфейса.
     */
    blenderLink?.post { animationViews() }
    //  Метод post используется вместо подхода с viewTreeObserver, который приведен ниже
    /*
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
    */
  }

  private fun animationViews() {
    val animationMusicText = moveToCenter(musicText, true)
    val animationMusicLink = moveToCenter(musicLink, false)
    val animationSoundText = moveToCenter(soundText, true)
    val animationSoundLink = moveToCenter(soundLink, false)
    val animationTextureText = moveToCenter(textureText, true)
    val animationTextureLink = moveToCenter(textureLink, false)
    val animationPictureText = moveToCenter(pictureText, true)
    val animationPictureLink = moveToCenter(pictureLink, false)
    val animationBlenderText = moveToCenter(blenderText, true)
    val animationBlenderLink = moveToCenter(blenderLink, false)

    val animatorSet = AnimatorSet() // задать порядок анимации
    animatorSet.play(animationMusicText).with(animationMusicLink)
    animatorSet.playTogether(animationMusicText, alphaForGroup(musicText, musicLink))
    animatorSet.play(animationMusicText).before(animationSoundText)

    animatorSet.play(animationSoundText).with(animationSoundLink)
    animatorSet.playTogether(animationSoundText, alphaForGroup(soundText, soundLink))

    animatorSet.play(animationTextureText).after(animationSoundText)
    animatorSet.play(animationTextureText).with(animationTextureLink)
    animatorSet.playTogether(animationTextureText, alphaForGroup(textureText, textureLink))

    animatorSet.play(animationPictureText).after(animationTextureText)
    animatorSet.play(animationPictureText).with(animationPictureLink)
    animatorSet.playTogether(animationPictureText, alphaForGroup(pictureText, pictureLink))

    animatorSet.play(animationBlenderText).after(animationPictureText)
    animatorSet.play(animationBlenderText).with(animationBlenderLink)
    animatorSet.playTogether(animationBlenderText, alphaForGroup(blenderText, blenderLink))

    animatorSet.start()
  }

  private fun moveToCenter(textView: TextView?, direct: Boolean): ValueAnimator {
    fun halfView(view: TextView?) = middleWidth - ((view?.width ?: 0).toFloat() / 2)
    val animation = ValueAnimator.ofFloat(0f, halfView(textView))
    animation.duration = duration
    if (direct) { // направление движения справа налево
      animation.addUpdateListener { textView?.translationX = it.animatedValue as Float }
    } else { // направление движения слева направо
      animation.addUpdateListener { textView?.translationX = - (it.animatedValue as Float) }
    }
    return animation
  }

  private fun alphaForGroup(textView1: TextView?, textView2: TextView?): ValueAnimator {
    val alpha = ValueAnimator.ofFloat(0f, 1f) // прозрачность от 0 до 100 %
    alpha.duration = duration
    alpha.addUpdateListener {
      textView1?.alpha = it.animatedValue as Float
      textView2?.alpha = it.animatedValue as Float
    }
    return alpha
  }
}
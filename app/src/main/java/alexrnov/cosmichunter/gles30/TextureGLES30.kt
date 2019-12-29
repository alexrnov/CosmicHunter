package alexrnov.cosmichunter.gles30

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import java.io.IOException
import java.io.InputStream

object TextureGLES30 {

  /**
   * Загружает текстуру из директории raw
   * [context] контекст приложения
   * [r] целочисленный идентификатор ресурса - тектстурного изображения
   */
  @JvmStatic
  fun loadTextureFromRaw(context: Context, r: Int): Int {
    val input: InputStream = context.resources.openRawResource(r) ?: return 0
    val bitmap: Bitmap = BitmapFactory.decodeStream(input)
    return loadTexture(bitmap)
  }

  /**
   * Загружает текстуру из директории asset
   * [context] контекст приложения
   * [fileName] имя файла, который расположен в директории asset
   */
  @JvmStatic
  fun loadTextureFromAsset(context: Context, fileName: String): Int {
    val input: InputStream = try {
      context.assets.open(fileName)
    } catch (ioe: IOException) { null } ?: return 0
    val bitmap: Bitmap = BitmapFactory.decodeStream(input)
    return loadTexture(bitmap)
  }

  private fun loadTexture(bitmap: Bitmap): Int {
    val textureId = IntArray(1)
    // создать текстурный объект. Первый параметр - количество текстурных
    // объектов, которые необходимо создать. Второй параметр - массив,
    // возвращающий n-чисел(в данном случае 1), идентифицируюших созданные
    // текстурные объекты
    GLES30.glGenTextures(1, textureId, 0)
    // привязать соответствующий текстурный блог к типу текстуры
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId[0])
    // загрузка изображения двумерной текстуры. Первый параметр - тип текстуры
    // второй параметр - задает загружаемый уровень в пирамиде. Первый уровень - 0
    // border - игнорируется.
    GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0)
    //задать режимы фильтрации
    //третий параметр - фильтр растяжения. GL_LINEAR - будет взято значение,
    //получаемое билинейной интерполяцией четырех значений, взятых из текстуры
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
            GLES30.GL_LINEAR)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
            GLES30.GL_LINEAR)
    //задать режимы отсечения текстурных координат, используются для
    //задания поведения в том случае, когда текстурная координата
    //оказывается вне диапазона [0.0, 1.0]
    //GL_CLAMP_TO_EDGE - привести к границе текстуры
    //другие варианты - GL_REPEAT, GL_MIRRORED_REPEAT
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,
            GLES30.GL_CLAMP_TO_EDGE)
    GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,
            GLES30.GL_CLAMP_TO_EDGE)

    // поддержка mip-карты
    // GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
    return textureId[0]
  }
}
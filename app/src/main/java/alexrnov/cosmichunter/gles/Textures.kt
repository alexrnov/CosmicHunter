package alexrnov.cosmichunter.gles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import java.io.IOException
import java.io.InputStream

object Textures {

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
    GLES20.glGenTextures(1, textureId, 0)
    // привязать соответствующий текстурный блок к типу текстуры
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0])
    // загрузка изображения двумерной текстуры. Первый параметр - тип текстуры
    // второй параметр - задает загружаемый уровень в пирамиде. Первый уровень - 0
    // border - игнорируется.
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    //задать режимы фильтрации
    //третий параметр - фильтр растяжения. GL_LINEAR - будет взято значение,
    //получаемое билинейной интерполяцией четырех значений, взятых из текстуры
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_LINEAR)
    //задать режимы отсечения текстурных координат, используются для
    //задания поведения в том случае, когда текстурная координата
    //оказывается вне диапазона [0.0, 1.0]
    //GL_CLAMP_TO_EDGE - привести к границе текстуры
    //другие варианты - GL_REPEAT, GL_MIRRORED_REPEAT
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE)
    return textureId[0]
  }

  /**
   * Загружает текстуру из директории raw с учетом настроек для mipmap
   * [context] контекст приложения
   * [r] целочисленный идентификатор ресурса - тектстурного изображения
   */
  @JvmStatic
  fun loadTextureWithMipMapFromRaw(context: Context, r: Int): Int {
    val input: InputStream = context.resources.openRawResource(r) ?: return 0
    val bitmap: Bitmap = BitmapFactory.decodeStream(input)
    return loadTextureWithMipmap(bitmap)
  }

  /**
   * Загружает текстуру из директории asset с учетом настроек для mipmap
   * [context] контекст приложения
   * [fileName] имя файла, который расположен в директории asset
   */
  @JvmStatic
  fun loadTextureWithMipMapFromAsset(context: Context, fileName: String): Int {
    val input: InputStream = try {
      context.assets.open(fileName)
    } catch (ioe: IOException) { null } ?: return 0
    val bitmap: Bitmap = BitmapFactory.decodeStream(input)
    return loadTextureWithMipmap(bitmap)
  }

  private fun loadTextureWithMipmap(bitmap: Bitmap): Int {
    val textureId = IntArray(1)
    // создать текстурный объект. Первый параметр - количество текстурных
    // объектов, которые необходимо создать. Второй параметр - массив,
    // возвращающий n-чисел(в данном случае 1), идентифицируюших созданные
    // текстурные объекты
    GLES20.glGenTextures(1, textureId, 0)
    // привязать соответствующий текстурный блок к типу текстуры
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0])
    // загрузка изображения двумерной текстуры. Первый параметр - тип текстуры
    // второй параметр - задает загружаемый уровень в пирамиде. Первый уровень - 0
    // border - игнорируется.
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
    // задать режимы фильтрации
    // установить формат фильтрации: первый параметр - тип текстуры
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST_MIPMAP_NEAREST)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
    // включить поддержку mipmap
    GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)
    return textureId[0]
  }





  /**
   * Загружает текстуру из директории asset
   * [context] контекст приложения
   * [fileName] имя файла, который расположен в директории asset
   */
  @JvmStatic
  fun loadTextureNearestFromAsset(context: Context, fileName: String): Int {
    val input: InputStream = try {
      context.assets.open(fileName)
    } catch (ioe: IOException) { null } ?: return 0
    val bitmap: Bitmap = BitmapFactory.decodeStream(input)
    return loadTextureNearest(bitmap)
  }

  private fun loadTextureNearest(bitmap: Bitmap): Int {
    val textureId = IntArray(1)
    // создать текстурный объект. Первый параметр - количество текстурных
    // объектов, которые необходимо создать. Второй параметр - массив,
    // возвращающий n-чисел(в данном случае 1), идентифицируюших созданные
    // текстурные объекты
    GLES20.glGenTextures(1, textureId, 0)
    // привязать соответствующий текстурный блок к типу текстуры
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0])
    // загрузка изображения двумерной текстуры. Первый параметр - тип текстуры
    // второй параметр - задает загружаемый уровень в пирамиде. Первый уровень - 0
    // border - игнорируется.
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)


    //задать режимы фильтрации
    // третий параметр - фильтр растяжения. GL_NEAREST - берется одно значение
    // из текстуры, соответствующее ближайшей текстурной координате

    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_NEAREST)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
            GLES20.GL_NEAREST)

    //задать режимы отсечения текстурных координат, используются для
    //задания поведения в том случае, когда текстурная координата
    //оказывается вне диапазона [0.0, 1.0]
    //GL_CLAMP_TO_EDGE - привести к границе текстуры
    //другие варианты - GL_REPEAT, GL_MIRRORED_REPEAT
    /*
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_REPEAT)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_REPEAT)
    */
    return textureId[0]
  }


  /**
   * Создает кубическую текстуру с различными изображениями для каждой из сторон
   * [xPos] raw-ресурс для GL_TEXTURE_CUBE_MAP_POSITIVE_X
   * [xNeg] raw-ресурс для GL_TEXTURE_CUBE_MAP_NEGATIVE_X
   * [yPos] raw-ресурс для GL_TEXTURE_CUBE_MAP_POSITIVE_Y
   * [yNeg] raw-ресурс для GL_TEXTURE_CUBE_MAP_NEGATIVE_Y
   * [zPos] raw-ресурс для GL_TEXTURE_CUBE_MAP_POSITIVE_Z
   * [zNeg] raw-ресурс для GL_TEXTURE_CUBE_MAP_NEGATIVE_Z
   */
  @JvmStatic
  fun createCubemapTexture(context: Context, xPos: Int, xNeg: Int,
                           yPos: Int, yNeg: Int, zPos: Int, zNeg: Int): Int {
    var input: InputStream = context.resources.openRawResource(xPos)
    val positiveX: Bitmap = BitmapFactory.decodeStream(input)

    input = context.resources.openRawResource(xNeg)
    val negativeX: Bitmap = BitmapFactory.decodeStream(input)

    input = context.resources.openRawResource(yPos)
    val positiveY: Bitmap = BitmapFactory.decodeStream(input)

    input = context.resources.openRawResource(yNeg)
    val negativeY: Bitmap = BitmapFactory.decodeStream(input)

    input = context.resources.openRawResource(zPos)
    val positiveZ: Bitmap = BitmapFactory.decodeStream(input)

    input = context.resources.openRawResource(zNeg)
    val negativeZ: Bitmap = BitmapFactory.decodeStream(input)

    val textureId = IntArray(1)

    //генерация объекта-текстуры
    //первый параметр - количество текстурных объектов, которые
    //нужно создать. Второй параметр - массив, в котором будут возвращены
    //n чисел, идентифицирующих созданные текстурные объекты. В момент
    //создания текстурный объект является пустым контейнером, который
    //будет использован для загрузки изображения и параметров текстуры
    GLES20.glGenTextures(1, textureId, 0)
    //связать объект-текстуру
    //После того как идентификатор текстурного объекта был создан при
    //помощи glGenTextures, приложение должно привязать соответствующий
    //текстурный объект для работы с ним (привязать объект к цели).
    //Первый параметр - привязывает текстурный объект к типу текстуры.
    GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId[0])

    GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, positiveX, 0)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, negativeX, 0)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, positiveY, 0)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, negativeY, 0)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, positiveZ, 0)
    GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, negativeZ, 0)

    //установить метод фильтрования
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
            GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
    GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,
            GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)

    GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_CUBE_MAP)
    return textureId[0]
  }
}
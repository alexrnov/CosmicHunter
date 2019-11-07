package alexrnov.cosmichunter.objects;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;

import alexrnov.cosmichunter.utils.gl30.LinkedProgram;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.utils.commonGL.Buffers.floatBuffer;
import static alexrnov.cosmichunter.utils.gl30.Texture.loadTextureFromAsset;

public class Explosion {
  private final byte NUM_COORDINATES = 3; // количество координат - 3 т.е. x, y, z
  private final byte FLOAT_SIZE = 4; // количество байт на тип float

  public float x, y = 0.0f; // координаты для центра взрыва
  private final Random random = new Random();
  private final int programObject;

  private final int lastTimeExplosionLink; // ссылка на uniform-переменную lastTimeExplosion
  private final int centerPositionLink; // ссылка на uniform-переменную centerPosition
  private final int colorLink;
  private final int sizeSpriteLink;
  private final int samplerLink; // ссылка на текстурный семплер
  private final int textureID;

  // данные вершин для частиц (время жизни, начальные и конечные координаты)
  private final float[] lifeTimeData; // время жизни
  private final float[] startPositionData; // начальные координаты
  private final float[] endPositionData; // конечные координаты
  private FloatBuffer lifeTimeAsFloatBuffer;
  private FloatBuffer startPositionAsFloatBuffer;
  private FloatBuffer endPositionAsFloatBuffer;
  private final int[] VBO = new int[3]; // массив буферов вершинных атрибутов
  // время, прошедшее с начала взрыва. При инициализации сделать его
  // значение больше чем время взрыва, чтобы взрыв не рендерился при
  // запуске приложения
  private float lastTimeExplosion = 2.0f;
  // флаг определяет произошел ли взрыв только что. Это нужно для того
  // чтобы установить координаты центра взрыва
  private boolean createExplosion = false;

  private final float startRadius; // начальный радиус взрыва
  private final float endRadius; // конечный радиус взрыва
  private final float sizeSprite; // размер точечного спрайта
  private final int numberParticles; // количество частиц
  private final float[] color; // цвет взрыва

  private List<Explosion> explosions;

  public Explosion(Context context, String textureFile) {
    this(context, textureFile, 0.05f,
            0.6f, 100.0f, 150, new float[] {1.0f, 0.7f, 0.1f, 1.0f});
  }

  public Explosion(Context context, String textureFile, float startRadius,
                   float endRadius, float sizeSprite, int numberParticles, float[] color) {
    this.startRadius = startRadius;
    this.endRadius = endRadius;
    this.sizeSprite = sizeSprite;
    this.numberParticles = numberParticles;
    this.color = color;

    lifeTimeData = new float[numberParticles];
    startPositionData = new float[numberParticles * NUM_COORDINATES];
    endPositionData = new float[numberParticles * NUM_COORDINATES];

    LinkedProgram linkProgram = new LinkedProgram(context,
            "shaders/explosion_v.glsl",
            "shaders/explosion_f.glsl");
    programObject = linkProgram.get();

    if (programObject == 0) {
      Log.v(TAG, "error program link explosion: " + programObject);
    }

    lastTimeExplosionLink = GLES30.glGetUniformLocation(programObject, "u_lastTimeExplosion");
    centerPositionLink = GLES30.glGetUniformLocation(programObject, "u_centerPosition");
    sizeSpriteLink = GLES30.glGetUniformLocation(programObject, "u_sizeSprite");
    colorLink = GLES30.glGetUniformLocation(programObject, "u_color");
    samplerLink = GLES30.glGetUniformLocation(programObject, "s_texture");

    textureID = loadTextureFromAsset(context, textureFile);
    Log.v(TAG, this.getClass().getSimpleName() + ".class: " +
            "lastTimeExplosionLink: " + lastTimeExplosionLink + "; centerPositionLink: " +
            centerPositionLink + "; sizeSpriteLink: " + sizeSpriteLink + "; colorLink: " +
            colorLink + "; samplerLink: " + samplerLink + "; textureID: " + textureID);
  }

  /**
   * Метод генерирует данные для каждой частицы, такие как: время жизни,
   * начальные и конечные координаты. Эти данные затем будут
   * передаваться в вершинный шейдер как вершинные атрибуты. Метод
   * также создает вершинные буферы для атрибутов вершин
   * @param width - ширина экрана
   * @param height - высота экрана
   */
  public void createDataVertex(int width, int height) {
    GLES30.glViewport(0, 0, width, height);
    float aspect = (float) height / (float) width;

    // генерация времени жизни для частиц
    float maxLifeTime = 3.0f; // максимальное время жизни частицы
    for (int i = 0; i < numberParticles; i ++) {
      //время жизни частицы - случайное значение (0 - 3 секунды)
      lifeTimeData[i] = random.nextFloat() * maxLifeTime;
    }

    // генерация начальных и конечных координат для частиц
    float[] xyz;
    for (int i = 0; i < numberParticles * NUM_COORDINATES; i += NUM_COORDINATES) {
      xyz = getPointForSphere(startRadius); // начальная позиция частицы
      startPositionData[i] = xyz[0] * aspect;
      startPositionData[i + 1] = xyz[1];
      startPositionData[i + 2] = xyz[2];
      xyz = getPointForSphere(endRadius); // конечная позиция частицы
      endPositionData[i] = xyz[0] * aspect;
      endPositionData[i + 1] = xyz[1];
      endPositionData[i + 2] = xyz[2];
    }
    lifeTimeAsFloatBuffer = floatBuffer(lifeTimeData);
    startPositionAsFloatBuffer = floatBuffer(startPositionData);
    endPositionAsFloatBuffer = floatBuffer(endPositionData);
    createVertexBuffer();
  }

  public void draw(float delta) {
    if (lastTimeExplosion > 1.0) {
      // удалить данный взрыв из списка активных взрывов, чтобы условие
      // (lastTimeExplosion > 1.0) не проверялось при прорисовке каждого кадра
      explosions.remove(this);
      return;
    }
    //if (lastTimeExplosion > 1.0) return; // если время истекло, не производить рендер взрыва
    lastTimeExplosion += delta * 0.011f; // единица получается примерно через три секунды
    // отключить тест глубины - чтобы взрыв отображался правильно
    GLES30.glDisable(GLES30.GL_DEPTH_TEST);
    GLES30.glUseProgram(programObject);
    if (createExplosion) { // если взрыв только-что создан
      createExplosion = false;
      GLES30.glUniform3f(centerPositionLink, x, y, 0.0f); // установить центр взрыва
      GLES30.glUniform4f(colorLink, color[0], color[1], color[2], color[3]); // оранжевый
      GLES30.glUniform1f(sizeSpriteLink, sizeSprite);
    }
    GLES30.glUniform1f(lastTimeExplosionLink, lastTimeExplosion);

    // передать в вершинный шейдер атрибуты из вершинных буферов
    GLES30.glEnableVertexAttribArray(0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
    GLES30.glVertexAttribPointer(0, 1, GLES30.GL_FLOAT, false, 4, 0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

    GLES30.glEnableVertexAttribArray(1);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[1]);
    GLES30.glVertexAttribPointer(1, NUM_COORDINATES, GLES30.GL_FLOAT,
            false, FLOAT_SIZE * NUM_COORDINATES, 0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

    GLES30.glEnableVertexAttribArray(2);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[2]);
    GLES30.glVertexAttribPointer(2, NUM_COORDINATES, GLES30.GL_FLOAT,
            false, FLOAT_SIZE * NUM_COORDINATES, 0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

    // Разрешить приложению альфа-блендинг с использованием следующей
    // функции для смешивания цветов. В результате этого кода значение альфа,
    // полученное во фрагментном шейдере, умножается на цвет фрагмента.
    // Это значение затем добавляется к тому значению, которое уже есть в
    // соответствующем месте во фреймбуфере. Результатом является
    // аддитивное смешивание цветов для системы частиц.
    GLES30.glEnable(GLES30.GL_BLEND);
    GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE);
    // Bind the texture
    // сделать текущей текстуру с дымом
    GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
    GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID);
    // Set the sampler texture unit to 0
    GLES30.glUniform1i(samplerLink, 0);
    // рендеринг частиц. В отличии от треугольников, у точечных спрайтов
    // нет ни какой связанности, поэтому использование glDrawElements не
    // дало бы никакого выигрыша в этом примере.
    GLES30.glDrawArrays(GLES30.GL_POINTS, 0, numberParticles);
    // отключить прозрачность, чтобы все объекты сцены не были прозрачными
    GLES30.glDisable(GLES30.GL_BLEND);

    GLES30.glDisableVertexAttribArray(0); // отключить атрибут времени жизни
    GLES30.glDisableVertexAttribArray(1); // отключить атрибут начальных координат
    GLES30.glDisableVertexAttribArray(2); // отключить атрибут конечных координат

    // включить тест глубины - чтобы после взрыва ракеты не отображались
    // позади астероидов
    GLES30.glEnable(GLES30.GL_DEPTH_TEST);
  }

  /** создает новый взрыв */
  public void create(float x, float y) {
    lastTimeExplosion = 0.0f;
    createExplosion = true;
    this.x = x;
    this.y = y;
  }

  // возвращает случайные координаты для точки, расположенной внутри сферы
  private float[] getPointForSphere(float size) {
    double d, x, y, z;
    do {
      x = Math.random() * 2.0 - 1.0;
      y = Math.random() * 2.0 - 1.0;
      z = Math.random() * 2.0 - 1.0;
      d = x * x + y * y + z * z;
    } while (d > 1.0);
    return new float[] { (float) x * size, (float) y * size, (float) z * size };
  }

  /* создать буферы для вершинных атрибутов */
  private void createVertexBuffer() {
    VBO[0] = 0;
    VBO[1] = 0;
    VBO[2] = 0;
    GLES30.glGenBuffers(3, VBO, 0);

    lifeTimeAsFloatBuffer.position(0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
            FLOAT_SIZE * numberParticles, lifeTimeAsFloatBuffer,
            GLES30.GL_STATIC_DRAW);

    startPositionAsFloatBuffer.position(0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[1]);
    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
            FLOAT_SIZE * NUM_COORDINATES * numberParticles,
            startPositionAsFloatBuffer, GLES30.GL_STATIC_DRAW);

    endPositionAsFloatBuffer.position(0);
    GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[2]);
    GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
            FLOAT_SIZE * NUM_COORDINATES * numberParticles,
            endPositionAsFloatBuffer, GLES30.GL_STATIC_DRAW);
  }

  public void setExplosions(List<Explosion> explosions) {
    this.explosions = explosions;
  }
}

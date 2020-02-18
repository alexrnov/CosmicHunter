package alexrnov.cosmichunter.gles.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import alexrnov.cosmichunter.Object3D;
import alexrnov.cosmichunter.R;
import alexrnov.cosmichunter.gles.LinkedProgram;
import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.View3D;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.gles.Textures.loadTextureFromRaw;

public class MetalAsteroid extends Object3D implements Asteroid {
  private final int programObject;
  // ссылка на переменную вершинного шейдера, содержащую итоговую MVP-матрицу
  private final int mvpMatrixLink;
  // ссылка на переменную вершинного шейдера, содержащую модельно-видовую матрицу
  private final int mvMatrixLink;
  // ссылка на переменную вершинного шейдера, которая является семплером
  private final int samplerLink;
  // ссылка на переменную вершинного шейдера, содержащую вектор цвета
  // внешнего освещения
  private final int ambientLightColorLink;
  // ссылка на переменную вершинного шейдера, содержащую значение
  // интенсивности внешнего освещения
  private final int ambientLightIntensityLink;
  // ссылка на переменную вершинного шейдера, содержащую вектор цвета диффузного освещения
  private final int diffuseLightColorLink;
  // ссылка на переменную вершинного шейдера, содержащую значение интенсивности диффузного освещения
  private final int diffuseLightIntensityLink;
  // ссылка на переменную вершинного шейдера, содержащую вектор цвета бликового освещения
  private final int specularLightColorLink;
  // ссылка на переменную вершинного шейдера, содержащуб значение интенсивности бликового освещения
  private final int specularLightIntensityLink;
  // обработчик текстуры
  private final int textureID;


  private final int positionLink; // индекс переменной атрибута для вершин
  private final int textureCoordinatesLink; // индекс переменной атрибута для текстурных координат
  private final int normalLink; // индекс переменной атрибута для нормали

  private AsteroidView3D view;
  private final int[] VBO = new int[4];

  private Explosion bigExplosion;
  private Explosion middleExplosion;
  private Explosion smallExplosion;

  public MetalAsteroid(double versionGL, Context context, float scale) { //, TypeAsteroid type) {
    super(context, scale, "objects/asteroid1.obj");

    //загрузка шейдеров из каталога raw
    //LinkedProgram linkedProgramGL = new LinkedProgram(context,
    //R.raw.vertex_shader, R.raw.fragment_shader);

    LinkedProgram linkProgram = null;
    if (versionGL == 2.0) {
      linkProgram = new LinkedProgram(context,
              "shaders/gles20/metal_asteroid_v.glsl",
              "shaders/gles20/metal_asteroid_f.glsl");
    } else if (versionGL == 3.0) {
      linkProgram = new LinkedProgram(context,
              "shaders/gles30/metal_asteroid_v.glsl",
              "shaders/gles30/metal_asteroid_f.glsl");
    }


    final String className = this.getClass().getSimpleName() + ".class: ";
    programObject = linkProgram.get();
    if (programObject == 0) {
      Log.v(TAG, className + "error program link asteroid: " + programObject);
    }

    //связать a_position с атрибутом 0 в шейдере
    //(необязательно, т.к. a_position связывается со значением через location)
    //в glVertexAttribPointer(0)
    //GLES30.glBindAttribLocation(this.programObject, 0, "a_position");

    //Получить ссылку на переменную, содержащую итоговую MPV-матрицу.
    //Эта переменная находится в вершинном шейдере: uniform mat4 u_mvpMatrix;
    mvpMatrixLink = GLES20.glGetUniformLocation(programObject, "u_mvpMatrix");
    // получить индексы для индентификации uniform-переменных в программе
    mvMatrixLink = GLES20.glGetUniformLocation(programObject, "u_mvMatrix");
    //получить местоположение семплера
    samplerLink = GLES20.glGetUniformLocation(programObject, "s_texture");
    textureID = loadTextureFromRaw(context, R.raw.metal_texture); //загрузить текстуру
    ambientLightColorLink = GLES20.glGetUniformLocation(programObject, "u_ambientLight.color");
    ambientLightIntensityLink = GLES20.glGetUniformLocation(programObject, "u_ambientLight.intensity");
    diffuseLightColorLink = GLES20.glGetUniformLocation(programObject, "u_diffuseLight.color");
    diffuseLightIntensityLink = GLES20.glGetUniformLocation(programObject, "u_diffuseLight.intensity");
    specularLightColorLink = GLES20.glGetUniformLocation(programObject, "u_specularLight.color");
    specularLightIntensityLink = GLES20.glGetUniformLocation(programObject, "u_specularLight.intensity");

    // получить индексы атрибутов в вершинном шейдере
    positionLink = GLES20.glGetAttribLocation(programObject, "a_position");
    textureCoordinatesLink = GLES20.glGetAttribLocation(programObject, "a_textureCoordinates");
    normalLink = GLES20.glGetAttribLocation(programObject, "a_normal");

    Log.v(TAG, className +
            ": u_mvpMatrix id: " + mvpMatrixLink + "; u_mvMatrix id: " +
            mvMatrixLink + "; s_texture id: " + samplerLink +
            "; u_ambientLight.color id: " + ambientLightColorLink +
            "; u_diffuseLight.color id: " + diffuseLightColorLink +
            "; u_diffuseLight.intensity id: " + diffuseLightIntensityLink +
            "; u_specularLight.color id: " + specularLightColorLink +
            "; u_specularLight.intensity id: " + specularLightColorLink +
            "; textureID: " + textureID);
    createVertexBuffers();
  }

  private void createVertexBuffers() {
    VBO[0] = 0;
    VBO[1] = 0;
    VBO[2] = 0;
    VBO[3] = 0;

    GLES20.glGenBuffers(4, VBO, 0);
    bufferVertices.position(0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, VERTEX_STRIDE * NUMBER_VERTICES,
            bufferVertices, GLES20.GL_STATIC_DRAW);

    bufferTextureCoordinates.position(0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[1]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, TEXTURE_STRIDE * NUMBERS_TEXTURES,
            bufferTextureCoordinates, GLES20.GL_STATIC_DRAW);

    bufferNormals.position(0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[2]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, VERTEX_STRIDE * NUMBER_NORMALS,
            bufferNormals, GLES20.GL_STATIC_DRAW);

    bufferIndices.position(0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, VBO[3]);
    GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, INT_SIZE * NUMBER_INDICES,
            bufferIndices, GLES20.GL_STATIC_DRAW);
  }

  @Override
  public void setView(View3D view) {
    if (view instanceof AsteroidView3D) this.view = (AsteroidView3D) view;
  }

  @Override
  public AsteroidView3D getView() {
    return view;
  }

  @Override
  public void draw() {
    GLES20.glUseProgram(programObject);
    // включение вершинного массива для атрибута(in vec4 a_position). Если
    // для заданного индекса атрибута вершинный массив выключен, то для
    // этого атрибута будет использоваться соответствующее постоянное значение
    GLES20.glEnableVertexAttribArray(positionLink); // разрешить атрибут вершин куба
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
    // Метод glVertexAttribPointer загружет вершинные массивы. Size - число
    // компонент в вершинном массиве для заданного атрибута. Допустимые
    // значения 1 - 4. Stride - смещение в байтах между вершиной I и вершиной
    // (I + 1). Если stride равен нулю, то данные для всех вершин хранятся
    // последовательно. Если stride больше нуля, то мы используем его для
    // получения данных для следующей вершины. Для лучшего быстродействия
    // предпочтительно использовать GLES30.GL_HALF_FLOAT (не работает)
    // Загрузить данные вершин (location = 0)
    GLES20.glVertexAttribPointer(positionLink, VERTEX_COMPONENT, GLES20.GL_FLOAT,
            false, VERTEX_STRIDE, 0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    //включение массива текстурных координат для атрибута(in vec4 a_position)
    GLES20.glEnableVertexAttribArray(textureCoordinatesLink);//разрешить атрибут координат текстуры
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[1]);
    //загрузить текстурные координаты (location = 1)
    GLES20.glVertexAttribPointer(textureCoordinatesLink, TEXTURE_COMPONENT, GLES20.GL_FLOAT,
            false, TEXTURE_STRIDE, 0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    GLES20.glEnableVertexAttribArray(normalLink);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[2]);
    // индекс переменной атрибута можно получить следущим образом
    // int a_normal_Handle = GLES30.glGetAttribLocation(programObject, "a_Normal");
    // но мы просто указываем индекс 2, поскольку в шейдере он обазначен
    // с помощью ключевого слова location
    GLES20.glVertexAttribPointer(normalLink, NORMAL_COMPONENT, GLES20.GL_FLOAT,
            false, NORMAL_STRIDE, 0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    // передать в шейдер трехкомпонентный вектор цвета(белый) для
    // окружающего света
    GLES20.glUniform3f(ambientLightColorLink, 1.0f, 1.0f, 1.0f);
    // передать в шейдер интенсивность окружающего света
    GLES20.glUniform1f(ambientLightIntensityLink, 0.2f);
    GLES20.glUniform3f(diffuseLightColorLink, 1.0f, 1.0f, 1.0f);
    GLES20.glUniform1f(diffuseLightIntensityLink, 1.0f);
    GLES20.glUniform3f(specularLightColorLink, 1.0f, 1.0f, 1.0f);
    GLES20.glUniform1f(specularLightIntensityLink, 0.015f);

    // привязка к текстурному блоку. Функция задает текущий текстурный
    // блок, так что все дальнейшие вызовы glBindTexture привяжут
    // текстуру к активному текстурному блоку. Номер текстурного блока,
    // к которому будет привязана текстура, записывается в переменную
    // семплера (s_texture) параметр задает текстурный блок, который
    // станет активным, принимает значения GL_TEXTURE0,
    // GL_TEXTURE1,..., GL_TEXTURE31.
    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    //привязать текстуру к активному текстурному блоку
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
    // установить текстурную единицу семплера в 0, что означает, что
    // будет использоваться текстурный блок GL_TEXTURE0, к которой
    // привязана текстура textureId
    GLES20.glUniform1i(samplerLink, 0);
    // MV-матрица загружается в соответствующую uniform-переменную
    GLES20.glUniformMatrix4fv(mvMatrixLink, 1, false,
            view.getMVMatrixAsFloatBuffer());

    // итоговая MVP-матрица загружается в соответствующую uniform-переменную
    GLES20.glUniformMatrix4fv(mvpMatrixLink, 1, false,
            view.getMVPMatrixAsFloatBuffer());
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, VBO[3]);
    // выполнить рендеринг. Первый параметр - тип выводимых примитивов.
    // второй параметр - количество индексов, которое необходимо вывести.
    // третий параметр - тип индексов (другие варианты UNSIGNED_SHORT и UNSIGNED_BYTE)
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, NUMBER_INDICES, GLES20.GL_UNSIGNED_INT, 0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    // GLES30.glDisable(GLES30.GL_TEXTURE_2D);
    GLES20.glDisableVertexAttribArray(positionLink); // отключить атрибут вершин куба
    GLES20.glDisableVertexAttribArray(textureCoordinatesLink); // отключить атрибут координат текстуры
    GLES20.glDisableVertexAttribArray(normalLink); // отключить атрибут нормалей
    //GLES30.glFinish();
  }

  @Override
  public void setBigExplosion(Explosion explosion) {
    bigExplosion = explosion;
  }

  @Override
  public Explosion getBigExplosion() {
    return bigExplosion;
  }

  @Override
  public void setMiddleExplosion(Explosion explosion) {
    middleExplosion = explosion;
  }

  @Override
  public Explosion getMiddleExplosion() {
    return middleExplosion;
  }

  @Override
  public void setSmallExplosion(Explosion explosion) {
    smallExplosion = explosion;
  }

  @Override
  public Explosion getSmallExplosion() {
    return smallExplosion;
  }
}

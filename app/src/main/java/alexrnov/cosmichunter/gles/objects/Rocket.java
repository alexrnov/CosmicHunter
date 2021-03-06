package alexrnov.cosmichunter.gles.objects;

import android.content.Context;
import android.opengl.GLES20;
//import android.util.Log;

import alexrnov.cosmichunter.R;
import alexrnov.cosmichunter.gles.LinkedProgram;
import alexrnov.cosmichunter.view.RocketView3D;
import alexrnov.cosmichunter.view.View3D;

//import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.Initialization.sp;

public class Rocket extends Object3D {
  private final int programObject;
  // ссылка на переменную вершинного шейдера, содержащую итоговую MVP-матрицу
  private final int mvpMatrixLink;
  // ссылка на переменную вершинного шейдера, содержащую модельно-видовую матрицу
  private final int mvMatrixLink;
  // ссылка на переменную вершинного шейдера, содержащую вектор цвета
  // внешнего освещения
  private final int ambientLightColorLink;
  // ссылка на переменную вершинного шейдера, содержащую значение
  // интенсивности внешнего освещения
  private final int ambientLightIntensityLink;
  // ссылка на переменную вершинного шейдера, содержащую вектор цвета
  // диффузного освещения
  private final int diffuseLightColorLink;
  // ссылка на переменную вершинного шейдера, содержащую значение
  // интенсивности диффузного освещения
  private final int diffuseLightIntensityLink;
  private final int positionLink; // индекс переменной атрибута для вершин
  private final int normalLink; // индекс переменной атрибута для нормали

  private RocketView3D view;
  private final int[] VBO = new int[3];

  public Rocket(double versionGL, Context context, float scale) {
    super(context, scale, R.raw.simple_rocket);
    LinkedProgram linkProgram = null;

    boolean fog = sp.getBoolean("fog", true);
    if (versionGL == 2.0) {
      //Log.i(TAG, "version = 2");
      if (fog) {
        linkProgram = new LinkedProgram(context,
                "shaders/gles20/rocket_fog_v.glsl",
                "shaders/gles20/rocket_fog_f.glsl");
      } else {
        linkProgram = new LinkedProgram(context,
                "shaders/gles20/rocket_v.glsl",
                "shaders/gles20/rocket_f.glsl");
      }
    } else if (versionGL == 3.0) {
      //Log.i(TAG, "version = 3");
      if (fog) {
        linkProgram = new LinkedProgram(context,
                "shaders/gles30/rocket_fog_v.glsl",
                "shaders/gles30/rocket_fog_f.glsl");
      } else {
        linkProgram = new LinkedProgram(context,
                "shaders/gles30/rocket_v.glsl",
                "shaders/gles30/rocket_f.glsl");
      }

    }

    //final String className = this.getClass().getSimpleName();
    programObject = linkProgram.get();
    //if (programObject == 0) {
      //Log.e(TAG, className + "error program link rocket: " + programObject);
    //}

    //Получить ссылку на переменную, содержащую итоговую MPV-матрицу.
    //Эта переменная находится в вершинном шейдере: uniform mat4 u_mvpMatrix;
    mvpMatrixLink = GLES20.glGetUniformLocation(this.programObject, "u_mvpMatrix");
    // получить индексы для индентификации uniform-переменных в программе
    mvMatrixLink = GLES20.glGetUniformLocation(this.programObject, "u_mvMatrix");
    ambientLightColorLink = GLES20.glGetUniformLocation(this.programObject,
            "u_ambientLight.color");
    ambientLightIntensityLink = GLES20.glGetUniformLocation(this.programObject,
            "u_ambientLight.intensity");
    diffuseLightColorLink = GLES20.glGetUniformLocation(this.programObject,
            "u_diffuseLight.color");
    diffuseLightIntensityLink = GLES20.glGetUniformLocation(this.programObject,
            "u_diffuseLight.intensity");

    // получить индексы атрибутов в вершинном шейдере
    positionLink = GLES20.glGetAttribLocation(programObject, "a_position");
    normalLink = GLES20.glGetAttribLocation(programObject, "a_normal");

    /*
    Log.v(TAG, this.getClass().getSimpleName() + ".class: u_mvpMatrix id: " +
            mvpMatrixLink + "; u_mvMatrix id: " + mvMatrixLink + "; u_ambientLight.color id: " + ambientLightColorLink +
            "; u_diffuseLight.color id: " + diffuseLightColorLink +
            "; u_diffuseLight.intensity id: " + diffuseLightIntensityLink);
    */
    createVertexBuffers();
  }

  private void createVertexBuffers() {
    VBO[0] = 0;
    VBO[1] = 0;
    VBO[2] = 0;

    GLES20.glGenBuffers(3, VBO, 0);
    bufferVertices.position(0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[0]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, VERTEX_STRIDE * NUMBER_VERTICES,
            bufferVertices, GLES20.GL_STATIC_DRAW);

    bufferNormals.position(0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[1]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, VERTEX_STRIDE * NUMBER_NORMALS,
            bufferNormals, GLES20.GL_STATIC_DRAW);

    bufferIndices.position(0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, VBO[2]);
    GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, INT_SIZE * NUMBER_INDICES,
            bufferIndices, GLES20.GL_STATIC_DRAW);
  }

  @Override
  public void setView(View3D view) {
    if (view instanceof RocketView3D) this.view = (RocketView3D) view;
  }

  @Override
  public RocketView3D getView() {
    return view;
  }

  @Override
  public void draw() {
    GLES20.glUseProgram(programObject);
    // включение вершинного массива для атрибута a_position. Если
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
    // Загрузить данные вершин
    GLES20.glVertexAttribPointer(positionLink, VERTEX_COMPONENT, GLES20.GL_FLOAT,
            false, VERTEX_STRIDE, 0);
    /*
     * В GLES30 и GLSL 3.0 можно использовать Location в шейдере для
     * упрощенной ссылки на объект. Загрузить данные вершин (location = 0)
     * GLES30.glVertexAttribPointer(0, VERTEX_COMPONENT, GLES30.GL_FLOAT, false, VERTEX_STRIDE, 0);
    */
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    GLES20.glEnableVertexAttribArray(normalLink);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[1]);
    GLES20.glVertexAttribPointer(normalLink, NORMAL_COMPONENT, GLES20.GL_FLOAT,
            false, NORMAL_STRIDE, 0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    // передать в шейдер трехкомпонентный вектор цвета(белый) для окружающего света
    GLES20.glUniform3f(ambientLightColorLink, 1.0f, 1.0f, 1.0f);
    // передать в шейдер интенсивность окружающего света
    GLES20.glUniform1f(ambientLightIntensityLink, 0.4f);
    GLES20.glUniform3f(diffuseLightColorLink, 1.0f, 1.0f, 1.0f);
    //GLES30.glUniform1f(diffuseLightIntensityLink, 500.0f);
    GLES20.glUniform1f(diffuseLightIntensityLink, 0.6f);


    // MV-матрица загружается в соответствующую uniform-переменную
    GLES20.glUniformMatrix4fv(mvMatrixLink, 1, false, view.getMVMatrixAsFloatBuffer());
    // итоговая MVP-матрица загружается в соответствующую uniform-переменную
    GLES20.glUniformMatrix4fv(mvpMatrixLink, 1, false,
            view.getMVPMatrixAsFloatBuffer());

    // включить прозрачность
    GLES20.glEnable(GLES20.GL_BLEND);
    //GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);


    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, VBO[2]);
    // выполнить рендеринг. Первый параметр - тип выводимых примитивов.
    // второй параметр - количество индексов, которое необходимо вывести.
    // третий параметр - тип индексов (другие варианты UNSIGNED_SHORT и UNSIGNED_BYTE)
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, NUMBER_INDICES, GLES20.GL_UNSIGNED_INT, 0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

    // отключить прозрачность, чтобы все объекты сцены не были прозрачными
    GLES20.glDisable(GLES20.GL_BLEND);

    GLES20.glDisableVertexAttribArray(positionLink); // отключить атрибут вершин
    GLES20.glDisableVertexAttribArray(normalLink); // отключить атрибут нормалей

    //GLES30.glFinish();
  }
}

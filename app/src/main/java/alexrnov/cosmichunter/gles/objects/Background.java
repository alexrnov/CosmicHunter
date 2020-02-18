package alexrnov.cosmichunter.gles.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import alexrnov.cosmichunter.Object3D;
import alexrnov.cosmichunter.R;
import alexrnov.cosmichunter.gles.LinkedProgram;
import alexrnov.cosmichunter.gles.Textures;
import alexrnov.cosmichunter.view.BackgroundView3D;
import alexrnov.cosmichunter.view.View3D;

import static alexrnov.cosmichunter.Initialization.TAG;

public class Background extends Object3D {
  private final int programObject;
  private BackgroundView3D view;

  // ссылка на переменную вершинного шейдера, содержащую итоговую MVP-матрицу
  private final int mvpMatrixLink;
  private final int samplerLink;// ссылка на переменную-семплер вершинного шейдера
  private final int textureID; // обработчик текстуры

  private final int[] VBO = new int[3];

  private final int positionLink; // индекс переменной атрибута для вершин
  private final int textureCoordinatesLink; // индекс переменной атрибута для текстурных координат

  public Background(double versionGL, Context context, float scale, int textureIDResource) {
    super(context, scale, R.raw.fone);

    LinkedProgram linkProgram = null;
    if (versionGL == 2.0) {
      linkProgram = new LinkedProgram(context,
              "shaders/gles20/background_v.glsl",
              "shaders/gles20/background_f.glsl");
    } else if (versionGL == 3.0) {
      linkProgram = new LinkedProgram(context,
              "shaders/gles30/background_v.glsl",
              "shaders/gles30/background_f.glsl");
    }


    programObject = linkProgram.get();
    final String className = this.getClass().getSimpleName() + ".class: ";
    if (programObject == 0) {
      Log.e(TAG, className + "error program link background: " + programObject);
    }

    //Получить ссылку на переменную, содержащую итоговую MPV-матрицу.
    //Эта переменная находится в вершинном шейдере: uniform mat4 u_mvpMatrix;
    mvpMatrixLink = GLES20.glGetUniformLocation(programObject, "u_mvpMatrix");
    //получить местоположение семплера
    samplerLink = GLES20.glGetUniformLocation(programObject, "s_texture");
    textureID = Textures.loadTextureFromRaw(context, textureIDResource); //загрузить текстуру

    // получить индексы атрибутов в вершинном шейдере
    positionLink = GLES20.glGetAttribLocation(programObject, "a_position");
    textureCoordinatesLink = GLES20.glGetAttribLocation(programObject, "a_textureCoordinates");

    Log.v(TAG, this.getClass().getSimpleName() + ".class: u_mvpMatrix id: " +
            mvpMatrixLink + "; s_texture id: " + samplerLink + "; textureID: " + textureID);
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

    bufferTextureCoordinates.position(0);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBO[1]);
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, TEXTURE_STRIDE * NUMBERS_TEXTURES,
            bufferTextureCoordinates, GLES20.GL_STATIC_DRAW);

    bufferIndices.position(0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, VBO[2]);
    GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, INT_SIZE * NUMBER_INDICES,
            bufferIndices, GLES20.GL_STATIC_DRAW);
  }

  @Override
  public void setView(View3D view) {
    if (view instanceof BackgroundView3D) this.view = (BackgroundView3D) view;
    //достаточно вызвать один раз, поскольку у фона нет анимации
    this.view.spotPosition(0.0f);
  }

  @Override
  public View3D getView() {
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

    // итоговая MVP-матрица загружается в соответствующую uniform-переменную
    GLES20.glUniformMatrix4fv(mvpMatrixLink, 1, false,
            view.getMVPMatrixAsFloatBuffer());

    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, VBO[2]);
    // выполнить рендеринг. Первый параметр - тип выводимых примитивов.
    // второй параметр - количество индексов, которое необходимо вывести.
    // третий параметр - тип индексов (другие варианты UNSIGNED_SHORT и UNSIGNED_BYTE)
    GLES20.glDrawElements(GLES20.GL_TRIANGLES, NUMBER_INDICES, GLES20.GL_UNSIGNED_INT, 0);
    GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    // GLES30.glDisable(GLES30.GL_TEXTURE_2D);
    GLES20.glDisableVertexAttribArray(positionLink); // отключить атрибут вершин куба
    GLES20.glDisableVertexAttribArray(textureCoordinatesLink); // отключить атрибут координат текстуры

    //GLES30.glFinish();
  }
}

package alexrnov.cosmichunter.objects;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import alexrnov.cosmichunter.utils.loader.ModelData;
import alexrnov.cosmichunter.utils.loader.OBJFileLoader;
import alexrnov.cosmichunter.view.View3D;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.utils.commonGL.Buffers.floatBuffer;
import static alexrnov.cosmichunter.utils.commonGL.Buffers.intBuffer;

public abstract class Object3D {

  final byte INT_SIZE = 4; //количество байт на тип integer
  private final byte FLOAT_SIZE = 4; // количество байт на тип float
  //количество элементов массива на вершину (x, y, z)
  final byte VERTEX_COMPONENT = 3;
  // количество элементов массива на текстурную координату (u,v)
  final byte TEXTURE_COMPONENT = 2;
  //количество элементов массива на нормаль (x, y, z)
  final byte NORMAL_COMPONENT = 3;
  // шаг для вершины: количество байт на тип float умножить на количество
  // элементов массива для вершины (x, y, z)
  final byte VERTEX_STRIDE = FLOAT_SIZE * VERTEX_COMPONENT;
  // шаг для текстурной координаты:  количество байт на тип float умножить
  // на количество элементов массива для текстурной координаты(u, v)
  final byte TEXTURE_STRIDE = FLOAT_SIZE * TEXTURE_COMPONENT;
  // шаг для нормали: количество байт на тип float умножить на количество
  // элементов массива для нормали (x, y, z)
  final byte NORMAL_STRIDE = FLOAT_SIZE * NORMAL_COMPONENT;
  final int NUMBER_INDICES; // количество индексов
  final int NUMBER_VERTICES; // количество вершин
  final int NUMBER_NORMALS; // количество нормалей
  final int NUMBERS_TEXTURES; // количество текстурных координат

  FloatBuffer bufferVertices;
  FloatBuffer bufferNormals;
  FloatBuffer bufferTextureCoordinates;
  IntBuffer bufferIndices;
  private ModelData modelData;
  private final String className = this.getClass().getSimpleName();

  Object3D(Context context, float scale, String assetFileName) {
    try {
      InputStream input = context.getAssets().open(assetFileName);
      OBJFileLoader loader = new OBJFileLoader();
      modelData = loader.loadOBJ(input);
    } catch (IOException e) {
      Log.v(TAG, className + "error load object file for asteroid");
    }

    // не выведено в отдельный метод - потому как финальные переменные
    // должны быть проинициализированы в конструкторе
    float[] verticesTemplate = modelData.getVertices();
    NUMBER_VERTICES = modelData.getVertices().length / VERTEX_COMPONENT;
    for (int i = 0; i < verticesTemplate.length; i++) {
      verticesTemplate[i] = verticesTemplate[i] * scale;
    }
    bufferVertices = floatBuffer(verticesTemplate);

    int[] cubeIndices = modelData.getIndices();
    NUMBER_INDICES = cubeIndices.length;
    bufferIndices = intBuffer(cubeIndices);

    float[] normals = modelData.getNormals();
    NUMBER_NORMALS = normals.length / VERTEX_COMPONENT;
    bufferNormals = floatBuffer(normals);

    float[] textureCoordinates = modelData.getTextureCoordinates();
    NUMBERS_TEXTURES = textureCoordinates.length / TEXTURE_COMPONENT;
    bufferTextureCoordinates = floatBuffer(textureCoordinates);
  }

  Object3D(Context context, float scale, int resourceID) {
    InputStream input = context.getResources().openRawResource(resourceID);
    OBJFileLoader loader = new OBJFileLoader();
    modelData = loader.loadOBJ(input);

    // не выведено в отдельный метод - потому как финальные переменные
    // должны быть проинициализированы в конструкторе
    float[] verticesTemplate = modelData.getVertices();
    NUMBER_VERTICES = modelData.getVertices().length / VERTEX_COMPONENT;
    for (int i = 0; i < verticesTemplate.length; i++) {
      verticesTemplate[i] = verticesTemplate[i] * scale;
    }
    bufferVertices = floatBuffer(verticesTemplate);

    int[] cubeIndices = modelData.getIndices();
    NUMBER_INDICES = cubeIndices.length;
    bufferIndices = intBuffer(cubeIndices);

    float[] normals = modelData.getNormals();
    NUMBER_NORMALS = normals.length / VERTEX_COMPONENT;
    bufferNormals = floatBuffer(normals);

    float[] textureCoordinates = modelData.getTextureCoordinates();
    NUMBERS_TEXTURES = textureCoordinates.length / TEXTURE_COMPONENT;
    bufferTextureCoordinates = floatBuffer(textureCoordinates);
  }

  /**
   * Сеттер для объекта, который содержит матрицы для отображения
   * трехмерного объекта в пространстве. Должен быть вызван перед
   * методом draw().
   */
  public abstract void setView(View3D view);

  /**
   * Геттер для объекта, который содержит матрицы для отображения
   * трехмерного объекта в пространстве
   */
  public abstract View3D getView();

  /** Метод содержит инструкции для прорисовки объекта */
  public abstract void draw();
}

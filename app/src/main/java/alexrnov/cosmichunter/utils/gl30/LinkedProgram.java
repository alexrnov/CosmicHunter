package alexrnov.cosmichunter.utils.gl30;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import static alexrnov.cosmichunter.Initialization.TAG;


/**
 * Класс линкует(собирает) объект-программу OpenGL. В качестве входных
 * параметров конструкторов используется контекст приложения и
 * местонахождение вершинного и фрагментного шейдеров. Местонахождение
 * шейдеров может задаваться либо через идентификатор ресурса, либо через
 * файловый путь. Перед линковкой программы шейдеры компилируются. Если
 * сборка программы потерпела неудачу, тогда метод get() возвращает
 * значение 0.
 */
public class LinkedProgram {
  private final String className = this.getClass().getSimpleName() + ".class: ";
  private Context context;
  private int programObject;

  /**
   * @param context контекст приложения
   * @param idVertexShader идентификатор ресурса для вершинного шейдера
   * @param idFragmentShader идентификатор ресурса для фрагментного шейдера
   */
  public LinkedProgram(final Context context, int idVertexShader,
                       int idFragmentShader) {
    this.context = context;
    String codeVertexShader = readShaderFileFromRawResource(idVertexShader);
    String codeFragmentShader = readShaderFileFromRawResource(idFragmentShader);
    programObject = linkProgramAndGetId(codeVertexShader, codeFragmentShader);
  }

  /**
   * @param context контекст приложения
   * @param filePathVertexShader путь к файлу вершинного шейдера
   * @param filePathFragmentShader путь к файлу фрагментного шейдера
   */
  public LinkedProgram(final Context context, String filePathVertexShader,
                       String filePathFragmentShader) {
    this.context = context;
    String codeVertexShader = readShaderFileFromFilePath(filePathVertexShader);
    String codeFragmentShader = readShaderFileFromFilePath(filePathFragmentShader);
    programObject = linkProgramAndGetId(codeVertexShader, codeFragmentShader);
  }

  /**
   * @param codeVertexShader исходный код вершинного шейдера
   * @param codeFragmentShader исходный код фрагментного шейдера
   */
  public LinkedProgram(String codeVertexShader, String codeFragmentShader) {
    programObject = linkProgramAndGetId(codeVertexShader, codeFragmentShader);
  }

  //Возвращает исходный код шейдера в виде строки. В качестве входного
  //параметра местоположения шейдера используется идентификатор ресурса
  private String readShaderFileFromRawResource(final int resourceId) {
    final InputStream inputStream = context.getResources()
                                              .openRawResource(resourceId);
    final BufferedReader bufferedReader = new BufferedReader(
                                          new InputStreamReader(inputStream));
    String nextLine;
    final StringBuilder code = new StringBuilder();
    try {
      while ((nextLine = bufferedReader.readLine()) != null) {
        code.append(nextLine);
        code.append("\n");
      }
    } catch(IOException e) {
      return null;
    }
    return code.toString();
  }

  //Возвращает исходный код шейдера в виде строки. В качестве входного
  //параметра местоположения шейдера используется путь к файлу.
  private String readShaderFileFromFilePath(final String filePath) {
    String code = "";
    try {
      InputStream in = context.getAssets().open(filePath);
      Scanner s = new Scanner(in).useDelimiter("\\A");//разделитель - начало ввода
      if (s.hasNext()) {
        code = s.next();
      }
      s.close();
    } catch(IOException e) {
      Log.e(TAG, className + "read shader-file from file path");
    }
    return code;
  }

  //собирает программу и возвращает ее идентификатор
  private int linkProgramAndGetId(String v, String f) {
    int vertexShader;
    int fragmentShader;
    int programId;
    //загрузить вершинный шейдер
    vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, v);
    if (vertexShader == 0) {
      GLES30.glDeleteShader(vertexShader);
      Log.e(TAG, className + "vertex shader is error");
      return 0;
    }
    //загрузить фрагментный шейдер
    fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, f);
    if (fragmentShader == 0) {
      GLES30.glDeleteShader(fragmentShader);
      Log.e(TAG, className + "fragment shader is error");
      return 0;
    }
    //необязательная инструкция, которую можно вызвать после компиляции
    //всех шейдеров,  для уменьшения затрат ресурсов, занимаемых компилятором
    GLES30.glReleaseShaderCompiler();
    //Создать объект-программу. Объект-программа может рассматриваться как
    //окончательно собранная программа. После того как различные шейдеры
    //были скомпилированы в объекты-шеейдеры, они должны быть присоединены
    //к объекту-программе и собраны перед рендерингом
    programId = GLES30.glCreateProgram();
    if (programId == 0) {
      Log.e(TAG, className + "error programObject: 0");
      return 0;
    }
    //присоединение шейдеров к программе. glDetachShader - отсоединяет шейдер
    GLES30.glAttachShader(programId, vertexShader);
    GLES30.glAttachShader(programId, fragmentShader);
    //линковщик проверит ряд условий, гарантирующих успешную сборку.
    //фаза сборки - тот самый момент, когда генерируются команды для GPU
    GLES30.glLinkProgram(programId);
    //проверка на ошибки: определить результат линкования программы
    int[] linked = new int[1];
    GLES30.glGetProgramiv(programId, GLES30.GL_LINK_STATUS, linked, 0);
    if (linked[0] == 0) {
      Log.e(TAG, className + "error linking program: " +
              GLES30.glGetProgramInfoLog(programId));
      GLES30.glDeleteProgram(programId);
      return 0;
    }
    /*
    int[] linked2 = new int[1];
    GLES30.glValidateProgram(programId);
    GLES30.glGetProgramiv(programId, GLES30.GL_VALIDATE_STATUS, linked2, 0);
    Log.v(TAG, className + "2: " + GLES30.glGetProgramInfoLog(programId));
    */
    int[] info = new int[1];
    //число активных атрибутов в вершинном шейдере
    GLES30.glGetProgramiv(programId, GLES30.GL_ACTIVE_ATTRIBUTES, info, 0);
    int activeAttributes = info[0];
    // число активных uniform-переменных. Uniform-переменная считается активной,
    // если она используется в коде
    GLES30.glGetProgramiv(programId, GLES30.GL_ACTIVE_UNIFORMS, info, 0);
    int activeUniforms = info[0];
    Log.v(TAG, className + "active attributes in vertex shader: " +
    activeAttributes + "; active uniform-variable in program: " + activeUniforms);
    //Освободить ресурсы, которые больше не нужные
    GLES30.glDeleteShader(vertexShader);
    GLES30.glDeleteShader(fragmentShader);
    return programId;
  }

  //Компилировать шейдер
  private int loadShader(int type, String shaderSrc) {
    int shader;

    //создать объект шейдера
    shader = GLES30.glCreateShader(type);
    if (shader == 0) {
      Log.e(TAG, className + "error shader: 0");
      return 0;
    }
    //загрузить исходный код шейдера
    GLES30.glShaderSource(shader, shaderSrc);
    //компилировать шейдер
    GLES30.glCompileShader(shader);
    //проверка на наличие ошибок: определить результат компиляции шейдера
    //shader, результат запроса будет записан в массив compiled
    int[] compiled = new int[1];
    GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);

    if (compiled[0] == 0) {
      Log.e(TAG, className + "error compiled shader: 0 ");
      // вывести в лог информацию об ошибке компиляции
      Log.e(TAG, className + GLES30.glGetShaderInfoLog(shader));
      GLES30.glDeleteShader(shader);
      return 0;
    }

    //проверить, поддерживает ли реализация OpenGL3.0 компиляцю
    //во время выполнения
    boolean[] runtime = new boolean[1];
    GLES30.glGetBooleanv(GLES30.GL_SHADER_COMPILER, runtime, 0);
    if (!runtime[0]) {
      Log.e(TAG, className
              + "OpenGL 3.0 ES not supported runtime compile shader");
      return 0;
    }
    return shader;
  }

  /**
   * Возвращает идентификатор объекта-программы OpenGL 3.0
   * @return идентификатор объекта-программы OpenGL 3.0
   */
  public int get() {
    return programObject;
  }
}

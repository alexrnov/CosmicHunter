package alexrnov.cosmichunter.utils.commonGL

import android.opengl.GLU
import java.util.*

/**
 * Выражает координаты центра объекта (или его вершины) в пикселах экрана.
 * В конструкторе указывается ширина и высота экрана OpenGL. После создания
 * объекта, при каждой перерисовке кадра необходимо вызывать метод
 * setMatrices() для получения модельно-видовой и проекционной матриц.
 * Затем вызвать метод fromCenterOfObjectOpenGL() или
 * fromVertexOfObjectOpenGL(xModel, yModel, zModel) для получения координат
 * в пикселах экрана
 */
class CoordinatesWindow(widthScreen: Int,
                        heightScreen: Int) {
  /** координата х объекта в пикселах экрана */
  val x: Float
    get() = coordinatesWindow[0]

  /** координата y объекта в пикселах экрана */
  val y: Float
    get() = coordinatesWindow[1]

  /** координата z объекта в пикселах экрана */
  val z: Float
    get() = coordinatesWindow[2]

  private var mvMatrix: FloatArray = FloatArray(16)
  private var projectionMatrix: FloatArray = FloatArray(16)

  //массив, содержащий координаты, выраженные в пикселах экрана
  private var coordinatesWindow: FloatArray = FloatArray(3)
  private var view:IntArray = intArrayOf(0, 0, widthScreen, heightScreen)

  /**
   * Установить модельно-видовую и проекционную матрицы
   */
  fun setMatrices(mvMatrix: FloatArray, projectionMatrix: FloatArray) {
    this.mvMatrix = Arrays.copyOf(mvMatrix, mvMatrix.size)
    this.projectionMatrix = Arrays.copyOf(projectionMatrix, projectionMatrix.size)
  }

  /**
   * Выразить координаты центра объекта в пикселах экрана
   */
  fun fromCenterOfObjectOpenGL() {
    GLU.gluProject(0.0f, 0.0f, 0.0f, mvMatrix, 0,
                  projectionMatrix, 0, view, 0,
                  coordinatesWindow, 0)
  }

  /**
   * Выразить координаты вершины объекта в пикселах экрана. Координаты
   * вершины объекта указываются в модельных координатах
   */
  fun fromVertexOfObjectOpenGL(modelX: Float, modelY: Float, modelZ: Float) {
    GLU.gluProject(modelX, modelY, modelZ, mvMatrix, 0,
            projectionMatrix, 0, view, 0,
            coordinatesWindow, 0)
  }
}
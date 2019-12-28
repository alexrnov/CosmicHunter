package alexrnov.cosmichunter.view

import android.opengl.Matrix
import alexrnov.cosmichunter.utils.commonGL.Buffers
import java.nio.FloatBuffer

/**
 * Определяет настройки, общие для всех объектов сцены, такие как:
 * настройка камеры(viewMatrix) и проекция с перспективой(projectionMatrix).
 * Модельная, модельно-видовая, и МВП-матрицы заполняются в
 * классах-наследниках.
 */
abstract class View3D(val widthScreen: Int, private val heightScreen: Int) {
  var x: Float = 0.0f
  var y: Float = 0.0f
  var z: Float = 0.0f

  protected val aspect = widthScreen.toFloat() / heightScreen.toFloat()

  protected val viewMatrix = FloatArray(16)
  protected val projectionMatrix = FloatArray(16)

  protected val modelMatrix = FloatArray(16)
  protected val modelViewMatrix = FloatArray(16)
  protected val mvpMatrix = FloatArray(16)

  init {
    // установить позицию камеры(матрица вида)
    // eyeX, eyeY, eyeZ - позиция камеры(отодвинута на три единицы
    // назад (к наблюдателю))
    // centerX, centerY, centerZ - координаты точки, куда смотрит камера
    // upX, upY, upZ - поворот камеры (обычно менять эти значения не нужно)
    Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 4f,
            0f, 0f, 0f, 0f, 1.0f, 0.0f)
    setPerspectiveProjection()
  }

  /*
  * Установить проекцию с перспективой.
  * https://startandroid.ru/ru/uroki/vse-uroki-spiskom/401-urok-172-perspective-frustum-ortho.html
  */
  private fun setPerspectiveProjection() {
    var left = -1.0f
    var right = 1.0f
    var bottom = -1.0f
    var top = 1.0f
    /*
     * Чтобы уменьшить перспективу, нужно увеличить это значение.
     * Что-бы перспективы не было вовсе, нужно установить значение near = 1.0f
     */
    val near = 1.0f
    // значение far установлено с запасом, потому-что на разных устройствах
    // фон звездного неба отображается при разном значении far
    val far = 120.0f
    if (widthScreen < heightScreen) {
      bottom /= aspect
      top /= aspect
    } else {
      left *= aspect
      right *= aspect
    }
    Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far)
  }

  /**
   * Метод вызывается при перересовке каждого кадра, и определяет
   * алгоритм поведения трехмерного объекта(перемещение, вращение,
   * изменение размера и т.п.) Паттерн СТРАТЕГИЯ
   */
  abstract fun spotPosition(delta: Float)

  /**
   * Возвращает итоговую модельно-видо-проекционную-матрицу,
   * характеризующую положение трехмерного объекта в пространстве
   */
  fun getMVPMatrixAsFloatBuffer(): FloatBuffer = Buffers.floatBuffer(mvpMatrix)

  /**
   * Возвращает модельно-видовую-матрицу (необходима для расчета
   * освещения в вершинном щейдере)
   */
  fun getMVMatrixAsFloatBuffer(): FloatBuffer = Buffers.floatBuffer(modelViewMatrix)
}
package alexrnov.cosmichunter.view

import android.opengl.Matrix
import kotlin.math.abs

/**
 * Определяет поведение ракеты, - такие ее характеристики как:
 * скорость и направление движения, вращение и реакцию нажатия на
 * экран. Объект ракеты в blender повернут на 270 градусов по оси x.
 */
class RocketView3D(widthScreen: Int, heightScreen: Int):
        View3D(widthScreen, heightScreen) {
  private var kx: Float = 0.23f
  private var ky: Float = 0.23f



  private var angleX: Float = 0f // угол поворота ракеты по оси X
  private var angleY: Float = 0f // угол поворота ракеты по оси Y
  private val k = 16 // коэффициент влияет на угол поворота ракеты
  var fly: Boolean = false // переменная определяет запущена ли ракета

  override fun spotPosition(delta: Float) {
    z -= delta * 0.3f
    x += delta * kx
    y += delta * ky
    Matrix.setIdentityM(modelMatrix, 0)
    // переместить куб вверх/вниз и влево/вправо
    Matrix.translateM(modelMatrix, 0, x, y, z)
    // угол и направления вращения
    Matrix.rotateM(modelMatrix, 0, angleX, 0.0f, 1.0f, 0.0f)
    Matrix.rotateM(modelMatrix, 0, angleY, 1.0f, 0.0f, 0.0f)
    // комбинировать видовую и модельные матрицы
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
    // комбинировать модельно-видовую матрицу и проектирующую матрицу
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)
    if (z < -95) { // если объект достаточно удалился - перестать отрисовывать его
      fly = false
    }
  }

  /**
   * Метод принимает координаты нажатия пальцем на экран.
   * Предпологается, что будут использоваться координаты в формате
   * OpenGL. [x] - координата, [y] - координата
   */
  fun setXYEvent(xPass: Float, yPass: Float) {
    fly = true // запустить ракету
    x = xPass * aspect * 4 // значение 4 подобрано эмпирически
    y = yPass * 4 // значение 4 подобрано эмпирически
    //z = 0f
    z = -4f
    kx = x / 10
    ky = y / 10
    angleX = if (x > 0.0f) 360 - abs(x * k) else abs(x * k)
    angleY = y * k
  }
}
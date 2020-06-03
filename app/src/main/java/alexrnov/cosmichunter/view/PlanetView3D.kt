package alexrnov.cosmichunter.view

import alexrnov.cosmichunter.gles.objects.Rocket
import android.opengl.GLU
import android.opengl.Matrix
import java.lang.Math.pow
import java.lang.Math.sqrt
import java.util.*

/**
 * Определяет поведение астероида, - такие его характеристики как:
 * скорость и направление движения, вращение
 */
class PlanetView3D(widthScreen: Int, heightScreen: Int):
        AsteroidView3D(widthScreen, heightScreen) {
  private var angle: Float = 0.0f
  override fun spotPosition(delta: Float) {
    Matrix.setIdentityM(modelMatrix, 0)
    angle += delta * 0.5f
    // переместить куб вверх/вниз и влево/вправо
    Matrix.translateM(modelMatrix, 0, 0.0f, 0.0f, -100.0f)
    // угол и направления вращения
    Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 0.5f, 0.0f)
    Matrix.scaleM(modelMatrix, 0, 15f, 15f, 15f) // увеличить объект
    // комбинировать видовую и модельные матрицы
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
    // комбинировать модельно-видовую матрицу и проектирующую матрицу
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)
  }
}
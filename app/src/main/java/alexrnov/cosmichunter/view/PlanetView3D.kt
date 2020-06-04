package alexrnov.cosmichunter.view

import alexrnov.cosmichunter.utils.commonGL.Buffers
import android.opengl.Matrix
import java.nio.FloatBuffer

/**
 * Определяет поведение астероида, - такие его характеристики как:
 * скорость и направление движения, вращение
 */
class PlanetView3D(widthScreen: Int, heightScreen: Int):
        AsteroidView3D(widthScreen, heightScreen) {

  private var angle: Float = 0.0f
  private val modelViewMatrixForShader = FloatArray(16)

  override fun spotPosition(delta: Float) {
    Matrix.setIdentityM(modelMatrix, 0)
    angle += delta * 0.24f
    // переместить куб вверх/вниз и влево/вправо
    Matrix.translateM(modelMatrix, 0, 70.0f, 0.0f, -100.0f)
    // угол и направления вращения
    Matrix.rotateM(modelMatrix, 0, angle, 0.0f, 0.5f, 0.0f)
    // отдельная mv-матрица для загрузки в шейдер
    Matrix.multiplyMM(modelViewMatrixForShader, 0, viewMatrix, 0, modelMatrix, 0)
    Matrix.scaleM(modelMatrix, 0, 17f, 17f, 17f) // увеличить объект
    // комбинировать видовую и модельные матрицы
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
    // комбинировать модельно-видовую матрицу и проектирующую матрицу
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)
  }

  /**
   * Для планеты используется отдельная mv-матрица, которая не
   * подвергалась трансформации с помощью метода Matrix.scaleM()
   */
  fun getMVNoScaleMatrixAsFloatBuffer(): FloatBuffer = Buffers.floatBuffer(modelViewMatrixForShader)

}
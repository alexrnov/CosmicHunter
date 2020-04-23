package alexrnov.cosmichunter.view

import android.opengl.Matrix

/** Определяет местоположение плоскости фона */
class BackgroundView3D(widthScreen: Int, heightScreen: Int):
        View3D(widthScreen, heightScreen) {

  init {
    z = -110f
  }
  /**
   * Метод вызывается при перересовке каждого кадра, и определяет
   * алгоритм поведения трехмерного объекта(перемещение, вращение,
   * изменение размера и т.п.) Паттерн СТРАТЕГИЯ
   */
  override fun spotPosition(delta: Float) { //Создать вращение и перемещение куба
    //сбросить матрицу на единичную
    Matrix.setIdentityM(modelMatrix, 0)
    // переместить куб вверх/вниз и влево/вправо
    Matrix.translateM(modelMatrix, 0, x, y, z)
    // комбинировать видовую и модельные матрицы
    Matrix.multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0)
    // комбинировать модельно-видовую матрицу и проектирующую матрицу
    Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0)
  }
}
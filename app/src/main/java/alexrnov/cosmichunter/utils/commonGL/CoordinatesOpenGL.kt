package alexrnov.cosmichunter.utils.commonGL

/**
 * Преобразование оконных координат устройства(в пикселах)
 * в координаты OpenGL, которые находятся в диапазоне от [-1;-1] до [1;1]
 */
class CoordinatesOpenGL {

  /** @return Преобразованная координата X (в системе OpenGL) */
  var xGL: Float = 0.0f
    private set
  /** @return Преобразованная координата Y (в системе OpenGL) */
  var yGL: Float = 0.0f
    private set

  /**
   * Получить координаты в формате OpenGL (от [-1;-1] до [1;1]) на основе
   * размеров
   * @param displayWidth - ширина экрана(в пискелах)
   * @param displayHeight - высота экрана(в пикселах)
   * @param displayX - координата X (в пикселах экрана) для преобразования
   * @param displayY - координата Y (в пикселах экрана) для преобразования
   * @return преобразованные X и Y в системе координат OpenGL
   */
  fun fromDisplay(displayWidth: Int, displayHeight: Int,
                        displayX: Float, displayY: Float) {
    val offsetX: Float //смещение по оси абцисс (в координатах устройства)
    val offsetY: Float //смещение по оси ординат (в координатах устройства)

    val centerX = displayWidth/2 //x центра экрана (в пикселах)
    val centerY = displayHeight/2 //y центра экрана (в пикселах)

    if (displayX <= centerX && displayY >= centerY) { //левая нижняя четверть
      offsetX = (centerX - displayX)
      offsetY = (displayY - centerY)
      xGL = -(offsetX/centerX)
      yGL = -(offsetY/centerY)
    } else if (displayX <= centerX && displayY <= centerY) { //левая верхняя четверть
      offsetX = (centerX - displayX)
      offsetY = (centerY - displayY)
      xGL = -(offsetX/centerX)
      yGL = offsetY/centerY
    } else if (displayX >= centerX && displayY <= centerY) { //правая верхняя четверть
      offsetX = (displayX - centerX)
      offsetY = (centerY - displayY)
      xGL = offsetX/centerX
      yGL = offsetY/centerY
    } else if (displayX > centerX && displayY > centerY) {//правая нижняя четверть
      offsetX = (displayX - centerX)
      offsetY = (displayY - centerY)
      xGL = offsetX/centerX
      yGL = -(offsetY/centerY)
    }
  }
}

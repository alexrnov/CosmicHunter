package alexrnov.cosmichunter.gles;

/**
 * Функциональный интерфейс который позволяет определять алгоритм
 * обработки нажатия на экран. Если его не использовать придется каждый
 * рад прибегать к условной проверке в методе setPassXY(float passX, float passY)
 * для классов Level 1-5
 * @param <T> координата x нажатия на экран
 * @param <U> координата y нажатия на экран
 */
@FunctionalInterface
public interface PressXYInterface<T, U> {
  /** Метод содержит обработчик нажатия на экран */
  void invoke(T x, U y);
}

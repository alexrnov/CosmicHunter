package alexrnov.cosmichunter.gles.objects;

import java.util.List;

public interface Explosion {

  /**
   * Метод генерирует данные для каждой частицы, такие как: время жизни,
   * начальные и конечные координаты. Эти данные затем будут
   * передаваться в вершинный шейдер как вершинные атрибуты. Метод
   * также создает вершинные буферы для атрибутов вершин
   * @param width - ширина экрана
   * @param height - высота экрана
   */
  void createDataVertex(int width, int height);

  void draw(float delta);

  void setExplosions(List<Explosion> explosions);

  /** создает новый взрыв */
  void create(float x, float y, float z);
}

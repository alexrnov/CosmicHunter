package alexrnov.cosmichunter.utils.loader;

/**
 * Класс данных модели. Хранит данные вершин, индексов, нормалей,
 * текстурных uv-координат. Первоисточник: OpenGL 3D game tutorial
 * https://www.dropbox.com/sh/x1fyet1otejxk3z/AAAoCqArl4cIx0THdRk2poW3a?dl=0
 */
public class ModelData {
  private float[] vertices;
  private float[] textureCoordinates;
  private float[] normals;
  private int[] indices;
  private float furthestPoint;

  ModelData(float[] vertices, float[] textureCoordinates,
                   float[] normals, int[] indices, float furthestPoint) {
    this.vertices = vertices;
    this.textureCoordinates = textureCoordinates;
    this.normals = normals;
    this.indices = indices;
    this.furthestPoint = furthestPoint;
  }

  public float[] getVertices() {
    return vertices;
  }

  public float[] getTextureCoordinates() {
    return textureCoordinates;
  }

  public float[] getNormals() {
    return normals;
  }

  public int[] getIndices() {
    return indices;
  }

  public float getFurthestPoint() {
    return furthestPoint;
  }
}

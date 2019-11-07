package alexrnov.cosmichunter.utils.loader;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Загружает данные модели из файла *.obj. Выгружаются данные вершин,
 * индексов, нормалей, текстурных координат. Первоисточник:
 * https://www.dropbox.com/sh/x1fyet1otejxk3z/AAAoCqArl4cIx0THdRk2poW3a?dl=0
 * OpenGL 3D game tutorial
 */

public class OBJFileLoader {

  public ModelData loadOBJ(InputStream input) {

    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(input));
    } catch (Exception e) {
      Log.v("P", "Error readRawFile");
      return null;
    }

    String line;
    List<Vertex> vertices = new ArrayList<>();
    List<Vector2f> textures = new ArrayList<>();
    List<Vector3f> normals = new ArrayList<>();
    List<Integer> indices = new ArrayList<>();
    try {
      while (true) {
        line = reader.readLine();
        if (line.startsWith("v ")) {
          String[] currentLine = line.split(" ");
          Vector3f vertex = new Vector3f(Float.valueOf(currentLine[1]),
                  Float.valueOf(currentLine[2]),
                  Float.valueOf(currentLine[3]));
          Vertex newVertex = new Vertex(vertices.size(), vertex);
          vertices.add(newVertex);

        } else if (line.startsWith("vt ")) {
          String[] currentLine = line.split(" ");
          Vector2f texture = new Vector2f(Float.valueOf(currentLine[1]),
                  Float.valueOf(currentLine[2]));
          textures.add(texture);
        } else if (line.startsWith("vn ")) {
          String[] currentLine = line.split(" ");
          Vector3f normal = new Vector3f(Float.valueOf(currentLine[1]),
                  Float.valueOf(currentLine[2]),
                  Float.valueOf(currentLine[3]));
          normals.add(normal);
        } else if (line.startsWith("f ")) {
          break;
        }
      }
      while (line != null && line.startsWith("f ")) {
        String[] currentLine = line.split(" ");
        String[] vertex1 = currentLine[1].split("/");
        String[] vertex2 = currentLine[2].split("/");
        String[] vertex3 = currentLine[3].split("/");
        processVertex(vertex1, vertices, indices);
        processVertex(vertex2, vertices, indices);
        processVertex(vertex3, vertices, indices);
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      System.err.println("Error reading the file");
    }
    removeUnusedVertices(vertices);
    float[] verticesArray = new float[vertices.size() * 3];
    float[] texturesArray = new float[vertices.size() * 2];
    float[] normalsArray = new float[vertices.size() * 3];
    float furthest = convertDataToArrays(vertices, textures, normals, verticesArray,
            texturesArray, normalsArray);
    int[] indicesArray = convertIndicesListToArray(indices);
    return new ModelData(verticesArray, texturesArray, normalsArray, indicesArray,
            furthest);
  }

  private void processVertex(String[] vertex, List<Vertex> vertices,
                             List<Integer> indices) {
    int index = Integer.parseInt(vertex[0]) - 1;
    Vertex currentVertex = vertices.get(index);
    int textureIndex = Integer.parseInt(vertex[1]) - 1;
    int normalIndex = Integer.parseInt(vertex[2]) - 1;
    if (!currentVertex.isSet()) {
      currentVertex.setTextureIndex(textureIndex);
      currentVertex.setNormalIndex(normalIndex);
      indices.add(index);
    } else {
      dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex,
              indices, vertices);
    }
  }

  private int[] convertIndicesListToArray(List<Integer> indices) {
    int[] indicesArray = new int[indices.size()];
    for (int i = 0; i < indicesArray.length; i++) {
      indicesArray[i] = indices.get(i);
    }
    return indicesArray;
  }

  private float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures,
                                           List<Vector3f> normals, float[] verticesArray, float[] texturesArray,
                                           float[] normalsArray) {
    float furthestPoint = 0;
    for (int i = 0; i < vertices.size(); i++) {
      Vertex currentVertex = vertices.get(i);
      if (currentVertex.getLength() > furthestPoint) {
        furthestPoint = currentVertex.getLength();
      }
      Vector3f position = currentVertex.getPosition();
      Vector2f textureCoordinates = textures.get(currentVertex.getTextureIndex());
      Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
      verticesArray[i * 3] = position.x;
      verticesArray[i * 3 + 1] = position.y;
      verticesArray[i * 3 + 2] = position.z;
      texturesArray[i * 2] = textureCoordinates.x;
      texturesArray[i * 2 + 1] = 1 - textureCoordinates.y;
      normalsArray[i * 3] = normalVector.x;
      normalsArray[i * 3 + 1] = normalVector.y;
      normalsArray[i * 3 + 2] = normalVector.z;
    }
    return furthestPoint;
  }

  private void dealWithAlreadyProcessedVertex(Vertex previousVertex,
                                              int newTextureIndex,
                                              int newNormalIndex,
                                              List<Integer> indices,
                                              List<Vertex> vertices) {
    if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
      indices.add(previousVertex.getIndex());
    } else {
      Vertex anotherVertex = previousVertex.getDuplicateVertex();
      if (anotherVertex != null) {
        dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex,
                indices, vertices);
      } else {
        Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
        duplicateVertex.setTextureIndex(newTextureIndex);
        duplicateVertex.setNormalIndex(newNormalIndex);
        previousVertex.setDuplicateVertex(duplicateVertex);
        vertices.add(duplicateVertex);
        indices.add(duplicateVertex.getIndex());
      }
    }
  }

  private void removeUnusedVertices(List<Vertex> vertices){
    for(Vertex vertex: vertices){
      if(!vertex.isSet()){
        vertex.setTextureIndex(0);
        vertex.setNormalIndex(0);
      }
    }
  }
}

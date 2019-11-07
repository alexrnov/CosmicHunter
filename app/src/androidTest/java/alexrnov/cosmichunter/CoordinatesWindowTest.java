package alexrnov.cosmichunter;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import alexrnov.cosmichunter.utils.commonGL.CoordinatesWindow;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class CoordinatesWindowTest {

  //минимальная разница вещественных чисел - пять знаков после разделителя
  private final double DELTA = 1e-5;
  private CoordinatesWindow cw;

  private float[] mvpMatrix = {-0.7839225f, 0.30831152f, 0.53889656f, 0.0f,
                                0.022926152f, 0.8817689f, -0.47112417f, 0.0f,
                                -0.62043524f, -0.35697f, -0.69830686f, 0.0f,
                                0.30499986f, 0.04500003f, -3.0f, 1.0f};

  private float[] projectionMatrix = {5.9999995f, 0.0f, 0.0f, 0.0f,
                                      0.0f, 3.652762f, 0.0f, 0.0f,
                                      0.0f, 0.0f, -1.0100503f, -1.0f,
                                      0.0f, 0.0f, -0.40201005f, 0.0f};

  @Before
  public void setUp() {
    final int widthScreen = 1080;
    final int heightScreen = 1774;
    cw = new CoordinatesWindow(widthScreen, heightScreen);
  }

  @After
  public void tearDown() {
    cw = null;
  }

  @Test //протестировать побочный эффект от вызова метода
  public void fromVertexOfObjectOpenGL() {
    cw.setMatrices(mvpMatrix, projectionMatrix);
    cw.fromVertexOfObjectOpenGL(0.05f, 0.05f, -0.05f);
    assertEquals(865.9715, cw.getX(), DELTA);
    assertEquals(1020.8498, cw.getY(), DELTA);
    assertEquals(0.9371569, cw.getZ(), DELTA);
  }

  @Test //протестировать побочный эффект от вызова метода
  public void fromCenterOfObjectOpenGL() {
    cw.setMatrices(mvpMatrix, projectionMatrix);
    cw.fromCenterOfObjectOpenGL();
    assertEquals(869.399841, cw.getX(), DELTA);
    assertEquals(935.600036, cw.getY(), DELTA);
    assertEquals(0.938023, cw.getZ(), DELTA);
  }
}

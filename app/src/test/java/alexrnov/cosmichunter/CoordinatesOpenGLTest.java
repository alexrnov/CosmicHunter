package alexrnov.cosmichunter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import alexrnov.cosmichunter.utils.commonGL.CoordinatesOpenGL;

import static org.junit.Assert.*;
public class CoordinatesOpenGLTest {

  //минимальная разница вещественных чисел - шесть знаков после разделителя
  private static final double DELTA = 1e-7;
  private CoordinatesOpenGL c;

  @Before
  public void setUp() {
    c = new CoordinatesOpenGL();
  }

  @After
  public void tearDown() {
    c = null;
  }

  @Test //побочный эффект от вызова метода
  public void fromDisplay() {
    c.fromDisplay(1080, 1774, 878.1869f, 261.8376f);
    assertEquals(0.626272, c.getXGL(), DELTA);
    assertEquals(0.70480543, c.getYGL(), DELTA);

    c.fromDisplay(1080, 1774, 806.2535f, 1335.2784f);
    assertEquals(0.493062, c.getXGL(), DELTA);
    assertEquals(-0.5053872, c.getYGL(), DELTA);

    c.fromDisplay(1080, 1774, 262.7567f,348.7923f);
    assertEquals(-0.5134135, c.getXGL(), DELTA);
    assertEquals(0.6067731, c.getYGL(), DELTA);

    c.fromDisplay(1080, 1774, 237.77983f, 1496.1947f);
    assertEquals(-0.55966693, c.getXGL(), DELTA);
    assertEquals(-0.6868035, c.getYGL(), DELTA);

    c.fromDisplay(1836, 1030, 1402.2697f, 303.67252f);
    assertEquals(0.52752686, c.getXGL(), DELTA);
    assertEquals(0.41034463, c.getYGL(), DELTA);
  }
}

package alexrnov.cosmichunter.utils.commonGL;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Содержит методы для получения числовых буферов
 */
public class Buffers {

  public static FloatBuffer floatBuffer(float[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 4)
                            .order(ByteOrder.nativeOrder());
    FloatBuffer returnBuffer = byteBuffer.asFloatBuffer();
    returnBuffer.put(data).position(0);
    return returnBuffer;
  }

  public static DoubleBuffer doubleBuffer(double[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 8)
                            .order(ByteOrder.nativeOrder());
    DoubleBuffer returnBuffer = byteBuffer.asDoubleBuffer();
    returnBuffer.put(data).position(0);
    return returnBuffer.put(data);
  }

  public static ShortBuffer shortBuffer(short[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 2)
                            .order(ByteOrder.nativeOrder());
    ShortBuffer returnBuffer = byteBuffer.asShortBuffer();
    returnBuffer.put(data).position(0);
    return returnBuffer.put(data);
  }

  public static CharBuffer charBuffer(char[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 2)
                            .order(ByteOrder.nativeOrder());
    CharBuffer returnBuffer = byteBuffer.asCharBuffer();
    returnBuffer.put(data).position(0);
    return returnBuffer.put(data);
  }

  public static IntBuffer intBuffer(int[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 4)
                            .order(ByteOrder.nativeOrder());
    IntBuffer returnBuffer = byteBuffer.asIntBuffer();
    returnBuffer.put(data).position(0);
    return returnBuffer.put(data);
  }

  public static LongBuffer longBuffer(long[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length * 8)
                            .order(ByteOrder.nativeOrder());
    LongBuffer returnBuffer = byteBuffer.asLongBuffer();
    returnBuffer.put(data).position(0);
    return returnBuffer.put(data);
  }

  public static ByteBuffer byteBuffer(byte[] data) {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length);
    byteBuffer.order(ByteOrder.nativeOrder());
    byteBuffer.put(data).position(0);
    return byteBuffer;
  }
}

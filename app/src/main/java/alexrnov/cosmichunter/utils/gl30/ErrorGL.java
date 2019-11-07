package alexrnov.cosmichunter.utils.gl30;

import android.opengl.GLES30;
import android.util.Log;

public class ErrorGL {

  private static final String TAG = "ErrorGL";
  /**
   * Utility method for debugging OpenGL calls. Provide the name of the call
   * just after making it:
   *
   * <pre>
   * mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
   * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
   *
   * If the operation is not successful, the check throws an error.
   * @param glOperation - Name of the OpenGL call to check.
   */
  public static void checkGlError(String glOperation) {
    int error;
    while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
      Log.e("P", glOperation + ": glError " + error);
      throw new RuntimeException(glOperation + ": glError " + error);
    }
  }
}

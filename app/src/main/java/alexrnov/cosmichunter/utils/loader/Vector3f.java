package alexrnov.cosmichunter.utils.loader;

/**
 * По материалам: OpenGL 3D game tutorial
 * https://github.com/tdgroot/opengl/tree/master/src/org/lwjgl/util/vector
 */
public class Vector3f {

  public float x,y,z;

  Vector3f(float x, float y, float z){
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3f(){
    this.x = 0.0f;
    this.y = 0.0f;
    this.z = 0.0f;
  }

  public float length() {
    return( (float)Math.sqrt( x * x + y * y + z * z ) );
  }
}

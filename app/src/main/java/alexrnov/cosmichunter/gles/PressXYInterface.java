package alexrnov.cosmichunter.gles;
@FunctionalInterface
public interface PressXYInterface<T, U> {
  void invoke(T x, U y);
}

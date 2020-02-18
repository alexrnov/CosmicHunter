package alexrnov.cosmichunter.gles;

import android.content.Context;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static alexrnov.cosmichunter.Initialization.TAG;
import static alexrnov.cosmichunter.concurrent.ViewHandlerKt.HITS_CODE;
import static alexrnov.cosmichunter.concurrent.ViewHandlerKt.MESSAGE_CODE;
import static alexrnov.cosmichunter.concurrent.ViewHandlerKt.ROCKETS_CODE;

import alexrnov.cosmichunter.ExplosionSound;
import alexrnov.cosmichunter.GunSound;
import alexrnov.cosmichunter.activities.GameActivity;
import alexrnov.cosmichunter.Object3D;
import alexrnov.cosmichunter.VibratorExplosion;
import alexrnov.cosmichunter.gles.objects.Asteroid;
import alexrnov.cosmichunter.gles.objects.Background;
import alexrnov.cosmichunter.gles.objects.Explosion;
import alexrnov.cosmichunter.gles.objects.IceAsteroid;
import alexrnov.cosmichunter.gles.objects.MetalAsteroid;
import alexrnov.cosmichunter.gles.objects.BasaltAsteroid;
import alexrnov.cosmichunter.gles.objects.Rocket;
import alexrnov.cosmichunter.utils.MeanValue;
import alexrnov.cosmichunter.utils.commonGL.CoordinatesOpenGL;
import alexrnov.cosmichunter.view.AsteroidView3D;
import alexrnov.cosmichunter.view.BackgroundView3D;
import alexrnov.cosmichunter.view.RocketView3D;

public class Level4 implements GLSurfaceView.Renderer, SceneRenderer {
  private final String className = this.getClass().getSimpleName() + ".class: ";
  private Context context; // нужно ли синхронизировать?
  private Object3D backgroundObject3D; // нужно ли синхронизировать?

  private GameActivity gameActivity;
  private int numberOfHints = 0; // количество попаданий в астероиды
  private int numbersOfRockets = 50; // количество оставшихся ракет
  // переменные используются в другом потоке (main)
  private volatile int widthDisplay;
  private volatile int heightDisplay;

  private float smoothedDeltaRealTime_ms = 16.0f; // initial value, Optionally you can save the new computed value (will change with each hardware) in Preferences to optimize the first drawing frames
  private float movAverageDeltaTime_ms = smoothedDeltaRealTime_ms; // mov Average start with default value
  private long lastRealTimeMeasurement_ms; // temporal storage for last time measurement

  // smooth constant elements to play with
  private static final float movAveragePeriod = 5; // #frames involved in average calc (suggested values 5-100)
  private static final float smoothFactor = 0.1f; // adjusting ratio (suggested values 0.01-0.5)

  private MeanValue meanValue = new MeanValue((short) 5000);

  private float totalVirtualRealTime_ms = 0;
  private float speedAdjustments_ms = 0; // to introduce a virtual Time for the animation (reduce or increase animation speed)
  private float totalAnimationTime_ms=0;
  private float fixedStepAnimation_ms = 20; // 20ms for a 50FPS descriptive animation
  private float interpolationRatio = 0;

  private float delta;
  private final byte NUMBER_ASTEROIDS = 10; // количество астероидов
  // Массив астероидов. Если бы объектов было много, возможно,
  // понадобилось применение паттерна "Приспособленец"
  private Asteroid[] asteroids = new Asteroid[NUMBER_ASTEROIDS];

  private final byte NUMBER_ROCKETS = 15; // максимальное количество ракет
  private Rocket[] allRockets = new Rocket[NUMBER_ROCKETS]; // все ракеты
  private List<Rocket> flyRockets = new ArrayList<>(); // летящие ракеты
  private int indexOfRocket = 0; // индекс ракеты, которая будет запущена

  // флаг определяет открыто ли приложение первый раз
  private boolean firstRun = true;
  private CoordinatesOpenGL coordinatesOpenGL = new CoordinatesOpenGL();

  // Коллекция для активных взрывов. Применяется для того, чтобы
  // для каждого взрыва не осуществлять проверку - нужно ли вообще его
  // рисовать. При таком подходе, объект просто удаляется из коллекции.
  // Использовать конструктор CopyOnWriteArrayList() вместо ArrayList()
  // что-бы не было ConcurrentModificationException.
  private List<Explosion> activeExplosions = new CopyOnWriteArrayList<>();

  private VibratorExplosion vibratorExplosion;
  enum TypeExplosion { ROCK_BIG, ROCK_MIDDLE, ROCK_SMALL, ICE_BIG,
    ICE_MIDDLE, ICE_SMALL, METAL_BIG, METAL_MIDDLE, METAL_SMALL }

  private double versionGL;

  public Level4(double versionGL, Context context) {
    Log.i(TAG, "versionGL = " + versionGL + ", Level4");
    this.versionGL = versionGL;
    this.context = context;
  }

  //вхожение в поток OpenGL
  public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
    Log.i(TAG, className + "onSurfaceCreated()");
    // установить приоритет потока OpenGL как самый высокий
    Thread.currentThread().setPriority(10);

    GLES20.glClearColor(0.3f, 0.3f, 0.3f, 0.3f);

    // реализация делает предпочтение на быстродействие
    GLES20.glHint(GLES20.GL_GENERATE_MIPMAP_HINT, GLES20.GL_FASTEST);
    // реаоизация делает предпочтение на точность
    //GLES30.glHint(GLES30.GL_GENERATE_MIPMAP_HINT, GLES30.GL_NICEST);
    // реализация сама может выбирать между точностью и быстродействием
    // (используется по умолчанию)
    //GLES30.glHint(GLES30.GL_GENERATE_MIPMAP_HINT, GLES30.GL_DONT_CARE);
    /*
    int[] hint = new int[1]; // текущее значение пожелания
    GLES30.glGetIntegerv(GLES30.GL_GENERATE_MIPMAP_HINT,
            hint, 0);
    Log.v("P", "hint:  " + hint[0]);
    */
    //GLES30.glHint(GLES30.GL_FRAGMENT_SHADER_DERIVATIVE_HINT,
    //GLES30.GL_FASTEST); // required version API 18
    asteroids[0] = new MetalAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[0]);
    asteroids[1] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[1]);
    asteroids[2] = new BasaltAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[2]);
    asteroids[3] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[3]);

    asteroids[4] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[4]);
    asteroids[5] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[5]);
    asteroids[6] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[6]);
    asteroids[7] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[7]);
    asteroids[8] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[8]);
    asteroids[9] = new IceAsteroid(versionGL, context, 1.0f);
    bindExplosions(asteroids[9]);

    for (int i = 0; i < NUMBER_ROCKETS; i++) allRockets[i] = new Rocket(versionGL, context, 1.0f);
    // значение scale установлено с запасом, поскольку на различных
    // устройствах могут появляться полоски по бокам экрана
    backgroundObject3D = new Background(versionGL, context, 90.0f);

    vibratorExplosion = new VibratorExplosion(context, 50);

    Log.i(TAG, "GL version: " + glUnused.glGetString(GL10.GL_VERSION));
    // расширения, поддерживаемые на данном устройстве
    // Log.i(TAG, "GL extensions: " + glUnused.glGetString(GL10.GL_EXTENSIONS));
    // подержка стандарта сжатия текстур ETC1
    Log.i(TAG, "ETC1 supported: " + ETC1Util.isETC1Supported());
  }

  // обработчик изменения ориентации экрана, вызывается также при возврате к приложению
  public void onSurfaceChanged(GL10 glUnused, int width, int height) {
    Log.i(TAG, className + "onSurfaceChanged() w: " + width + ", h: " + height);

    widthDisplay = width;
    heightDisplay = height;

    GLES20.glViewport(0, 0, width, height); //установить размер экрана
    //создать объект поведения только один раз, чтобы при повторном вызове
    //метода onSurfaceCreated() (например, когда приложение скрывается, а
    //потом открывается вновь) объект имел координаты в пространстве
    //такие же как и перед закрытием
    if (firstRun) { // первый запуск приложения
      backgroundObject3D.setView(new BackgroundView3D(width, height));
      float z = -95;
      for (Asteroid asteroid: asteroids) {
        AsteroidView3D view = new AsteroidView3D(width, height);
        view.setZ(z);
        asteroid.setView(view);
        z = z - 30;
        asteroid.getBigExplosion().createDataVertex(width, height);
        asteroid.getMiddleExplosion().createDataVertex(width, height);
        asteroid.getSmallExplosion().createDataVertex(width, height);
      }
      for (Object3D rocket: allRockets) {
        rocket.setView(new RocketView3D(width, height));
      }
      firstRun = false;
    }
  }

  public void onDrawFrame(GL10 glUnused) { // вызывается при перерисовке кадра
    delta = meanValue.add(interpolationRatio);
    defineFlyRocket();
    for (Rocket rocket: flyRockets) rocket.getView().spotPosition(delta);
    for (Asteroid asteroid: asteroids) {
      asteroid.getView().spotPosition(delta);

      if (asteroid.getView().checkHit(flyRockets)) {
        numberOfHints++;
        gameActivity.handleState(HITS_CODE, String.valueOf(numberOfHints));
        if (numberOfHints == 10) {
          gameActivity.handleState(MESSAGE_CODE, String.valueOf("уровень пройден"));
        }
        createExplosion(asteroid);
        ExplosionSound.createExplosion(gameActivity);
      }

    }

    //установить буфер цвета
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    // ориентация против часовой стрелки (используется по умолчанию)
    // GLES30.glFrontFace(GLES30.GL_CCW);
    GLES20.glEnable(GLES20.GL_CULL_FACE); // разрешить отбрасывание
    // отбрасывать заднюю грань примитивов при рендеринге
    GLES20.glCullFace(GLES20.GL_BACK);
    // включить тест глубины, который нужен для правильного отображения
    // материала, иначе объекты будут выглядеть неправильно
    GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    backgroundObject3D.draw();

    for (Asteroid asteroid: asteroids) asteroid.draw();

    /* отрисовка только летящих ракет */
    for (Rocket rocket: flyRockets) rocket.draw();

    /* отрисовка только активных взрывов */
    for (Explosion explosion: activeExplosions) explosion.draw(delta);

    flyRockets.clear();
    defineDeltaTime();
  }

  // добавляет в коллекцию flyRockets только летящие ракеты
  private void defineFlyRocket() {
    for (Rocket rocket: allRockets) {
      if (rocket.getView().getFly()) flyRockets.add(rocket);
    }
  }

  /*
   * Проверить было ли попадание ракеты в астероид. Если было, тогда
   * создать взрыв. Координаты центра взрыва выводятся на основе оконных
   * координат астероида, какими они были в момент попадания ракеты
   */
  private void createExplosion(Asteroid asteroid) {
    vibratorExplosion.execute();
    asteroid.getView().setHit(false);
    coordinatesOpenGL.fromDisplay(this.getWidthDisplay(), this.getHeightDisplay(),
            asteroid.getView().getPixelX(), // координаты астероида в пикселах
            asteroid.getView().getPixelY()); // экрана в момент взрыва
    // создать взрыв, присвоив ему координаты центра в координатах OpenGL
    // значение для y делается отрицательным, поскольку event рендера
    // отсчитывает координаты от низа, а GLU функция - от верха экрана
    // в зависимости от дальности назначить: большой, средний или маленький взрыв
    if (asteroid.getView().getZ() > -10) { // ближний взрыв
      asteroid.getBigExplosion().create(coordinatesOpenGL.getXGL(), -coordinatesOpenGL.getYGL());
      activeExplosions.add(asteroid.getBigExplosion());
    } else if (asteroid.getView().getZ() > -17) { // средний взрыв
      asteroid.getMiddleExplosion().create(coordinatesOpenGL.getXGL(), -coordinatesOpenGL.getYGL());
      activeExplosions.add(asteroid.getMiddleExplosion());
    } else { // дальний взрыв
      asteroid.getSmallExplosion().create(coordinatesOpenGL.getXGL(), -coordinatesOpenGL.getYGL());
      activeExplosions.add(asteroid.getSmallExplosion());
    }
    asteroid.getView().beginning();
  }

  // resolve javqui https://stackoverflow.com/questions/10648325/android-smooth-game-loop
  private void defineDeltaTime() {
    totalVirtualRealTime_ms += smoothedDeltaRealTime_ms + speedAdjustments_ms;
    while (totalVirtualRealTime_ms > totalAnimationTime_ms) {
      totalAnimationTime_ms += fixedStepAnimation_ms;
    }

    interpolationRatio = (totalAnimationTime_ms - totalVirtualRealTime_ms)
            / fixedStepAnimation_ms;

    long currTimePick_ms = SystemClock.uptimeMillis();
    float realTimeElapsed_ms;
    if (lastRealTimeMeasurement_ms > 0) {
      realTimeElapsed_ms = (currTimePick_ms - lastRealTimeMeasurement_ms);
    } else {
      realTimeElapsed_ms = smoothedDeltaRealTime_ms; // just the first time
    }
    movAverageDeltaTime_ms = (realTimeElapsed_ms + movAverageDeltaTime_ms
            * (movAveragePeriod-1)) / movAveragePeriod;

    // Calc a better approximation for smooth stepTime
    smoothedDeltaRealTime_ms = smoothedDeltaRealTime_ms +
            (movAverageDeltaTime_ms - smoothedDeltaRealTime_ms) * smoothFactor;

    lastRealTimeMeasurement_ms = currTimePick_ms;
  }

  /** @return ширина дисплея OpenGL (в пикселах) */
  public int getWidthDisplay() { return widthDisplay; }

  /** @return высота дисплея OpenGL (в пикселах) */
  public int getHeightDisplay() { return heightDisplay; }

  public void setPassXY(float passX, float passY) {
    GunSound.createGun(gameActivity);
    RocketView3D rv = allRockets[indexOfRocket].getView();
    rv.setXYEvent(passX, passY);
    indexOfRocket++;
    if (indexOfRocket > NUMBER_ROCKETS - 1) {
      indexOfRocket = 0;
    }
    numbersOfRockets--;
    gameActivity.handleState(ROCKETS_CODE, String.valueOf(numbersOfRockets));
  }

  /*
   * К каждому астероиду привязать по три взрыва (большой, средний, маленький)
   */
  private void bindExplosions(Asteroid asteroid) {
    if (asteroid instanceof BasaltAsteroid) {
      asteroid.setBigExplosion(createExplosion(TypeExplosion.ROCK_BIG));
      asteroid.setMiddleExplosion(createExplosion(TypeExplosion.ROCK_MIDDLE));
      asteroid.setSmallExplosion(createExplosion(TypeExplosion.ROCK_SMALL));
    } else if (asteroid instanceof MetalAsteroid) {
      asteroid.setBigExplosion(createExplosion(TypeExplosion.METAL_BIG));
      asteroid.setMiddleExplosion(createExplosion(TypeExplosion.METAL_MIDDLE));
      asteroid.setSmallExplosion(createExplosion(TypeExplosion.METAL_SMALL));
    } else {
      asteroid.setBigExplosion(createExplosion(TypeExplosion.ICE_BIG));
      asteroid.setMiddleExplosion(createExplosion(TypeExplosion.ICE_MIDDLE));
      asteroid.setSmallExplosion(createExplosion(TypeExplosion.ICE_SMALL));
    }
    asteroid.getBigExplosion().setExplosions(activeExplosions);
    asteroid.getMiddleExplosion().setExplosions(activeExplosions);
    asteroid.getSmallExplosion().setExplosions(activeExplosions);
  }

  private Explosion createExplosion(TypeExplosion type) {
    switch (type) {
      case ROCK_BIG:
        return new Explosion(versionGL, context, "explosion/rock.png");
      case ROCK_MIDDLE:
        return new Explosion(versionGL, context, "explosion/rock.png", 0.001f,
                0.4f, 80.0f, 120, new float[] {1.0f, 0.7f, 0.1f, 1.0f});
      case ROCK_SMALL:
        return new Explosion(versionGL, context, "explosion/rock.png", 0.005f,
                0.2f, 60.0f, 110, new float[] {1.0f, 0.7f, 0.1f, 1.0f});
      case ICE_BIG:
        return new Explosion(versionGL, context, "explosion/ice.png", 0.05f,
                0.6f, 100.0f, 150, new float[] {0.1f, 0.4f, 1.0f, 1.0f}); // синий
      case ICE_MIDDLE:
        return new Explosion(versionGL, context, "explosion/ice.png", 0.001f,
                0.4f, 80.0f, 120, new float[] {0.1f, 0.4f, 1.0f, 1.0f});
      case ICE_SMALL:
        return new Explosion(versionGL, context, "explosion/ice.png", 0.005f,
                0.2f, 60.0f, 110, new float[] {0.1f, 0.4f, 1.0f, 1.0f});
      case METAL_BIG:
        return new Explosion(versionGL,context, "explosion/metal.png", 0.05f,
                0.6f, 100.0f, 150, new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      case METAL_MIDDLE:
        return new Explosion(versionGL, context, "explosion/metal.png", 0.001f,
                0.4f, 80.0f, 120, new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      case METAL_SMALL:
        return new Explosion(versionGL, context, "explosion/metal.png", 0.005f,
                0.2f, 60.0f, 110, new float[] {0.3f, 1.0f, 0.3f, 1.0f});
      default:
        return null;
    }
  }

  public void setGameActivity(@NotNull GameActivity gameActivity) {
    this.gameActivity = gameActivity;
  }

}

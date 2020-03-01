package alexrnov.cosmichunter.testutils;

import android.view.View;

import org.hamcrest.Matcher;

/**
 * Статические классы позволяют проверять соответствие bitMap and vector drawable для компонентов View.
 * Solution of a_subscriber and Anatolii https://stackoverflow.com/questions/50800133/android-espresso-cant-check-background
 */
public class EspressoTestsMatchers {

  public static Matcher<View> withDrawable(final int resourceId) {
    return new DrawableMatcher(resourceId);
  }

  public static Matcher<View> noDrawable() {
    return new DrawableMatcher(-1);
  }
}
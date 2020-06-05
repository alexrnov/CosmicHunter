package alexrnov.cosmichunter.testutils;

import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import androidx.test.espresso.matcher.BoundedMatcher;

public class SeekBarMatcher {
  public static Matcher<View> withProgress(final int expectedProgress) {
    return new BoundedMatcher<View, SeekBar>(SeekBar.class) {
      @Override
      public void describeTo(Description description) {
        description.appendText("expected: ");
        description.appendText(""+expectedProgress);
      }

      @Override
      public boolean matchesSafely(SeekBar seekBar) {
        return seekBar.getProgress() == expectedProgress;
      }
    };
  }
}

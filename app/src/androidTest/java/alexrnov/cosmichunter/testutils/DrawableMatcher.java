package alexrnov.cosmichunter.testutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import static alexrnov.cosmichunter.Initialization.TAG;

/**
 * Позволяет проверять соответствие bitMap and vector drawable для компонентов View.
 * Solution of a_subscriber and Anatolii https://stackoverflow.com/questions/50800133/android-espresso-cant-check-background
 */
public class DrawableMatcher extends TypeSafeMatcher<View> {

  private final int expectedId;
  private String resourceName;
  static final int EMPTY = -1;
  static final int ANY = -2;

  DrawableMatcher(int expectedId) {
    super(View.class);
    this.expectedId = expectedId;
  }

  @Override
  protected boolean matchesSafely(View view) {
    return sameBitmap(view.getContext(), view.getBackground(), expectedId);
  }

  private Bitmap getBitmap(Drawable drawable) {
    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
    drawable.draw(canvas);
    return bitmap;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("with drawable from resource id: ");
    description.appendValue(expectedId);
    if (resourceName != null) {
      description.appendText("[");
      description.appendText(resourceName);
      description.appendText("]");
    }
  }


  private static boolean sameBitmap(Context context, Drawable drawable, int expectedId) {
    Drawable expectedDrawable = ContextCompat.getDrawable(context, expectedId);
    if (drawable == null || expectedDrawable == null) {
      return false;
    }

    if (drawable instanceof StateListDrawable && expectedDrawable instanceof StateListDrawable) {
      drawable = drawable.getCurrent();
      expectedDrawable = expectedDrawable.getCurrent();
    }
    if (drawable instanceof BitmapDrawable) {
      Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
      Bitmap otherBitmap = ((BitmapDrawable) expectedDrawable).getBitmap();
      return bitmap.sameAs(otherBitmap);
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (drawable instanceof VectorDrawable ||
              drawable instanceof VectorDrawableCompat ||
              drawable instanceof GradientDrawable) {
        Rect drawableRect = drawable.getBounds();
        Bitmap bitmap = Bitmap.createBitmap(drawableRect.width(), drawableRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        Bitmap otherBitmap = Bitmap.createBitmap(drawableRect.width(), drawableRect.height(), Bitmap.Config.ARGB_8888);
        Canvas otherCanvas = new Canvas(otherBitmap);
        expectedDrawable.setBounds(0, 0, otherCanvas.getWidth(), otherCanvas.getHeight());
        expectedDrawable.draw(otherCanvas);
        return bitmap.sameAs(otherBitmap);
      }
    }
    return false;
  }
}
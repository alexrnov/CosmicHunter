package alexrnov.cosmichunter.utils;

import android.content.Context;
import android.content.res.Resources;
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
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import androidx.core.content.ContextCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import static alexrnov.cosmichunter.Initialization.TAG;

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
    Log.i(TAG, "0000000000");
    return sameBitmap(view.getContext(), view.getBackground(), expectedId);

    /*
    if (!(target instanceof ImageView)) {
      return false;
    }
    ImageView imageView = (ImageView) target;
    if (expectedId == EMPTY) {
      return imageView.getDrawable() == null;
    }
    if (expectedId == ANY) {
      return imageView.getDrawable() != null;
    }
    Resources resources = target.getContext().getResources();
    Drawable expectedDrawable = resources.getDrawable(expectedId);
    resourceName = resources.getResourceEntryName(expectedId);

    if (expectedDrawable == null) {
      return false;
    }

    Bitmap bitmap = getBitmap(imageView.getDrawable());
    Bitmap otherBitmap = getBitmap(expectedDrawable);
    return bitmap.sameAs(otherBitmap);
  */
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
    Log.i(TAG, "22222222222");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      if (drawable instanceof VectorDrawable ||
              drawable instanceof VectorDrawableCompat ||
              drawable instanceof GradientDrawable) {
        Rect drawableRect = drawable.getBounds();
        Bitmap bitmap = Bitmap.createBitmap(drawableRect.width(), drawableRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        Log.i(TAG, "33333333333");
        Bitmap otherBitmap = Bitmap.createBitmap(drawableRect.width(), drawableRect.height(), Bitmap.Config.ARGB_8888);
        Canvas otherCanvas = new Canvas(otherBitmap);
        expectedDrawable.setBounds(0, 0, otherCanvas.getWidth(), otherCanvas.getHeight());
        expectedDrawable.draw(otherCanvas);
        Log.i(TAG, "555555555");
        return bitmap.sameAs(otherBitmap);
      }
    }
    return false;
  }
}
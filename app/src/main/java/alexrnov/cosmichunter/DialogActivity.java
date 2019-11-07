package alexrnov.cosmichunter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class DialogActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dialog);
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int width = dm.widthPixels;
    int height = dm.heightPixels;
    if (width < height) {
      getWindow().setLayout((int) (width * .8), (int) (height * .4));
    } else {
      getWindow().setLayout((int) (width * .5), (int) (height * .7));
    }
  }

  public void backToMainMenu(View view) {
    startActivity(new Intent(this, MainActivity.class));
  }

  public void cancel(View view) {
    finish();
  }

  @Override
  public void onStop() {
    super.onStop();
    finish();
  }
}

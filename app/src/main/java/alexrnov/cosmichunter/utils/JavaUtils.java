package alexrnov.cosmichunter.utils;

import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import alexrnov.cosmichunter.R;

public class JavaUtils {

  public static void showToast2(AppCompatActivity app) {
    LayoutInflater inflater = app.getLayoutInflater();
    //первый параметр - xml-файл с представлением, второй параметр
    //- корневой вьюер в этом файле
    View layout = inflater.inflate(R.layout.custom_toast,
            (ViewGroup) app.findViewById(R.id.custom_toast_container));

    TextView text = (TextView) layout.findViewById(R.id.text_toast);
    text.setText("OpenGL не поддерживается");
    Toast toast = new Toast(app.getApplicationContext());
    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 20);
    toast.setDuration(Toast.LENGTH_SHORT);
    toast.setView(layout);
    toast.show();
  }
}

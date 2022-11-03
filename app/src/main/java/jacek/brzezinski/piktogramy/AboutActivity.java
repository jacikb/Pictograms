package jacek.brzezinski.piktogramy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tv_title = findViewById(R.id.aboutTextViewTitle);
        TextView tv_phone = findViewById(R.id.aboutTextViewPhone);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AboutActivity.this);
        tv_title.setText("Właściciel " + prefs.getString("owner", "..."));
        tv_phone.setText(getResources().getString(R.string.about_author_phone) + " " + prefs.getString("phone", "..."));
        if (!prefs.getString("phone", "...").equals("737 456 220")) {
            ImageView iv_image = findViewById(R.id.imageViewPawel);
            iv_image.setVisibility(View.INVISIBLE);
        }
    }
}
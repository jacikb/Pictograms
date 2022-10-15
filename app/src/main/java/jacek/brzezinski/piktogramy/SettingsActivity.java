package jacek.brzezinski.piktogramy;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {
    String[] pictograms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

//        Resources res = getResources();
//        pictograms = res.getStringArray(R.array.pictograms);

//        Button button = (Button) findViewById(R.id.button_setup_close);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                closeSetupActivity();
//            }
//        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
//    public void closeSetupActivity() {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//    }
}
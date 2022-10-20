package jacek.brzezinski.piktogramy;
/**
 * Audio from https://soundoftext.com/
 */

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private IntentFilter intentBatteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

    private MediaPlayer mp;
    private static Resources resources;
    private static String packageName;


    GridView simpleGrid;
    int gridSize = 140;
    ArrayList<Integer> logos = new ArrayList<Integer>();
    ArrayList<Integer> audios = new ArrayList<Integer>();
    List<PictogramModel> pictograms;

    ArrayAdapter customerArrayAdapter;
    DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        resources = this.getResources();
        packageName = this.getPackageName();
        databaseHelper = new DataBaseHelper(MainActivity.this);


        ShowPictograms(databaseHelper);
//        Log.d(TAG, "onCreate");
        mp = MediaPlayer.create(MainActivity.this, R.raw.start);

        if (audios.size() == 0) {
            updateFromPreferences();
        }


        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = pictograms.get(position).getPath();
                //111
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                    }
                    mp = MediaPlayer.create(MainActivity.this, getResource("raw", path));
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // set an Intent to Another Activity
//                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                intent.putExtra("image", logos[position]); // put image data in Intent
//                startActivity(intent); // start Intent
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_app:
                openSettingsActivity();
                return true;
            case R.id.action_settings_pictograms:
                openSettingsPictogramActivity();
                return true;

            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }
    public void openSettingsPictogramActivity() {
        Intent intent = new Intent(this, PictogramSettingsActivity.class);
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(batteryReceiver, intentBatteryFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryReceiver);
        super.onPause();
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateFromPreferences();
        recreate();
    }

    private void ShowPictograms(DataBaseHelper dataBaseHelper) {

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        // simpleGrid.setNumColumns((int) (dpWidth / (120 + (gridSize * 40))));
        //1024 or 600
        simpleGrid.setNumColumns((int) (dpWidth / gridSize));
        Log.i(TAG, "************************* ShowPictograms");
        pictograms = databaseHelper.getAll(true);

        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, (int) (dpWidth / (100 + (gridSize * 40))), pictograms);
        Log.i(TAG, "************************* ShowPictograms customAdapter");
        simpleGrid.setAdapter(customAdapter);

//        customerArrayAdapter = new ArrayAdapter<PictogramModel>(MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper.getAll(true));
//        simpleGrid.setAdapter(customerArrayAdapter);

    }

    public void updateFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        logos.clear();
        audios.clear();
//        if (prefs.getBoolean("p_tak", true)) {
//            logos.add(R.drawable.p_tak);
//            audios.add(R.raw.tak);
//        }
//        if (prefs.getBoolean("p_nie", true)) {
//            logos.add(R.drawable.p_nie);
//            audios.add(R.raw.nie);
//        }
//        if (prefs.getBoolean("p_koniec", true)) {
//            logos.add(R.drawable.p_koniec);
//            audios.add(R.raw.koniec);
//        }
//        if (prefs.getBoolean("p_toaleta", true)) {
//            logos.add(R.drawable.p_toaleta);
//            audios.add(R.raw.toaleta);
//        }
//        if (prefs.getBoolean("p_pic", true)) {
//            logos.add(R.drawable.p_pic);
//            audios.add(R.raw.pic);
//        }
//        if (prefs.getBoolean("p_jesc", true)) {
//            logos.add(R.drawable.p_jesc);
//            audios.add(R.raw.jesc);
//        }
//        if (prefs.getBoolean("p_kanapka", true)) {
//            logos.add(R.drawable.p_kanapka);
//            audios.add(R.raw.kanapka);
//        }
//        if (prefs.getBoolean("p_owoce", true)) {
//            logos.add(R.drawable.p_owoce);
//            audios.add(R.raw.owoce);
//        }
//        if (prefs.getBoolean("p_czekolada", true)) {
//            logos.add(R.drawable.p_czekolada);
//            audios.add(R.raw.czekolada);
//        }
//        if (prefs.getBoolean("p_jablko", true)) {
//            logos.add(R.drawable.p_jablko);
//            audios.add(R.raw.jablko);
//        }
//        if (prefs.getBoolean("p_komputer", true)) {
//            logos.add(R.drawable.p_komputer);
//            audios.add(R.raw.komputer);
//        }
//        if (prefs.getBoolean("p_jeszcze", true)) {
//            logos.add(R.drawable.p_jeszcze);
//            audios.add(R.raw.jeszcze);
//        }
//        if (prefs.getBoolean("p_kapac", true)) {
//            logos.add(R.drawable.p_kapac);
//            audios.add(R.raw.kapac);
//        }
//        if (prefs.getBoolean("p_odpoczac", true)) {
//            logos.add(R.drawable.p_odpoczac);
//            audios.add(R.raw.odpoczac);
//        }
//        if (prefs.getBoolean("p_spac", true)) {
//            logos.add(R.drawable.p_spac);
//            audios.add(R.raw.spac);
//        }
        gridSize = Integer.parseInt(prefs.getString("grid_size", "140"));
        if (gridSize < 100 || gridSize > 180) {
            gridSize = 140;
        }
    }

    /**
     * @param resName drawable, raw
     * @param name
     * @return
     */
    public static int getResource(String resName, String name) {
        return resources.getIdentifier(name, resName, packageName);
    }
}
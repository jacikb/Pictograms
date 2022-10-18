package jacek.brzezinski.piktogramy;
/**
 * Audio from https://soundoftext.com/
 */

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private MediaPlayer mp;
    GridView simpleGrid;
    ArrayList<Integer> logos = new ArrayList<Integer>();
    ArrayList<Integer> audios = new ArrayList<Integer>();
    int gridSize = 140;
    private BatteryReceiver batteryReceiver = new BatteryReceiver();
    private IntentFilter intentBatteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.d(TAG, "onCreate");
        mp = MediaPlayer.create(MainActivity.this, R.raw.start);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (audios.size() == 0) {
            updateFromPreferences();
        }
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // simpleGrid.setNumColumns((int) (dpWidth / (120 + (gridSize * 40))));
        //1024 or 600
        simpleGrid.setNumColumns((int) (dpWidth / gridSize));
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), (int) (dpWidth / (100 + (gridSize * 40))), logos, audios);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                    }
                    mp = MediaPlayer.create(MainActivity.this, audios.get(position));
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
            case R.id.action_settings:
                openSettingsActivity();
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

    public void updateFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        logos.clear();
        audios.clear();
        if (prefs.getBoolean("p_tak", true)) {
            logos.add(R.drawable.tak);
            audios.add(R.raw.tak);
        }
        if (prefs.getBoolean("p_nie", true)) {
            logos.add(R.drawable.nie);
            audios.add(R.raw.nie);
        }
        if (prefs.getBoolean("p_koniec", true)) {
            logos.add(R.drawable.koniec);
            audios.add(R.raw.koniec);
        }
        if (prefs.getBoolean("p_toaleta", true)) {
            logos.add(R.drawable.toaleta);
            audios.add(R.raw.toaleta);
        }
        if (prefs.getBoolean("p_pic", true)) {
            logos.add(R.drawable.pic);
            audios.add(R.raw.pic);
        }
        if (prefs.getBoolean("p_jesc", true)) {
            logos.add(R.drawable.jesc);
            audios.add(R.raw.jesc);
        }
        if (prefs.getBoolean("p_kanapka", true)) {
            logos.add(R.drawable.kanapka);
            audios.add(R.raw.kanapka);
        }
        if (prefs.getBoolean("p_owoce", true)) {
            logos.add(R.drawable.owoce);
            audios.add(R.raw.owoce);
        }
        if (prefs.getBoolean("p_czekolada", true)) {
            logos.add(R.drawable.czekolada);
            audios.add(R.raw.czekolada);
        }
        if (prefs.getBoolean("p_jablko", true)) {
            logos.add(R.drawable.jablko);
            audios.add(R.raw.jablko);
        }
        if (prefs.getBoolean("p_komputer", true)) {
            logos.add(R.drawable.komputer);
            audios.add(R.raw.komputer);
        }
        if (prefs.getBoolean("p_jeszcze", true)) {
            logos.add(R.drawable.jeszcze);
            audios.add(R.raw.jeszcze);
        }
        if (prefs.getBoolean("p_kapac", true)) {
            logos.add(R.drawable.kapac);
            audios.add(R.raw.kapac);
        }
        if (prefs.getBoolean("p_odpoczac", true)) {
            logos.add(R.drawable.odpoczac);
            audios.add(R.raw.odpoczac);
        }
        if (prefs.getBoolean("p_spac", true)) {
            logos.add(R.drawable.spac);
            audios.add(R.raw.spac);
        }
        gridSize = Integer.parseInt(prefs.getString("grid_size", "140"));
        if (gridSize < 100 || gridSize > 180) {
            gridSize = 140;
        }
    }
}
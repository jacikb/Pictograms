package jacek.brzezinski.piktogramy;
/**
 * Audio from https://soundoftext.com/
 * example Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
 */

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.io.File;
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
    public int gridColumns = 8;
    ArrayList<Integer> logos = new ArrayList<Integer>();
    ArrayList<Integer> audios = new ArrayList<Integer>();
    List<PictogramModel> pictograms;
    DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        resources = this.getResources();
        packageName = this.getPackageName();
        databaseHelper = new DataBaseHelper(MainActivity.this);

        mp = MediaPlayer.create(MainActivity.this, R.raw.start);
        ShowPictograms(databaseHelper);

        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PictogramModel pictogramModel = pictograms.get(position);
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                    }
                    if (pictogramModel.isResource()) {
                        mp = MediaPlayer.create(MainActivity.this, getResourceAudio(pictogramModel));
                    } else {
                        File audioFile = (new File(getFilesDir() + "/audio/" + pictogramModel.getPath() + ".mp3"));
                        mp = MediaPlayer.create(MainActivity.this, Uri.fromFile(audioFile));
                    }
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        int columns;
        updateFromPreferences();
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns = gridColumns;
        } else {
            columns = (int) (gridColumns / 1.4);
        }
        int gridSize = (int) (displayMetrics.widthPixels / columns);
        simpleGrid.setNumColumns(columns);
        pictograms = databaseHelper.getAll(true);

        // Create an object of CustomAdapter and set Adapter to GirdView
        PictogramListAdapter pictogramListAdapter = new PictogramListAdapter(MainActivity.this, gridSize, pictograms);
        simpleGrid.setAdapter(pictogramListAdapter);

    }

    public void updateFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        gridColumns = Integer.parseInt(prefs.getString("grid_columns", "6"));
        if (gridColumns < 3 || gridColumns > 8) {
            gridColumns = 6;
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

    /**
     * @param pictogramModel
     * @return
     */
    public static int getResourceImage(PictogramModel pictogramModel) {
        return resources.getIdentifier(pictogramModel.getPath(), "drawable", packageName);
    }

    /**
     * @param pictogramModel
     * @return
     */
    public static int getResourceAudio(PictogramModel pictogramModel) {
        return resources.getIdentifier(pictogramModel.getPath(), "raw", packageName);
    }
}
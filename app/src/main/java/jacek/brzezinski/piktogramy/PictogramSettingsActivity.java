package jacek.brzezinski.piktogramy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class PictogramSettingsActivity extends AppCompatActivity {
    private static final String TAG = "PictogramSettingsActivity";
    ListView pictogramListView;
    List<PictogramModel> pictograms;
    ArrayAdapter customerArrayAdapter;
    DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictogram_list);
        databaseHelper = new DataBaseHelper(super.getBaseContext());
        pictogramListView = (ListView) findViewById(R.id.pictogramListView); // init GridView
        pictograms = databaseHelper.getAll(false);

        PictogramSettingsAdapter customAdapter = new PictogramSettingsAdapter(PictogramSettingsActivity.this, databaseHelper, pictograms);
        pictogramListView.setAdapter(customAdapter);
    }
}
package jacek.brzezinski.piktogramy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PictogramSettingsActivity extends AppCompatActivity {
    private static final String TAG = "PictogramSettingsActivity";
    ListView pictogramListView;
    List<PictogramModel> pictograms;
    PictogramSettingsAdapter customAdapter;
    DataBaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pictogram_list);
        databaseHelper = new DataBaseHelper(super.getBaseContext());
        pictogramListView = (ListView) findViewById(R.id.pictogramListView); // init GridView
        pictograms = databaseHelper.getAll(false);
        customAdapter = new PictogramSettingsAdapter(PictogramSettingsActivity.this, databaseHelper, pictograms);
        pictogramListView.setAdapter(customAdapter);
    }

    public void actionMoveUp(View view) {
        Log.w("ACTION", "---------------------------------------------");
        Log.w("ACTION", "UP" + view.getTag().toString());
        int id = Integer.parseInt(view.getTag().toString());

        for (int i = 0; i < pictograms.size(); i++) {
            PictogramModel pictogram = pictograms.get(i);
            if (pictogram.getId() == id && i > 0) {
                changePosition(pictogram, pictograms.get(i - 1));
            }
        }
    }

    public void actionMoveDown(View view) {
        Log.w("ACTION", "---------------------------------------------");
        Log.w("ACTION", "DOWN " + view.getTag().toString());
        int id = Integer.parseInt(view.getTag().toString());
        for (int i = 0; i < pictograms.size(); i++) {
            PictogramModel pictogram = pictograms.get(i);
            if (pictogram.getId() == id && i < pictograms.size() - 1) {
                changePosition(pictogram, pictograms.get(i + 1));
            }
        }
    }

    public void actionEdit(View view) {
        Log.w("EDIT ", view.getTag().toString());
        Intent intent = new Intent(PictogramSettingsActivity.this, PictogramAddActivity.class);
        intent.putExtra("editId", Integer.parseInt(view.getTag().toString()));
        startActivity(intent);
    }


    public void actionAdd(View view) {
        Intent intent = new Intent(PictogramSettingsActivity.this, PictogramAddActivity.class);
        startActivity(intent);
    }

    private void changePosition(PictogramModel pictogramA, PictogramModel pictogramB) {
        int position;
        position = pictogramA.getPosition();
        pictogramA.setPosition(pictogramB.getPosition());
        pictogramB.setPosition(position);
        databaseHelper.updateOne(pictogramA);
        databaseHelper.updateOne(pictogramB);
        recreate();
    }
}
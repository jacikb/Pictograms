package jacek.brzezinski.piktogramy;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PictogramSettingsAdapter extends BaseAdapter {
    private static final String TAG = "CustomAdapter";
    DataBaseHelper dataBaseHelper;
    Context context;
    List<PictogramModel> pictograms;
    LayoutInflater inflter;

    public PictogramSettingsAdapter(Context applicationContext, DataBaseHelper dataBaseHelper, List<PictogramModel> pictograms) {
        this.context = applicationContext;
        this.pictograms = pictograms;
        this.inflter = (LayoutInflater.from(applicationContext));
        this.dataBaseHelper = dataBaseHelper;
    }

    @Override
    public int getCount() {
        return pictograms.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PictogramModel pictogramModel = pictograms.get(i);
        view = inflter.inflate(R.layout.pictogram_list_item, null); // inflate the layout
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView text = (TextView) view.findViewById(R.id.pictogramListName);
        Switch active = (Switch) view.findViewById(R.id.pictogramListActive);

        icon.setImageResource(MainActivity.getResource("drawable", pictogramModel.getPath()));
        text.setText(pictogramModel.getPath());
        active.setChecked(pictogramModel.isActive());
        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pictogramModel.setActive(isChecked);
                dataBaseHelper.updateOne(pictogramModel);
                // do something, the isChecked will be
                // true if the switch is in the On position
                //Toast.makeText(context, pictogramModel.getPath() + " " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
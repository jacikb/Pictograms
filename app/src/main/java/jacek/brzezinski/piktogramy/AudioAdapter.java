package jacek.brzezinski.piktogramy;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AudioAdapter extends BaseAdapter {
    private static final String TAG = "AudioAdapter";
    Context context;
    List<AudioModel> audios;
    LayoutInflater inflter;

    public AudioAdapter(Context applicationContext, List<AudioModel> audios) {
        this.context = applicationContext;
        this.audios = audios;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return audios.size();
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
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout

        ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView

        AudioModel audioModel = audios.get(i);
//        icon.setImageResource(MainActivity.getResource("drawable", pictogramModel.getPath()));
//        icon.getLayoutParams().height = gridHeight;
//        icon.getLayoutParams().width = gridHeight;
//        icon.requestLayout();

        return view;
    }
}
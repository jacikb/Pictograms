package jacek.brzezinski.piktogramy;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int gridHeight;
    ArrayList<Integer> logos;
    ArrayList<Integer> audios;
    MediaPlayer mediaPlayer;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int gridHeight, ArrayList<Integer> logos, ArrayList<Integer> audios) {
        this.context = applicationContext;
        this.logos = new ArrayList<Integer>(logos);
        this.audios = new ArrayList<Integer>(audios);

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return logos.size();
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
        icon.getLayoutParams().height =gridHeight;
        icon.setImageResource(logos.get(i)); // set logo images
        return view;
    }
}
package jacek.brzezinski.piktogramy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class PictogramListAdapter extends BaseAdapter {
    private static final String TAG = "CustomAdapter";
    Context context;
    int gridHeight;
    String imageDir;
    List<PictogramModel> pictograms;
    LayoutInflater inflter;

    public PictogramListAdapter(Context applicationContext, int gridHeight, List<PictogramModel> pictograms) {
        this.context = applicationContext;
        this.imageDir = context.getFilesDir() + "/image/";
        this.pictograms = pictograms;
        this.gridHeight = gridHeight;
        inflter = (LayoutInflater.from(applicationContext));
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
        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout

        ImageView icon = (ImageView) view.findViewById(R.id.pictogramListIcon); // get the reference of ImageView

        PictogramModel pictogramModel = pictograms.get(i);
        try {
            if (pictogramModel.isResource()) {
                icon.setImageResource(MainActivity.getResourceImage(pictogramModel));
            } else {
                String fileName = imageDir + pictogramModel.getPath() + ".jpg";
                Bitmap imageBitmap;
                imageBitmap = BitmapFactory.decodeFile(fileName);
                icon.setImageBitmap(imageBitmap);
            }
        } catch (Exception e) {
            icon.setImageResource(R.drawable.no_foto);
        }
        icon.getLayoutParams().height = gridHeight;
        icon.getLayoutParams().width = gridHeight;
        icon.requestLayout();


        return view;
    }
}
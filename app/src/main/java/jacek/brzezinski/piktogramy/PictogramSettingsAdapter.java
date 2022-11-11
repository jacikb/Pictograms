package jacek.brzezinski.piktogramy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.util.List;

public class PictogramSettingsAdapter extends BaseAdapter {
    private static final String TAG = "CustomAdapter";
    DataBaseHelper dataBaseHelper;
    Context context;
    String imageDir;
    List<PictogramModel> pictograms;
    LayoutInflater inflter;

    public PictogramSettingsAdapter(Context applicationContext, DataBaseHelper dataBaseHelper, List<PictogramModel> pictograms) {
        this.context = applicationContext;
        this.imageDir = context.getFilesDir() + "/image/";
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

    @SuppressLint({"UseSwitchCompatOrMaterialCode", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PictogramModel pictogramModel = pictograms.get(i);
        view = inflter.inflate(R.layout.pictogram_list_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.pictogramListIcon);
        //EditText e_name = (EditText) view.findViewById(R.id.pictogramListName);
        Switch e_active = (Switch) view.findViewById(R.id.pictogramListActive);
        ImageButton bt_up = (ImageButton) view.findViewById(R.id.pictogramListUp);
        ImageButton bt_down = (ImageButton) view.findViewById(R.id.pictogramListDown);
        ImageButton bt_edit = (ImageButton) view.findViewById(R.id.pictogramListEdit);

        bt_up.setVisibility(i > 0 ? View.VISIBLE : View.INVISIBLE);
        bt_down.setVisibility(i < getCount() - 1 ? View.VISIBLE : View.INVISIBLE);

        bt_down.setEnabled(i < getCount() - 1);
        bt_up.setTag("" + pictogramModel.getId());
        bt_down.setTag("" + pictogramModel.getId());
        bt_edit.setTag("" + pictogramModel.getId());
        bt_edit.setEnabled(!pictogramModel.isResource());
        bt_edit.setVisibility(pictogramModel.isResource() ? View.INVISIBLE : View.VISIBLE);

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

        //e_name.setText(pictogramModel.getName());
        e_active.setChecked(pictogramModel.isActive());
        e_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pictogramModel.setActive(isChecked);
                dataBaseHelper.updateOne(pictogramModel);

            }
        });
//        e_name.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//
//
//            }
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                pictogramModel.setName(s.toString());
//                dataBaseHelper.updateOne(pictogramModel);
//            }
//        });

//        bt_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.w("EDIT ", pictogramModel.getName());
//                        Intent intent = new Intent(PictogramSettingsActivity.this, PictogramAddActivity.class);
//                        intent.putExtra("editId",312);
//                        startActivity(intent);
//                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
//            }
//        });
        return view;
    }
}
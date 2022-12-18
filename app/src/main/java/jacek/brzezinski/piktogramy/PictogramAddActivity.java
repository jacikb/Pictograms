package jacek.brzezinski.piktogramy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PictogramAddActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_FILE = 10;
    public static final int PICK_AUDIO_FILE = 20;

    DataBaseHelper databaseHelper;

    private ImageView imageView;
    private Spinner listParent;
    List<PictogramModel> treeParent;
    private EditText editName;
    private EditText editPath;
    private EditText editPosition;
    private FloatingActionButton actionButtonPlay;
    private int editId = 0;
    private Uri audioUri;
    private Uri imageUri;
    private Bitmap imageBitmap;
    private Boolean audioChanged = false;
    private Boolean imageChanged = false;

//    List<StringWithTag> treeParent = new ArrayList<StringWithTag>();

    private PictogramModel pictogramModel;

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram_add);
        Intent data = getIntent();
        editId = data.getIntExtra("editId", 0);
        Toast.makeText(PictogramAddActivity.this, "onCreate", Toast.LENGTH_LONG).show();
        databaseHelper = new DataBaseHelper(PictogramAddActivity.this);
        imageView = (ImageView) findViewById(R.id.pictogramAddIcon);
        listParent = (Spinner) findViewById(R.id.pictogramAddParent);
        editName = (EditText) findViewById(R.id.pictogramAddName);
        editPath = (EditText) findViewById(R.id.pictogramAddPath);
        editPosition = (EditText) findViewById(R.id.pictogramAddPosition);
        actionButtonPlay = (FloatingActionButton) findViewById(R.id.ActionButtonPlay);
        PictogramModel pictogramRoot = new PictogramModel(0, 0, 0, "Katalog główny", "", false, 0, true);
        audioChanged = false;
        imageChanged = false;
        treeParent = new ArrayList<PictogramModel>();
        treeParent.add(pictogramRoot);
        treeParent.addAll(databaseHelper.getTree(0));
        ArrayAdapter<PictogramModel> treeParentAdapter = new ArrayAdapter<PictogramModel>(this, android.R.layout.simple_spinner_item, treeParent);
        listParent.setAdapter(treeParentAdapter);

        if (editId > 0) {
            pictogramModel = databaseHelper.getById(editId);
            editName.setText(pictogramModel.getName());
            editPath.setText(pictogramModel.getPath());
            int position = 0;
            int parentId = pictogramModel.getParent();
            for (int i = 0; i < treeParent.size(); i++) {
                if (treeParent.get(i).getId() == parentId) {
                    position = i;
                }
            }
            listParent.setSelection(position);
            //listParent.setSelection(treeParentAdapter.getPosition(pictogramModel));

            imageUri = Uri.fromFile(new File(getFilesDir() + "/image/" + pictogramModel.getPath() + ".jpg"));
            audioUri = Uri.fromFile(new File(getFilesDir() + "/audio/" + pictogramModel.getPath() + ".mp3"));
            try {
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                imageBitmap = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
            actionButtonPlay.setEnabled(true);
        } else {
            pictogramModel = new PictogramModel();
            pictogramModel.setPosition(databaseHelper.getMaxPosition() + 1);
            pictogramModel.setResource(false);
            pictogramModel.setActive(true);
        }
        editPosition.setText(Integer.toString(pictogramModel.getPosition()));
        editPosition.setText(Integer.toString(editId));
    }


//    public void onItemSelected(AdapterView<?> parant, View v, int pos, long id) {
//        StringWithTag s = (StringWithTag) parant.getItemAtPosition(pos);
//        Object tag = s.tag;
//    }

    public void actionPhotoFromFile(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE_FILE);
    }

    public void actionAudioFromFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*"); // specify "audio/mp3" to filter only mp3 files
        startActivityForResult(intent, PICK_AUDIO_FILE);
    }

    public void actionAudioPlay(View view) {
        mp = MediaPlayer.create(PictogramAddActivity.this, audioUri);
        mp.start();
    }

    public void actionSave(View view) {

        String imageFileName;
        String audioFileName;
        PictogramModel parent = (PictogramModel) listParent.getSelectedItem();
        pictogramModel.setParent(parent.getId());
        pictogramModel.setName(editName.getText().toString());
        pictogramModel.setPath(editPath.getText().toString());
        pictogramModel.setPosition(Integer.parseInt(editPosition.getText().toString()));

        if (pictogramModel.getPath().length() == 0) {
            Toast.makeText(PictogramAddActivity.this, "Kod nie może być pusty!", Toast.LENGTH_LONG).show();
            return;
        }

        //Image
        if (imageChanged) {
            try {
                imageFileName = pictogramModel.getPath() + ".jpg";
                copyFile(imageUri, getFilesDir() + "/image/", imageFileName);
//            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(PictogramAddActivity.this, "error " + e.toString(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PictogramAddActivity.this, "Something went wrong 2 " + audioUri.getPath(), Toast.LENGTH_LONG).show();
            }
        }

        //Audio
        if (audioChanged) {
            try {
                audioFileName = pictogramModel.getPath() + ".mp3";
                copyFile(audioUri, getFilesDir() + "/audio/", audioFileName);
//            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(PictogramAddActivity.this, "error " + e.toString(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(PictogramAddActivity.this, "Something went wrong 2 " + audioUri.getPath(), Toast.LENGTH_LONG).show();
            }
        }
        if (pictogramModel.getId() > 0) {
            databaseHelper.updateOne(pictogramModel);
            Toast.makeText(PictogramAddActivity.this, "Zaktualizowano piktogram " + pictogramModel.getName(), Toast.LENGTH_LONG).show();
        } else {
            databaseHelper.addOne(pictogramModel);
            Toast.makeText(PictogramAddActivity.this, "Dodano nowy piktogram " + pictogramModel.getName(), Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public boolean saveFileTxt(String fileName, String mytext) {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(mytext);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_FILE) {
                try {
                    imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    imageBitmap = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(imageBitmap);
                    Toast.makeText(this, "RESULT FILE " + Integer.toString(resultCode), Toast.LENGTH_SHORT).show();
                    imageChanged = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == PICK_AUDIO_FILE) {
//                final Uri audioUri = ;
                audioUri = data.getData();
                actionButtonPlay.setEnabled(true);
                audioChanged = true;
            }
        }
    }

    /**
     * @param sourceUri
     * @param targetDir
     * @param targetFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void copyFile(Uri sourceUri, String targetDir, String targetFile)
            throws FileNotFoundException, IOException {
        try {
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            Toast.makeText(PictogramAddActivity.this, "creating file error " + e.toString(), Toast.LENGTH_LONG).show();
            Log.w("creating file error ", e.toString());
        }
        try {
            File file = new File(targetDir + targetFile);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            Toast.makeText(PictogramAddActivity.this, "deleting file error " + e.toString(), Toast.LENGTH_LONG).show();
            Log.w("deleting file error ", e.toString());
        }

        final InputStream in = getContentResolver().openInputStream(sourceUri);
        OutputStream out = new FileOutputStream(targetDir + targetFile);

        // Copy the bits from instream to outstream
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
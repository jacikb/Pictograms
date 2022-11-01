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
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Locale;

public class PictogramAddActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_FILE = 10;
    public static final int PICK_AUDIO_FILE = 20;

    DataBaseHelper databaseHelper;

    private ImageView imageView;
    private EditText editName;
    private EditText editPath;
    private FloatingActionButton actionButtonPlay;

    private Uri audioUri;
    private Uri imageUri;
    private Bitmap imageBitmap;
    private PictogramModel pictogramModel = new PictogramModel();

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram_add);

        databaseHelper = new DataBaseHelper(PictogramAddActivity.this);
        imageView = (ImageView) findViewById(R.id.pictogramAddIcon);
        editName = (EditText) findViewById(R.id.pictogramAddName);
        editPath = (EditText) findViewById(R.id.pictogramAddPath);
        actionButtonPlay = (FloatingActionButton) findViewById(R.id.ActionButtonPlay);
        editName.setText(pictogramModel.getName());
        editPath.setText(pictogramModel.getPath());


    }

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

        pictogramModel.setName(editName.getText().toString());
        pictogramModel.setPath(editPath.getText().toString());
        pictogramModel.setResource(false);
        pictogramModel.setPosition(20);
        pictogramModel.setActive(true);

        if (pictogramModel.getPath().length() == 0) {
            Toast.makeText(PictogramAddActivity.this, "Kod nie może być pusty!", Toast.LENGTH_LONG).show();
            return;
        }

        //Image
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

        //Audio
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
        databaseHelper.addOne(pictogramModel);
        Toast.makeText(PictogramAddActivity.this, "Dodano nowy piktogram", Toast.LENGTH_LONG).show();
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == PICK_AUDIO_FILE) {
//                final Uri audioUri = ;
                audioUri = data.getData();
                actionButtonPlay.setEnabled(true);
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
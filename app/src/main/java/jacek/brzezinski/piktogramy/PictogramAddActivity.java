package jacek.brzezinski.piktogramy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PictogramAddActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_CAMERA = 5;
    public static final int PICK_IMAGE_FILE = 10;
    public static final int PICK_AUDIO_FILE = 20;

    private ImageView imageView;
    private FloatingActionButton actionButtonPlay;

    private Uri audioFile;
    private Bitmap imageFile;
    private PictogramModel pictogramModel = new PictogramModel();

    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram_add);
        imageView = (ImageView) findViewById(R.id.pictogramAddIcon);
        actionButtonPlay = (FloatingActionButton) findViewById(R.id.ActionButtonPlay);
        pictogramModel.setPath("p_test");
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
        mp = MediaPlayer.create(PictogramAddActivity.this, audioFile);
        mp.start();

    }

    public void actionSave(View view) {
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    // Call Back method  to get the Message form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_CAMERA) {
//            https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery/
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    Toast.makeText(this, "RESULT CAMERA " + Integer.toString(resultCode), Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == PICK_IMAGE_FILE) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    imageFile = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(imageFile);
                    Toast.makeText(this, "RESULT FILE " + Integer.toString(resultCode), Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            if (requestCode == PICK_AUDIO_FILE) {
//                try {
                final Uri audioUri = data.getData();
                audioFile = audioUri;
                actionButtonPlay.setEnabled(true);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                }
            }

        }
    }
}
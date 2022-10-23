package jacek.brzezinski.piktogramy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class PictogramAddActivity extends AppCompatActivity {
    public static final int PICK_IMAGE_FILE = 10;
    public static final int PICK_IMAGE_CAMERA = 20;
    private ImageView imageView;
    private File audioFile;
    private PictogramModel pictogramModel = new PictogramModel();

    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictogram_add);
        imageView = (ImageView) findViewById(R.id.pictogramAddIcon);
        pictogramModel.setPath("p_test");

    }

    public void actionPhotoFromCamera(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CAMERA);
//        File file = new File(Environment.getExternalStorageDirectory(),
//                "p_" + UUID.randomUUID() + ".jpg");
        File file = PictogramAddActivity.this.getExternalFilesDir(UUID.randomUUID() + ".jpg");
        Toast.makeText(this, "file " + file, Toast.LENGTH_SHORT).show();

        outputFileUri = FileProvider.getUriForFile(PictogramAddActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        //takePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //takePhotoIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/tmp")));


        //Intent chooseImageIntent = ImagePicker.getPickImageIntent(PictogramAddActivity.this);
        startActivityForResult(takePhotoIntent, PICK_IMAGE_CAMERA);
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
//        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        getIntent.setType("image/*");
//        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setType("image/*");
//        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
//        startActivityForResult(chooserIntent, PICK_IMAGE_FILE);
    }

    public void actionAudioPlay(View view) {

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
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setImageBitmap(selectedImage);
                    Toast.makeText(this, "RESULT FILE " + Integer.toString(resultCode), Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(PictogramAddActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
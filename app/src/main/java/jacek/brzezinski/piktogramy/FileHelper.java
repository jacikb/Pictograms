package jacek.brzezinski.piktogramy;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {
    private Context context;
    private static Resources resources;
    private static String packageName;

    public FileHelper(Context applicationContext) {
        context = applicationContext;
        resources = context.getResources();
        packageName = context.getPackageName();
    }

    public String getDefaultImageDir() {
        return this.context.getFilesDir() + "/image/";
    }

    public String getDefaultImageFilePath(String path) {
        return getDefaultImageDir() + path.toLowerCase() + ".jpg";
    }

    public String getDefaultImageFileName(String path) {
        return path.toLowerCase() + ".jpg";
    }

    public String getDefaultAudioDir() {
        return this.context.getFilesDir() + "/audio/";
    }

    public String getDefaultAudioFilePath(String path) {
        return getDefaultAudioDir() + getDefaultAudioFileName(path);
    }

    public String getDefaultAudioFileName(String path) {
        return path + ".mp3";
    }

    /**
     *
     * @param code
     * @throws IOException
     */
    public void copyResourceToLocal(String code) throws IOException {

        int rImage = resources.getIdentifier(code, "drawable", packageName);
        int rAudio = resources.getIdentifier(code, "raw", packageName);
        Uri uriImage = Uri.parse("android.resource://" + packageName + "/" + rImage);
        Uri uriAudio = Uri.parse("android.resource://" + packageName + "/" + rAudio);

        copyUriFile(uriImage, getDefaultImageDir(), getDefaultImageFileName(code));
        copyUriFile(uriAudio, getDefaultAudioDir(), getDefaultAudioFileName(code));
    }

    /**
     * @param sourceUri
     * @param targetDir
     * @param targetFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void copyUriFile(Uri sourceUri, String targetDir, String targetFile) throws IOException {
        try {
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } catch (Exception e) {
            //Toast.makeText(PictogramAddActivity.this, "creating file error " + e.toString(), Toast.LENGTH_LONG).show();
            Log.w("creating file error ", e.toString());
        }
        try {
            File file = new File(targetDir + targetFile);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            //Toast.makeText(PictogramAddActivity.this, "deleting file error " + e.toString(), Toast.LENGTH_LONG).show();
            Log.w("deleting file error ", e.toString());
        }

        final InputStream in = this.context.getContentResolver().openInputStream(sourceUri);
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

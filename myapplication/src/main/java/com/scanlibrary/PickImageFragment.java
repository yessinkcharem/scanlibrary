//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.scanlibrary.R.id;
import com.scanlibrary.R.layout;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PickImageFragment extends Fragment {
    int camorgal = 0;
    private String imagePath = "";
    private View view;
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    private Uri fileUri;
    private IScanner scanner;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    public PickImageFragment() {
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof IScanner)) {
            throw new ClassCastException("Activity must implement IScanner");
        } else {
            this.scanner = (IScanner)activity;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(layout.pick_image_fragment, (ViewGroup)null);
        this.init();
        return this.view;
    }

    private void init() {
        this.cameraButton = (ImageButton)this.view.findViewById(id.cameraButton);
        this.cameraButton.setOnClickListener(new CameraButtonClickListener());
        this.galleryButton = (ImageButton)this.view.findViewById(id.selectButton);
        this.galleryButton.setOnClickListener(new GalleryClickListener());
        this.imagePath = this.getActivity().getApplicationContext().getExternalCacheDir().getPath() + "/scanSample";
        if (this.isIntentPreferenceSet()) {
            this.handleIntentPreference();
        } else {
            this.getActivity().finish();
        }

    }

    private void clearTempImages() {
        try {
            File tempFolder = new File(this.imagePath);
            File[] var2 = tempFolder.listFiles();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                File f = var2[var4];
                f.delete();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    private void handleIntentPreference() {
        int preference = this.getIntentPreference();
        if (preference == 4) {
            this.openCamera();
        } else if (preference == 5) {
            this.openMediaContent();
        }

    }

    private boolean isIntentPreferenceSet() {
        int preference = this.getArguments().getInt("selectContent", 0);
        return preference != 0;
    }

    private int getIntentPreference() {
        int preference = this.getArguments().getInt("selectContent", 0);
        return preference;
    }

    public void openMediaContent() {
        this.camorgal = 1;
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/*");
        this.startActivityForResult(intent, 1);
    }

    public void openCamera() {
        this.camorgal = 0;
        if (ContextCompat.checkSelfPermission(this.getActivity(), "android.permission.CAMERA") == 0) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            File file = this.createImageFile();
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            Log.d("", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
            if (VERSION.SDK_INT >= 24) {
                String aut = "com.scanlibrary.provider";
                Uri tempFileUri = FileProvider.getUriForFile(this.getActivity().getApplicationContext(), aut, file);
                cameraIntent.putExtra("output", tempFileUri);
            } else {
                Uri tempFileUri = Uri.fromFile(file);
                cameraIntent.putExtra("output", tempFileUri);
            }

            this.startActivityForResult(cameraIntent, 2);
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{"android.permission.CAMERA"}, 100);
        }

    }

    private File createImageFile() {
        this.clearTempImages();
        String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
        File file = new File(this.imagePath, "IMG_" + timeStamp + ".jpg");
        this.fileUri = Uri.fromFile(file);
        return file;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("", "onActivityResult" + resultCode);
        Bitmap bitmap = null;
        if (resultCode == -1) {
            try {
                switch(requestCode) {
                    case 1:
                        bitmap = this.getBitmap(data.getData());
                        break;
                    case 2:
                        bitmap = this.getBitmap(this.fileUri);
                }
            } catch (Exception var11) {
                var11.printStackTrace();
            }
        } else {
            this.getActivity().finish();
        }

        if (bitmap != null) {
            if (this.camorgal == 0) {
                File imageFile = new File(this.fileUri.getPath());
                ExifInterface exif = null;

                try {
                    exif = new ExifInterface(imageFile.getAbsolutePath());
                } catch (IOException var10) {
                    var10.printStackTrace();
                }

                int orientation = 0;
                if (exif != null) {
                    orientation = exif.getAttributeInt("Orientation", 1);
                }

                int rotate = 0;
                switch(orientation) {
                    case 3:
                        rotate = 180;
                        break;
                    case 6:
                        rotate = 90;
                        break;
                    case 8:
                        rotate = 270;
                }

                Matrix matrix = new Matrix();
                matrix.postRotate((float)rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }

            this.postImagePick(bitmap);
        }

    }

    protected void postImagePick(Bitmap bitmap) {
        Uri uri = Utils.getUri(this.getActivity(), bitmap);
        bitmap.recycle();
        this.scanner.onBitmapSelect(uri);
    }

    private Bitmap getBitmap(Uri selectedimg) throws IOException {
        Options options = new Options();

        try {
            int quality = this.getArguments().getInt("quality", 1);
            options.inSampleSize = quality;
        } catch (Exception var5) {
            options.inSampleSize = 1;
        }

        AssetFileDescriptor fileDescriptor = null;
        fileDescriptor = this.getActivity().getContentResolver().openAssetFileDescriptor(selectedimg, "r");
        Bitmap original = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), (Rect)null, options);
        return original;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == 0) {
                this.openCamera();
            } else {
                Toast.makeText(this.getActivity(), "camera permission denied", 1).show();
            }
        }

    }

    private class GalleryClickListener implements OnClickListener {
        private GalleryClickListener() {
        }

        public void onClick(View view) {
            PickImageFragment.this.openMediaContent();
        }
    }

    private class CameraButtonClickListener implements OnClickListener {
        private CameraButtonClickListener() {
        }

        public void onClick(View v) {
            PickImageFragment.this.openCamera();
        }
    }
}

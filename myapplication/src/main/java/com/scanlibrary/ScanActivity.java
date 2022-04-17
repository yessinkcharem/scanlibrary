package com.scanlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentCallbacks2;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.scanlibrary.R.id;
import com.scanlibrary.R.layout;
import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends Activity implements IScanner, ComponentCallbacks2 {
    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"};

    public ScanActivity() {
    }

    public static  String testing(){
        return "testing this shit" ;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.scan_layout);
        if (this.getActionBar() != null) {
            this.getActionBar().hide();
        }

        this.checkPermissions();
    }

    private boolean checkPermissions() {
        List<String> listPermissionsNeeded = new ArrayList();
        String[] var2 = this.permissions;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String p = var2[var4];
            int result = ContextCompat.checkSelfPermission(this, p);
            if (result != 0) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, (String[])listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        } else {
            this.init();
            return true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == 0) {
            this.init();
        }

    }

    private void init() {
        PickImageFragment fragment = new PickImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("selectContent", this.getPreferenceContent());
        bundle.putInt("quality", this.getIntent().getIntExtra("quality", 1));
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id.content, fragment);
        fragmentTransaction.commit();
    }

    protected int getPreferenceContent() {
        return this.getIntent().getIntExtra("selectContent", 0);
    }

    public void onBitmapSelect(Uri uri) {
        ScanFragment fragment = new ScanFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedBitmap", uri);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id.content, fragment);
        fragmentTransaction.addToBackStack(ScanFragment.class.toString());
        fragmentTransaction.commit();
    }

    public void onScanFinish(Uri uri) {
        System.out.println("************** onScanFinish ");
        ResultFragment fragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("scannedResult", uri);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id.content, fragment);
        fragmentTransaction.addToBackStack(ResultFragment.class.toString());
        fragmentTransaction.commit();
    }

    public void onTrimMemory(int level) {
        switch(level) {
            case 5:
            case 10:
            case 15:
            case 20:
            case 40:
            case 60:
            case 80:
            default:
        }
    }

    public native Bitmap getScannedBitmap(Bitmap var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9);

    public native Bitmap getGrayBitmap(Bitmap var1);

    public native Bitmap getMagicColorBitmap(Bitmap var1);

    public native Bitmap getBWBitmap(Bitmap var1);

    public native float[] getPoints(Bitmap var1);

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("Scanner");
    }
}

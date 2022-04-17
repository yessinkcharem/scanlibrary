//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class Utils {
  static Date currentTime;

  private Utils() {
  }

  public static Uri getUri(Context context, Bitmap bitmap) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bitmap.compress(CompressFormat.JPEG, 100, bytes);
    Log.wtf("PATH", "before insertImage");
    String path = Media.insertImage(context.getContentResolver(), bitmap, "Title - " + (currentTime = Calendar.getInstance().getTime()), (String)null);
    Log.wtf("PATH", path);
    return Uri.parse(path);
  }

  public static Bitmap getBitmap(Context context, Uri uri) throws IOException {
    return Media.getBitmap(context.getContentResolver(), uri);
  }
}

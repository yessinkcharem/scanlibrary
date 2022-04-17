//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.FrameLayout.LayoutParams;
import com.scanlibrary.R.dimen;
import com.scanlibrary.R.id;
import com.scanlibrary.R.layout;
import com.scanlibrary.R.string;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScanFragment extends Fragment {
  private Button scanButton;
  private ImageView sourceImageView;
  private FrameLayout sourceFrame;
  private PolygonView polygonView;
  private View view;
  private ProgressDialogFragment progressDialogFragment;
  private IScanner scanner;
  private Bitmap original;

  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (!(activity instanceof IScanner)) {
      throw new ClassCastException("Activity must implement IScanner");
    } else {
      this.scanner = (IScanner)activity;
    }
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.view = inflater.inflate(layout.scan_fragment_layout, (ViewGroup)null);
    this.init();
    return this.view;
  }

  public ScanFragment() {
  }

  private void init() {
    this.sourceImageView = (ImageView)this.view.findViewById(id.sourceImageView);
    this.scanButton = (Button)this.view.findViewById(id.scanButton);
    if (this.getActivity().getIntent().getStringExtra("scanNextText") != null) {
      this.scanButton.setText(this.getActivity().getIntent().getStringExtra("scanNextText"));
    }

    this.scanButton.setOnClickListener(new ScanButtonClickListener());
    this.sourceFrame = (FrameLayout)this.view.findViewById(id.sourceFrame);
    this.polygonView = (PolygonView)this.view.findViewById(id.polygonView);
    this.sourceFrame.post(new Runnable() {
      public void run() {
        ScanFragment.this.original = ScanFragment.this.getBitmap();
        if (ScanFragment.this.original != null) {
          ScanFragment.this.setBitmap(ScanFragment.this.original);
        }

      }
    });
  }

  private Bitmap getBitmap() {
    Uri uri = this.getUri();

    try {
      Bitmap bitmap = Utils.getBitmap(this.getActivity(), uri);
      this.getActivity().getContentResolver().delete(uri, (String)null, (String[])null);
      return bitmap;
    } catch (IOException var3) {
      var3.printStackTrace();
      return null;
    }
  }

  private Uri getUri() {
    Uri uri = (Uri)this.getArguments().getParcelable("selectedBitmap");
    return uri;
  }

  private void setBitmap(Bitmap original) {
    Bitmap scaledBitmap = this.scaledBitmap(original, this.sourceFrame.getWidth(), this.sourceFrame.getHeight());
    this.sourceImageView.setImageBitmap(scaledBitmap);
    Bitmap tempBitmap = ((BitmapDrawable)this.sourceImageView.getDrawable()).getBitmap();
    Map<Integer, PointF> pointFs = new HashMap();
    pointFs.put(0, new PointF(0.0F, 0.0F));
    pointFs.put(1, new PointF((float)scaledBitmap.getWidth(), 0.0F));
    pointFs.put(2, new PointF(0.0F, (float)scaledBitmap.getHeight()));
    pointFs.put(3, new PointF((float)scaledBitmap.getWidth(), (float)scaledBitmap.getHeight()));
    this.polygonView.setPoints(pointFs);
    this.polygonView.setVisibility(0);
    int padding = (int)this.getResources().getDimension(dimen.scanPadding);
    LayoutParams layoutParams = new LayoutParams(tempBitmap.getWidth() + 2 * padding, tempBitmap.getHeight() + 2 * padding);
    layoutParams.gravity = 17;
    this.polygonView.setLayoutParams(layoutParams);
  }

  private Map<Integer, PointF> getEdgePoints(Bitmap tempBitmap) {
    List<PointF> pointFs = this.getContourEdgePoints(tempBitmap);
    Map<Integer, PointF> orderedPoints = this.orderedValidEdgePoints(tempBitmap, pointFs);
    return orderedPoints;
  }

  private List<PointF> getContourEdgePoints(Bitmap tempBitmap) {
    float[] points = ((ScanActivity)this.getActivity()).getPoints(tempBitmap);
    float x1 = points[0];
    float x2 = points[1];
    float x3 = points[2];
    float x4 = points[3];
    float y1 = points[4];
    float y2 = points[5];
    float y3 = points[6];
    float y4 = points[7];
    List<PointF> pointFs = new ArrayList();
    pointFs.add(new PointF(x1, y1));
    pointFs.add(new PointF(x2, y2));
    pointFs.add(new PointF(x3, y3));
    pointFs.add(new PointF(x4, y4));
    return pointFs;
  }

  private Map<Integer, PointF> getOutlinePoints(Bitmap tempBitmap) {
    Map<Integer, PointF> outlinePoints = new HashMap();
    outlinePoints.put(0, new PointF(0.0F, 0.0F));
    outlinePoints.put(1, new PointF((float)tempBitmap.getWidth(), 0.0F));
    outlinePoints.put(2, new PointF(0.0F, (float)tempBitmap.getHeight()));
    outlinePoints.put(3, new PointF((float)tempBitmap.getWidth(), (float)tempBitmap.getHeight()));
    return outlinePoints;
  }

  private Map<Integer, PointF> orderedValidEdgePoints(Bitmap tempBitmap, List<PointF> pointFs) {
    Map<Integer, PointF> orderedPoints = this.polygonView.getOrderedPoints(pointFs);
    if (!this.polygonView.isValidShape(orderedPoints)) {
      orderedPoints = this.getOutlinePoints(tempBitmap);
    }

    return orderedPoints;
  }

  private void showErrorDialog() {
    String title = "Error";
    String message = this.getString(string.cantCrop);
    String ok = this.getString(string.ok);
    if (this.getActivity().getIntent().getStringExtra("scanCantCropFilterTitle") != null) {
      title = this.getActivity().getIntent().getStringExtra("scanCantCropFilterTitle");
    }

    if (this.getActivity().getIntent().getStringExtra("scanCantCropMessage") != null) {
      message = this.getActivity().getIntent().getStringExtra("scanCantCropMessage");
    }

    if (this.getActivity().getIntent().getStringExtra("scanOkLabel") != null) {
      ok = this.getActivity().getIntent().getStringExtra("scanOkLabel");
    }

    SingleButtonDialogFragment fragment = new SingleButtonDialogFragment(ok, message, title, true);
    FragmentManager fm = this.getActivity().getFragmentManager();
    fragment.show(fm, SingleButtonDialogFragment.class.toString());
  }

  private boolean isScanPointsValid(Map<Integer, PointF> points) {
    return points.size() == 4;
  }

  private Bitmap scaledBitmap(Bitmap bitmap, int width, int height) {
    Matrix m = new Matrix();
    m.setRectToRect(new RectF(0.0F, 0.0F, (float)bitmap.getWidth(), (float)bitmap.getHeight()), new RectF(0.0F, 0.0F, (float)width, (float)height), ScaleToFit.CENTER);
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
  }

  private Bitmap getScannedBitmap(Bitmap original, Map<Integer, PointF> points) {
    int width = original.getWidth();
    int height = original.getHeight();
    float xRatio = (float)original.getWidth() / (float)this.sourceImageView.getWidth();
    float yRatio = (float)original.getHeight() / (float)this.sourceImageView.getHeight();
    float x1 = ((PointF)points.get(0)).x * xRatio;
    float x2 = ((PointF)points.get(1)).x * xRatio;
    float x3 = ((PointF)points.get(2)).x * xRatio;
    float x4 = ((PointF)points.get(3)).x * xRatio;
    float y1 = ((PointF)points.get(0)).y * yRatio;
    float y2 = ((PointF)points.get(1)).y * yRatio;
    float y3 = ((PointF)points.get(2)).y * yRatio;
    float y4 = ((PointF)points.get(3)).y * yRatio;
    Log.d("", "POints(" + x1 + "," + y1 + ")(" + x2 + "," + y2 + ")(" + x3 + "," + y3 + ")(" + x4 + "," + y4 + ")");
    Bitmap _bitmap = ((ScanActivity)this.getActivity()).getScannedBitmap(original, x1, y1, x2, y2, x3, y3, x4, y4);
    return _bitmap;
  }

  protected void showProgressDialog(String message) {
    this.progressDialogFragment = new ProgressDialogFragment(message);
    FragmentManager fm = this.getFragmentManager();
    this.progressDialogFragment.show(fm, ProgressDialogFragment.class.toString());
  }

  protected void dismissDialog() {
    this.progressDialogFragment.dismissAllowingStateLoss();
  }

  private class ScanAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private Map<Integer, PointF> points;

    public ScanAsyncTask(Map<Integer, PointF> points) {
      this.points = points;
    }

    protected void onPreExecute() {
      super.onPreExecute();
      if (ScanFragment.this.getActivity().getIntent().getStringExtra("scanScanningMessage") != null) {
        ScanFragment.this.showProgressDialog(ScanFragment.this.getActivity().getIntent().getStringExtra("scanScanningMessage"));
      } else {
        ScanFragment.this.showProgressDialog(ScanFragment.this.getResources().getString(string.scanning));
      }

    }

    protected Bitmap doInBackground(Void... params) {
      Bitmap bitmap = ScanFragment.this.getScannedBitmap(ScanFragment.this.original, this.points);
      Uri uri = Utils.getUri(ScanFragment.this.getActivity(), bitmap);
      ScanFragment.this.scanner.onScanFinish(uri);
      return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);
      bitmap.recycle();
      ScanFragment.this.dismissDialog();
    }
  }

  private class ScanButtonClickListener implements OnClickListener {
    private ScanButtonClickListener() {
    }

    public void onClick(View v) {
      Map<Integer, PointF> points = ScanFragment.this.polygonView.getPoints();
      if (ScanFragment.this.isScanPointsValid(points)) {
        (ScanFragment.this.new ScanAsyncTask(points)).execute(new Void[0]);
      } else {
        ScanFragment.this.showErrorDialog();
      }

    }
  }
}

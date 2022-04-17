//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.scanlibrary.R.id;
import com.scanlibrary.R.layout;
import com.scanlibrary.R.string;
import java.io.IOException;

public class ResultFragment extends Fragment {
  private View view;
  private ImageView scannedImageView;
  private Button doneButton;
  private Bitmap original;
  private Button originalButton;
  private Button MagicColorButton;
  private Button grayModeButton;
  private Button bwButton;
  private Button rotanticButton;
  private Button rotcButton;
  private Bitmap transformed;
  private Bitmap rotoriginal;
  private static ProgressDialogFragment progressDialogFragment;

  public ResultFragment() {
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.view = inflater.inflate(layout.result_layout, (ViewGroup)null);
    this.init();
    return this.view;
  }

  private void init() {
    this.scannedImageView = (ImageView)this.view.findViewById(id.scannedImage);
    this.originalButton = (Button)this.view.findViewById(id.original);
    if (this.getActivity().getIntent().getStringExtra("scanORGText") != null) {
      this.originalButton.setText(this.getActivity().getIntent().getStringExtra("scanORGText"));
    }

    this.originalButton.setOnClickListener(new OriginalButtonClickListener());
    this.MagicColorButton = (Button)this.view.findViewById(id.magicColor);
    this.MagicColorButton.setOnClickListener(new MagicColorButtonClickListener());
    this.grayModeButton = (Button)this.view.findViewById(id.grayMode);
    this.grayModeButton.setOnClickListener(new GrayButtonClickListener());
    this.bwButton = (Button)this.view.findViewById(id.BWMode);
    if (this.getActivity().getIntent().getStringExtra("scanBNWText") != null) {
      this.bwButton.setText(this.getActivity().getIntent().getStringExtra("scanBNWText"));
    }

    this.bwButton.setOnClickListener(new BWButtonClickListener());
    this.rotanticButton = (Button)this.view.findViewById(id.rotanticButton);
    this.rotanticButton.setOnClickListener(new RotanticlockButtonClickListener());
    if (this.getActivity().getIntent().getStringExtra("scanRotateLeftText") != null) {
      this.rotanticButton.setText(this.getActivity().getIntent().getStringExtra("scanRotateLeftText"));
    }

    this.rotcButton = (Button)this.view.findViewById(id.rotcButton);
    if (this.getActivity().getIntent().getStringExtra("scanRotateRightText") != null) {
      this.rotcButton.setText(this.getActivity().getIntent().getStringExtra("scanRotateRightText"));
    }

    this.rotcButton.setOnClickListener(new RotclockButtonClickListener());
    Bitmap bitmap = this.getBitmap();
    this.transformed = bitmap;
    this.rotoriginal = bitmap;
    this.setScannedImage(bitmap);
    this.doneButton = (Button)this.view.findViewById(id.doneButton);
    if (this.getActivity().getIntent().getStringExtra("scanSaveText") != null) {
      this.doneButton.setText(this.getActivity().getIntent().getStringExtra("scanSaveText"));
    }

    this.doneButton.setOnClickListener(new DoneButtonClickListener());
  }

  private Bitmap getBitmap() {
    Uri uri = this.getUri();

    try {
      this.original = Utils.getBitmap(this.getActivity(), uri);
      this.getActivity().getContentResolver().delete(uri, (String)null, (String[])null);
      return this.original;
    } catch (IOException var3) {
      var3.printStackTrace();
      return null;
    }
  }

  private Uri getUri() {
    Uri uri = (Uri)this.getArguments().getParcelable("scannedResult");
    return uri;
  }

  public void setScannedImage(Bitmap scannedImage) {
    this.scannedImageView.setImageBitmap(scannedImage);
  }

  protected synchronized void disableButtons() {
    this.doneButton.setEnabled(false);
    this.originalButton.setEnabled(false);
    this.MagicColorButton.setEnabled(false);
    this.grayModeButton.setEnabled(false);
    this.bwButton.setEnabled(false);
    this.rotanticButton.setEnabled(false);
    this.rotcButton.setEnabled(false);
  }

  protected synchronized void enableButtons() {
    this.doneButton.setEnabled(true);
    this.originalButton.setEnabled(true);
    this.MagicColorButton.setEnabled(true);
    this.grayModeButton.setEnabled(true);
    this.bwButton.setEnabled(true);
    this.rotanticButton.setEnabled(true);
    this.rotcButton.setEnabled(true);
  }

  protected synchronized void showProgressDialog(String message) {
    this.disableButtons();
    if (progressDialogFragment != null && progressDialogFragment.isVisible()) {
      progressDialogFragment.dismissAllowingStateLoss();
    }

    progressDialogFragment = null;
    progressDialogFragment = new ProgressDialogFragment(message);
    FragmentManager fm = this.getFragmentManager();
    progressDialogFragment.show(fm, ProgressDialogFragment.class.toString());
  }

  protected synchronized void dismissDialog() {
    progressDialogFragment.dismissAllowingStateLoss();
    this.enableButtons();
  }

  private class RotclockButtonClickListener implements OnClickListener {
    private RotclockButtonClickListener() {
    }

    public void onClick(final View v) {
      if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage") != null) {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage"));
      } else {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.applying_filter));
      }

      AsyncTask.execute(new Runnable() {
        public void run() {
          try {
            Bitmap imageViewBitmap = ((BitmapDrawable)ResultFragment.this.scannedImageView.getDrawable()).getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0F);
            ResultFragment.this.rotoriginal = Bitmap.createBitmap(ResultFragment.this.rotoriginal, 0, 0, ResultFragment.this.rotoriginal.getWidth(), ResultFragment.this.rotoriginal.getHeight(), matrix, true);
            ResultFragment.this.transformed = Bitmap.createBitmap(imageViewBitmap, 0, 0, imageViewBitmap.getWidth(), imageViewBitmap.getHeight(), matrix, true);
          } catch (final OutOfMemoryError var3) {
            ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                ResultFragment.this.transformed = ResultFragment.this.original;
                ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.original);
                var3.printStackTrace();
                ResultFragment.this.dismissDialog();
                RotclockButtonClickListener.this.onClick(v);
              }
            });
          }

          ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
              ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.transformed);
              ResultFragment.this.dismissDialog();
            }
          });
        }
      });
    }
  }

  private class RotanticlockButtonClickListener implements OnClickListener {
    private RotanticlockButtonClickListener() {
    }

    public void onClick(final View v) {
      if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage") != null) {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage"));
      } else {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.applying_filter));
      }

      AsyncTask.execute(new Runnable() {
        public void run() {
          try {
            Bitmap imageViewBitmap = ((BitmapDrawable)ResultFragment.this.scannedImageView.getDrawable()).getBitmap();
            Matrix matrix = new Matrix();
            matrix.postRotate(-90.0F);
            ResultFragment.this.rotoriginal = Bitmap.createBitmap(ResultFragment.this.rotoriginal, 0, 0, ResultFragment.this.rotoriginal.getWidth(), ResultFragment.this.rotoriginal.getHeight(), matrix, true);
            ResultFragment.this.transformed = Bitmap.createBitmap(imageViewBitmap, 0, 0, imageViewBitmap.getWidth(), imageViewBitmap.getHeight(), matrix, true);
          } catch (final OutOfMemoryError var3) {
            ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                ResultFragment.this.transformed = ResultFragment.this.original;
                ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.original);
                var3.printStackTrace();
                ResultFragment.this.dismissDialog();
                RotanticlockButtonClickListener.this.onClick(v);
              }
            });
          }

          ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
              ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.transformed);
              ResultFragment.this.dismissDialog();
            }
          });
        }
      });
    }
  }

  private class GrayButtonClickListener implements OnClickListener {
    private GrayButtonClickListener() {
    }

    public void onClick(final View v) {
      if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage") != null) {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage"));
      } else {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.applying_filter));
      }

      AsyncTask.execute(new Runnable() {
        public void run() {
          try {
            ResultFragment.this.transformed = ((ScanActivity)ResultFragment.this.getActivity()).getGrayBitmap(ResultFragment.this.rotoriginal);
          } catch (final OutOfMemoryError var2) {
            ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                ResultFragment.this.transformed = ResultFragment.this.original;
                ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.original);
                var2.printStackTrace();
                ResultFragment.this.dismissDialog();
                GrayButtonClickListener.this.onClick(v);
              }
            });
          }

          ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
              ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.transformed);
              ResultFragment.this.dismissDialog();
            }
          });
        }
      });
    }
  }

  private class OriginalButtonClickListener implements OnClickListener {
    private OriginalButtonClickListener() {
    }

    public void onClick(View v) {
      try {
        if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage") != null) {
          ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage"));
        } else {
          ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.applying_filter));
        }

        ResultFragment.this.transformed = ResultFragment.this.rotoriginal;
        ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.rotoriginal);
        ResultFragment.this.dismissDialog();
      } catch (OutOfMemoryError var3) {
        var3.printStackTrace();
        ResultFragment.this.dismissDialog();
      }

    }
  }

  private class MagicColorButtonClickListener implements OnClickListener {
    private MagicColorButtonClickListener() {
    }

    public void onClick(final View v) {
      if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage") != null) {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage"));
      } else {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.applying_filter));
      }

      AsyncTask.execute(new Runnable() {
        public void run() {
          try {
            ResultFragment.this.transformed = ((ScanActivity)ResultFragment.this.getActivity()).getMagicColorBitmap(ResultFragment.this.rotoriginal);
          } catch (final OutOfMemoryError var2) {
            ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                ResultFragment.this.transformed = ResultFragment.this.original;
                ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.original);
                var2.printStackTrace();
                ResultFragment.this.dismissDialog();
                MagicColorButtonClickListener.this.onClick(v);
              }
            });
          }

          ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
              ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.transformed);
              ResultFragment.this.dismissDialog();
            }
          });
        }
      });
    }
  }

  private class BWButtonClickListener implements OnClickListener {
    private BWButtonClickListener() {
    }

    public void onClick(final View v) {
      if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage") != null) {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanApplyingFilterMessage"));
      } else {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.applying_filter));
      }

      AsyncTask.execute(new Runnable() {
        public void run() {
          try {
            ResultFragment.this.transformed = ((ScanActivity)ResultFragment.this.getActivity()).getBWBitmap(ResultFragment.this.rotoriginal);
          } catch (final OutOfMemoryError var2) {
            ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                ResultFragment.this.transformed = ResultFragment.this.original;
                ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.original);
                var2.printStackTrace();
                ResultFragment.this.dismissDialog();
                BWButtonClickListener.this.onClick(v);
              }
            });
          }

          ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
            public void run() {
              ResultFragment.this.scannedImageView.setImageBitmap(ResultFragment.this.transformed);
              ResultFragment.this.dismissDialog();
            }
          });
        }
      });
    }
  }

  private class DoneButtonClickListener implements OnClickListener {
    private DoneButtonClickListener() {
    }

    public void onClick(View v) {
      if (ResultFragment.this.getActivity().getIntent().getStringExtra("scanLoadingMessage") != null) {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getActivity().getIntent().getStringExtra("scanLoadingMessage"));
      } else {
        ResultFragment.this.showProgressDialog(ResultFragment.this.getResources().getString(string.loading));
      }

      AsyncTask.execute(new Runnable() {
        public void run() {
          try {
            Intent data = new Intent();
            Bitmap bitmap = ResultFragment.this.transformed;
            if (bitmap == null) {
              bitmap = ResultFragment.this.original;
            }

            Uri uri = Utils.getUri(ResultFragment.this.getActivity(), bitmap);
            data.putExtra("scannedResult", uri);
            ResultFragment.this.getActivity().setResult(-1, data);
            ResultFragment.this.original.recycle();
            System.gc();
            ResultFragment.this.getActivity().runOnUiThread(new Runnable() {
              public void run() {
                ResultFragment.this.dismissDialog();
                ResultFragment.this.getActivity().finish();
              }
            });
          } catch (Exception var4) {
            var4.printStackTrace();
          }

        }
      });
    }
  }
}

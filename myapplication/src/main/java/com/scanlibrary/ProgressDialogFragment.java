//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;

@SuppressLint({"ValidFragment"})
public class ProgressDialogFragment extends DialogFragment {
  public String message;

  public ProgressDialogFragment(String message) {
    this.message = message;
  }

  public Dialog onCreateDialog(Bundle savedInstanceState) {
    ProgressDialog dialog = new ProgressDialog(this.getActivity());
    dialog.setIndeterminate(true);
    dialog.setMessage(this.message);
    dialog.setCancelable(false);
    dialog.setCanceledOnTouchOutside(false);
    OnKeyListener keyListener = new OnKeyListener() {
      public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        return keyCode == 4;
      }
    };
    dialog.setOnKeyListener(keyListener);
    return dialog;
  }
}

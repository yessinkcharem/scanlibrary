//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.scanlibrary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

@SuppressLint({"ValidFragment"})
public class SingleButtonDialogFragment extends DialogFragment {
  protected String positiveButtonTitle;
  protected String message;
  protected String title;
  protected boolean isCancelable;

  public SingleButtonDialogFragment(String positiveButtonTitle, String message, String title, boolean isCancelable) {
    this.positiveButtonTitle = positiveButtonTitle;
    this.message = message;
    this.title = title;
    this.isCancelable = isCancelable;
  }

  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Builder builder = (new Builder(this.getActivity())).setTitle(this.title).setCancelable(this.isCancelable).setMessage(this.message).setPositiveButton(this.positiveButtonTitle, new OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
      }
    });
    return builder.create();
  }
}

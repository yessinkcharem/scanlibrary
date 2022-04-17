package com.scanlibrary;

import android.os.Environment;

public class ScanConstants {
  public static final int PICKFILE_REQUEST_CODE = 1;

  public static final int START_CAMERA_REQUEST_CODE = 2;

  public static final String OPEN_INTENT_PREFERENCE = "selectContent";

  public static final String IMAGE_BASE_PATH_EXTRA = "ImageBasePath";

  public static final int OPEN_CAMERA = 4;

  public static final int OPEN_MEDIA = 5;

  public static final String SCANNED_RESULT = "scannedResult";

  public static final String IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/scanSample";

  public static final String SELECTED_BITMAP = "selectedBitmap";

  public static final String SCAN_NEXT_TEXT = "scanNextText";

  public static final String SCAN_SAVE_TEXT = "scanSaveText";

  public static final String SCAN_ROTATE_LEFT_TEXT = "scanRotateLeftText";

  public static final String SCAN_ROTATE_RIGHT_TEXT = "scanRotateRightText";

  public static final String SCAN_BNW_TEXT = "scanBNWText";

  public static final String SCAN_ORG_TEXT = "scanORGText";

  public static final String SCAN_SCANNING_MESSAGE = "scanScanningMessage";

  public static final String SCAN_LOADING_MESSAGE = "scanLoadingMessage";

  public static final String SCAN_APPLYING_FILTER_MESSAGE = "scanApplyingFilterMessage";

  public static final String SCAN_CANT_CROP_ERROR_MESSAGE = "scanCantCropMessage";

  public static final String SCAN_CANT_CROP_ERROR_TITLE = "scanCantCropFilterTitle";

  public static final String SCAN_OK_LABEL = "scanOkLabel";
}


/* Location:              C:\Users\ASUS\Desktop\AndroidDocumentScanLibrary-1.8.2\jars\classes.jar!\com\scanlibrary\ScanConstants.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */
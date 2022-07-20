package com.mobiato.sfa.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CheckPermissionUtils {

    public static final String READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String ACCESS_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;


    public static final int REQUEST_CODE_READ_EXTERNAL = 101;
    public static final int WRITE_CODE_READ_EXTERNAL = 102;
    public static final int CAMERA_CODE = 103;
    public static final int PHONE_STATE_CODE = 104;
    public static final int RECORD_AUDIO_CODE = 105;
    public static final int MULTIPLE = 106;
    public static final int WRITE_CALENDAR_CODE = 107;
    public static final int ACCESS_LOCATION_CODE = 108;

    public static boolean checkPermission(Context mContext, String permission) {
        return ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void isCheck(Context mContext, String[] permission, int requestCode, permissionListener mListener) {
        if (hasPermissions(mContext, permission)) {
            if (mListener != null)
                mListener.onAllow(requestCode);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, permission, requestCode);
        }
    }

    public static void isCheck(Context mContext, String permission, int requestCode, permissionListener mListener) {
        if (checkPermission(mContext, permission)) {
            if (mListener != null)
                mListener.onAllow(requestCode);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
        }
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, permissionListener permissionListener) {
        if (grantResults.length > 0) {
            boolean valid = true;
            for (int grantResult : grantResults) {
                valid = valid && grantResult == PackageManager.PERMISSION_GRANTED;
            }
            if (valid)
                permissionListener.onAllow(requestCode);
            else
                permissionListener.onDeny(requestCode);
        } else
            permissionListener.onDeny(requestCode);
    }


    public interface permissionListener {
        public void onAllow(int requestCode);

        public void onDeny(int requestCode);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}

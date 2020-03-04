package cn.com.cretech.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.PermissionChecker;

public class UtilPermission {
    public static boolean requireSDKInt(int sdkInt) {
                 return Build.VERSION.SDK_INT >= sdkInt;
             }
     public static boolean checkPermissionGranted(Context context, String permission) {
                 return PermissionChecker.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
             }

}

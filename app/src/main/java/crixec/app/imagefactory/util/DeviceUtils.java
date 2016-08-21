package crixec.app.imagefactory.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Toast;

public class DeviceUtils {
    private static String TAG = "DeviceUtils";

    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    public static String getSystemManufacture() {
        return android.os.Build.MANUFACTURER;
    }

    public static int getSystemSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

//
//    public static String getArchitecture() {
//        String arch = "Unsupported CPU Architecture!";
//        boolean x64 = false;
//        if (Build.VERSION.SDK_INT >= 21) {
//            arch = Build.SUPPORTED_ABIS[0];
//        } else {
//            arch = Build.CPU_ABI;
//        }
//        x64 = arch.indexOf("64") != -1;
//        if (arch.indexOf("arm") != -1) {
//            if (!x64) arch = "armeabi-v7a";
//            arch = "arm64-v8a";
//        } else if (arch.indexOf("86") != -1) {
//            if (!x64) arch = "x86";
//            arch = "x86_64";
//        }
//        Log.i(TAG, "The base cpu architecture : " + arch);
//        return arch;
//    }

    public static void openFolder(Context context, File file) {
        if (null == file || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "resource/folder");
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeShortText(String.format(context.getString(R.string.operation_failed), e.toString()));
        }
    }

    public static void openFile(Context context, File file) {
        openFolder(context, file.getParentFile());
    }

    public static void openUrl(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    //
//    public static void openAppInMarket(Context context, String pkgName) {
//        Intent viewIntent = new Intent("android.intent.action.VIEW",
//                Uri.parse("market://details?id=" + pkgName));
//        context.startActivity(viewIntent);
//    }
//
    public static int getScrenWidth() {
        return ImageFactory.getApp().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return ImageFactory.getApp().getResources().getDisplayMetrics().heightPixels;
    }

    public static float getScreenDensity() {
        return ImageFactory.getApp().getResources().getDisplayMetrics().scaledDensity;
    }

    public static String getSystemTime() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date(System.currentTimeMillis()));
    }
}

package crixec.app.imagefactory.core;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import crixec.app.imagefactory.BuildConfig;
import crixec.app.imagefactory.util.DeviceUtils;

public class ExceptionHandler {
    public static void handle(Exception e) {
        StringBuilder mStringBuilder = new StringBuilder();
        mStringBuilder.append(ExceptionHandler.getSystemInfo());
        mStringBuilder.append(e.getMessage() + "\n");
        StringWriter mStringWriter = new StringWriter();
        PrintWriter pw = new PrintWriter(mStringWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        mStringBuilder.append(mStringWriter.toString());
        handle(mStringBuilder.toString());
    }

    public static void handle(CharSequence text) {
        Debug.e("ExceptionHandler", text.toString());
    }

    public static String getSystemInfo() {
        StringBuilder mStringBuilder = new StringBuilder();
        String baseLine = "========================";
        mStringBuilder.append(baseLine);
        mStringBuilder.append("\nDEVICE_MODEL:" + DeviceUtils.getSystemModel());
        mStringBuilder.append("\nDEVICE_MANUFACTURE:" + DeviceUtils.getSystemManufacture());
        mStringBuilder.append("\nDEVICE_SDK_VERSION:" + DeviceUtils.getSystemSDKVersion());
        //mStringBuilder.append("\nAPP_VERSION_CODE:" + ImageFactory.getPackageVersionCode());
        //mStringBuilder.append("\nAPP_VERSION_NAME:" + ImageFactory.getPackageVersionName());
        mStringBuilder.append("\n" + baseLine + "\n");
        return mStringBuilder.toString();
    }
}

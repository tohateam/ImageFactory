package crixec.app.imagefactory.core;

import android.app.Application;
import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

import crixec.app.imagefactory.BuildConfig;

public class ImageFactory extends Application {
    public static Application APP;
    public static File DATA_PATH;

    @Override
    public void onCreate() {
        // TODO: Implement this method
        super.onCreate();
        APP = this;
        CrashReport.initCrashReport(getApplicationContext(), "b625275adf", BuildConfig.DEBUG);
        new AppLoader().start();
    }

    public static Context getApp() {
        // TODO: Implement this method
        return APP;
    }
}

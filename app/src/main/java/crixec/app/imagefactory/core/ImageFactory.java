package crixec.app.imagefactory.core;

import android.app.Application;
import android.content.Context;

import net.youmi.android.spot.SpotManager;

import java.io.File;

import crixec.app.imagefactory.function.bootimage.Device;
import crixec.app.imagefactory.util.DeviceUtils;

public class ImageFactory extends Application {
    public static Application APP;
    public static File DATA_PATH;

    @Override
    public void onCreate() {
        // TODO: Implement this method
        super.onCreate();
        APP = this;
        new AppLoader().start();
    }

    public static Context getApp() {
        // TODO: Implement this method
        return APP;
    }

    public static void show(Context context) {
        SpotManager.getInstance(context).showSpotAds(context);
    }
}

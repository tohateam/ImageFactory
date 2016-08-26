package crixec.app.imagefactory.core;

import android.os.Environment;

import java.io.File;

import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.XmlDataUtils;

public class AppLoader extends Thread {

    private String TAG = "AppLoader";

    @Override
    public void run() {
        // TODO: Implement this method
        super.run();
        XmlDataUtils.getInstance().init();
        if (XmlDataUtils.getString(Constant.KEY_DATA_PATH).equals("")) {
            XmlDataUtils.putString(Constant.KEY_DATA_PATH, getDefaultStorage().getPath());
        }
        ImageFactory.DATA_PATH = new File(XmlDataUtils.getString(Constant.KEY_DATA_PATH));
        if (!ImageFactory.DATA_PATH.exists()) {
            if (ImageFactory.DATA_PATH.mkdirs()) {
                ImageFactory.DATA_PATH = getDefaultStorage();
            }
        }
        ImageFactory.DATA_PATH.mkdirs();
        File log = new File(ImageFactory.DATA_PATH, ".logs");
        log.mkdirs();
        Debug.init(new File(log, "log_" + DeviceUtils.getSystemTime() + ".txt"));
        Debug.i(TAG, "Loading app");
        Debug.i(TAG, "DATA_PATH=" + ImageFactory.DATA_PATH);
        File kernelBackups = new File(ImageFactory.DATA_PATH, "backups");
        File kernelUnpacked = new File(ImageFactory.DATA_PATH, "unpacked");
        File kernelRepacked = new File(ImageFactory.DATA_PATH, "repacked");
        File imageConverted = new File(ImageFactory.DATA_PATH, "converted");
        kernelBackups.mkdirs();
        kernelRepacked.mkdirs();
        kernelUnpacked.mkdirs();
        imageConverted.mkdirs();
        ImageFactory.KERNEL_BACKUPS = kernelBackups;
        ImageFactory.KERNEL_REPACKED = kernelRepacked;
        ImageFactory.KERNEL_UNPACKED = kernelUnpacked;
        ImageFactory.IMAGE_CONVERTED = imageConverted;
    }

    public static File getDefaultStorage() {
        return new File(Environment.getExternalStorageDirectory(), "ImageFactory");
    }
}

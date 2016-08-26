package crixec.app.imagefactory.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ImageFactory;

/**
 * Created by Crixec on 2016/8/13.
 */
public class FlashUtils {
    private static String TAG = "FlashUtils";

    // some devices not using SuperSu will causing FileNotFoundException
    //
    public static boolean flash(File file, File dev, ShellUtils.Result result) {
        File tmpFile = new File(ImageFactory.getApp().getFilesDir(), file.getName());
        try {
            String s = "creating tmp file : " + tmpFile.getPath();
            Debug.i(TAG, s);
            result.onStdout(s);
            FileUtils.copyFile(file, tmpFile);
            Toolbox.envalid(tmpFile, result);
            return Toolbox.dd(tmpFile, dev, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            tmpFile.delete();
        }
    }

    public static boolean backup(File dev, File file, ShellUtils.Result result) {
        File tmpFile = new File(ImageFactory.getApp().getFilesDir(), file.getName());
        try {
            String s = "creating tmp file : " + tmpFile.getPath();
            Debug.i(TAG, s);
            result.onStdout(s);
            if (Toolbox.dd(dev, tmpFile, result)) {
                Toolbox.envalid(tmpFile, result);
                FileUtils.copyFile(tmpFile, file);
                return true;
            }
            return false;
        } catch (IOException e) {
            result.onStderr(e.toString());
            throw new RuntimeException(e);
        } finally {
            tmpFile.delete();
        }
    }
}

package crixec.app.imagefactory.util;

import java.io.File;
import java.io.IOException;

import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ImageFactory;

/**
 * Created by Crixec on 2016/8/13.
 */
public class FlashUtils {
    private static String TAG = "FlashUtils";

    // some devices not using SuperSu will causing FileNotFoundException
    //
    public static boolean flash(File file, File dev) {
        File tmpFile = new File(ImageFactory.getApp().getFilesDir(), file.getName());
        try {
            Toolbox.chmod(tmpFile, "0777");
            Debug.i(TAG, "creating tmp file : " + tmpFile.getPath());
            FileUtils.copyFile(file, tmpFile);
            return 0 == ShellUtils.execRoot(String.format("dd if=\'%s\' of=\'%s\'", tmpFile.getPath(), dev.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            tmpFile.delete();
        }
    }

    public static boolean backup(File dev, File file) {
        File tmpFile = new File(ImageFactory.getApp().getFilesDir(), file.getName());
        try {
            Debug.i(TAG, "creating tmp file : " + tmpFile.getPath());
            if (0 == ShellUtils.execRoot(String.format("dd if=\'%s\' of=\'%s\'", dev.getPath(), tmpFile.getPath()))) {
                Toolbox.chmod(tmpFile, "0777");
                FileUtils.copyFile(tmpFile, file);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            tmpFile.delete();
        }
    }
}

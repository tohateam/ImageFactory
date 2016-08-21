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
            FileUtils.setValid(tmpFile);
            String s = "creating tmp file : " + tmpFile.getPath();
            Debug.i(TAG, s);
            result.onStdout(s);
            FileUtils.copyFile(file, tmpFile);
            return 0 == ShellUtils.execRoot(String.format("dd if=\'%s\' of=\'%s\'", tmpFile.getPath(), dev.getPath()), result);
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
            List<String> cmds = new ArrayList<String>();
            cmds.add(String.format("dd if=\'%s\' of=\'%s\'", dev.getPath(), tmpFile.getPath()));
            cmds.add(String.format("chmod 0755 \'%s\'", tmpFile.getPath()));
            if (0 == ShellUtils.exec(cmds, result, true)) {
                FileUtils.setValid(tmpFile);
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

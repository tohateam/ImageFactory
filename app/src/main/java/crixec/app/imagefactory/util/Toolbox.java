package crixec.app.imagefactory.util;

import android.os.Build;

import java.io.File;

/**
 * Created by Crixec on 2016/8/13.
 */
public class Toolbox {
    public static final int REBOOT_HOTREBOOT = 1;
    public static final int REBOOT_REBOOT = 2;
    public static final int REBOOT_SHUTDOWN = 3;
    public static final int REBOOT_RECOVERY = 4;
    public static final int REBOOT_BOOTLOADER = 5;

    public static String getToolbox() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return "/system/bin/toybox";
        } else {
            return "/system/bin/toolbox";
        }
    }

    public static void reboot(int action) {
        String command;
        switch (action) {
            case REBOOT_REBOOT:
                command = "reboot";
                break;
            case REBOOT_SHUTDOWN:
                command = "reboot -p";
                break;
            case REBOOT_RECOVERY:
                command = "reboot recovery";
                break;
            case REBOOT_HOTREBOOT:
                command = "killall -9 system_server";
                break;
            case REBOOT_BOOTLOADER:
                command = "reboot bootloader";
                break;
            default:
                command = "reboot";
                break;
        }
        ShellUtils.execRoot(command);
    }

    public static boolean copy(File from, File to) {
        return ShellUtils.exec(String.format("%s cp \'%s\' \'%s\'", getToolbox(), from.getPath(), to.getPath())) == 0;
    }

    public static boolean chmod(File file, String mode) {
        return ShellUtils.execRoot(String.format("%s chmod %s \'%s\'", getToolbox(), mode, file.getPath())) == 0;
    }
}

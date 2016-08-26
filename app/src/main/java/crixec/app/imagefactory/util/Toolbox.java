package crixec.app.imagefactory.util;

import android.os.Build;

import java.io.File;

import crixec.app.imagefactory.core.Invoker;

/**
 * Created by Crixec on 2016/8/13.
 */
public class Toolbox {
    public static final int REBOOT_HOTREBOOT = 1;
    public static final int REBOOT_REBOOT = 2;
    public static final int REBOOT_SHUTDOWN = 3;
    public static final int REBOOT_RECOVERY = 4;
    public static final int REBOOT_BOOTLOADER = 5;

    public static boolean envalid(File file) {
        return envalid(file, null);
    }

    public static boolean envalid(File file, ShellUtils.Result result) {
        String cmd = String.format("%s envalid \'%s\'", Invoker.getInvoker(), file.getPath());
        return ShellUtils.execRoot(cmd, result) == 0;
    }

    public static boolean dd(File from, File to) {
        return dd(from, to, null);
    }

    public static boolean dd(File from, File to, ShellUtils.Result result) {
        String cmd = String.format("%s dd if=\'%s\' of=\'%s\'", Invoker.getInvoker(), from.getPath(), to.getPath());
        return ShellUtils.execRoot(cmd, result) == 0;
    }

    public static void reboot(int action) {
        String command;
        switch (action) {
            case REBOOT_REBOOT:
                command = "reboot";
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
}

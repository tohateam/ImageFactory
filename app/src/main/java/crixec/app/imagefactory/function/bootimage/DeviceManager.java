package crixec.app.imagefactory.function.bootimage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.util.FileUtils;
import crixec.app.imagefactory.util.ShellUtils;

/**
 * Created by crixec on 2016/8/3.
 */

public class DeviceManager {
    private static Device device;
    private static String mRecoveryPath;
    private static String mKernelPath;
    private static String RECOVERY_VERIFY = "RECOVERY_PATH=";
    private static String KERNEL_VERIFY = "KERNEL_PATH=";
    private static String TAG = "DeviceManager";

    public static Device get() {
        if (device == null) {
            String REC = "get-recovery-path.sh";
            String KER = "get-kernel-path.sh";
            File getRecovery = new File(ImageFactory.getApp().getFilesDir(), REC);
            File getKernel = new File(ImageFactory.getApp().getFilesDir(), KER);
            if (!getKernel.canExecute() || getRecovery.canExecute()) {
                try {
                    FileUtils.writeFile(ImageFactory.getApp().getAssets().open(KER), getKernel);
                    FileUtils.writeFile(ImageFactory.getApp().getAssets().open(REC), getRecovery);
                    FileUtils.setValid(getKernel);
                    FileUtils.setValid(getRecovery);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            List<String> cmds = new ArrayList<>();
            cmds.add(getRecovery.getPath());
            cmds.add(getKernel.getPath());
            ShellUtils.exec(cmds, new ShellUtils.Result() {
                @Override
                public void onStdout(String text) {
                    Debug.i(TAG, text);
                    if (text.contains(RECOVERY_VERIFY)) {
                        mRecoveryPath = text.replace(RECOVERY_VERIFY, "");
                    }
                    if (text.contains(KERNEL_VERIFY)) {
                        mKernelPath = text.replace(KERNEL_VERIFY, "");
                    }
                }

                @Override
                public void onStderr(String text) {

                }
            }, true);
            if (mKernelPath == null || mRecoveryPath == null) {
                device = new Device("", "");
            } else {
                device = new Device(mRecoveryPath, mKernelPath);
            }
        }
        return device;
    }
}

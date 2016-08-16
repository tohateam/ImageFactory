package crixec.app.imagefactory.function.bootimage;

/**
 * Created by crixec on 2016/8/3.
 */
public class Device {
    public static final int KERNEL = 0;
    public static final int RECOVERY = 1;
    private String recoveryPath;
    private String kernelPath;

    public Device(String recoveryPath, String kernelPath) {
        this.recoveryPath = recoveryPath;
        this.kernelPath = kernelPath;
    }

    public String getRecoveryPath() {
        return recoveryPath;
    }

    public void setRecoveryPath(String recoveryPath) {
        this.recoveryPath = recoveryPath;
    }

    public String getKernelPath() {
        return kernelPath;
    }

    public void setKernelPath(String kernelPath) {
        this.kernelPath = kernelPath;
    }
}

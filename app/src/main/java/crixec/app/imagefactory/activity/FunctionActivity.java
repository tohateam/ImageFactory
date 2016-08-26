package crixec.app.imagefactory.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.fragment.AboutFragment;
import crixec.app.imagefactory.fragment.BackupBootImageFragment;
import crixec.app.imagefactory.fragment.FlashBootImageFragment;
import crixec.app.imagefactory.fragment.Img2simgFragment;
import crixec.app.imagefactory.fragment.LogcatFragment;
import crixec.app.imagefactory.fragment.PortBootImageFragment;
import crixec.app.imagefactory.fragment.RepackBootImageFragment;
import crixec.app.imagefactory.fragment.Sdat2imgFragment;
import crixec.app.imagefactory.fragment.SettingFragment;
import crixec.app.imagefactory.fragment.Simg2imgFragment;
import crixec.app.imagefactory.fragment.SplitAppFragment;
import crixec.app.imagefactory.fragment.UnpackBootImageFragment;

public class FunctionActivity extends BaseChildActivity {

    public static final int FUNCTION_UNPACK_BOOT_IMAGE = 1001 << 1;
    public static final int FUNCTION_REPACK_BOOT_IMAGE = 1001 << 2;
    public static final int FUNCTION_PORT_BOOT_IMAGE = 1001 << 3;
    public static final int FUNCTION_BACKUP_BOOT_IMAGE = 1001 << 4;
    public static final int FUNCTION_FLASH_BOOT_IMAGE = 1001 << 5;
    public static final int FUNCTION_SIMG2IMG = 1001 << 6;
    public static final int FUNCTION_IMG2SIMG = 1001 << 7;
    public static final int FUNCTION_SDAT2IMG = 1001 << 8;
    public static final int FUNCTION_SPLIT_APP = 1001 << 9;
    public static final int FUNCTION_ABOUT = 1001 << 10;
    public static final int FUNCTION_SETTING = 1001 << 11;
    public static final int FUNCTION_LOGCAT = 1001 << 12;
    public static String TOKEN = "Image Factory";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        applyToolbar(R.id.toolbar);
        setType(getIntent().getIntExtra(TOKEN, FUNCTION_UNPACK_BOOT_IMAGE));
        Fragment fragment = null;
        String title = getString(R.string.function_unpack_boot_image);
        switch (type) {
            case FUNCTION_UNPACK_BOOT_IMAGE:
                fragment = UnpackBootImageFragment.newInstance(this);
                break;
            case FUNCTION_REPACK_BOOT_IMAGE:
                fragment = RepackBootImageFragment.newInstance(this);
                title = getString(R.string.function_repack_boot_image);
                break;
            case FUNCTION_PORT_BOOT_IMAGE:
                fragment = PortBootImageFragment.newInstance(this);
                title = getString(R.string.function_port_boot_image);
                break;
            case FUNCTION_BACKUP_BOOT_IMAGE:
                fragment = BackupBootImageFragment.newInstance(this);
                title = getString(R.string.function_backup_boot_image);
                break;
            case FUNCTION_FLASH_BOOT_IMAGE:
                fragment = FlashBootImageFragment.newInstance(this);
                title = getString(R.string.function_flash_boot_image);
                break;
            case FUNCTION_SIMG2IMG:
                fragment = Simg2imgFragment.newInstance(this);
                title = getString(R.string.function_simg2img);
                break;
            case FUNCTION_IMG2SIMG:
                fragment = Img2simgFragment.newInstance(this);
                title = getString(R.string.function_img2simg);
                break;
            case FUNCTION_SDAT2IMG:
                fragment = Sdat2imgFragment.newInstance(this);
                title = getString(R.string.function_sdat2img);
                break;
            case FUNCTION_SPLIT_APP:
                fragment = SplitAppFragment.newInstance(this);
                title = getString(R.string.function_split_app);
                break;
            case FUNCTION_ABOUT:
                fragment = AboutFragment.newInstance(this);
                title = getString(R.string.function_about);
                break;
            case FUNCTION_SETTING:
                fragment = new SettingFragment();
                title = getString(R.string.function_setting);
                break;
            case FUNCTION_LOGCAT:
                fragment = LogcatFragment.newInstance(this);
                title = getString(R.string.function_logcat);
                break;
        }
        setTitle(title);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    public void setType(int type) {
        this.type = type;
    }
}

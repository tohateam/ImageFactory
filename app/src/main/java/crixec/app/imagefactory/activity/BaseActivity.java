package crixec.app.imagefactory.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import net.youmi.android.spot.SpotManager;

/**
 * Created by crixec on 2016/8/3.
 */
public class BaseActivity extends AppCompatActivity {
    public static final String BOOT_IMAGE_TYPE_COMMON = "COMMON_BOOT_IMAGE_TYPE";

    public static final String BOOT_IMAGE_TYPE_MTK = "MTK_BOOT_IMAGE_TYPE";

    public static final String TOKEN_OPEN_UNPACK_BOOT_IMAGE_ACTIVITY = "OPEN_UNPACK_BOOT_IMAGE_ACTIVITY_TOKEN";

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}

package crixec.app.imagefactory.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Constant;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.XmlDataUtils;

public class SplashActivity extends BaseActivity {
    private static long DURATION = 2000;
    private static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        enterAnimation(findViewById(R.id.imageView));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    Debug.i(TAG, "attempting get permission : WRITE_EXTERNAL_STORAGE");
                    if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                Constant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                    }
                }
                openApp();
            }
        }, DURATION);
    }

    private void openApp() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void enterAnimation(View view) {
        view.setVisibility(View.VISIBLE);
        ScaleAnimation animation = new ScaleAnimation(0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF);
        animation.setDuration(DURATION - 500);
        view.startAnimation(animation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                Debug.i(TAG, "got permission : WRITE_EXTERNAL_STORAGE");
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getString(R.string.permission_not_granted), 1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Process.killProcess(Process.myPid());
                        }
                    }, 1000);
                } else {
                    startActivity(getIntent());
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}

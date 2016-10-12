package crixec.app.imagefactory.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.AppLoader;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.ui.Dialog;

public class SplashActivity extends BaseActivity {
    private static String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View view = findViewById(R.id.imageView);
        view.setVisibility(View.VISIBLE);
        ScaleAnimation animation = new ScaleAnimation(0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF);
        animation.setDuration(1500);
        view.startAnimation(animation);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    Debug.i(TAG, "attempting get permission : WRITE_EXTERNAL_STORAGE");
                    requestStoragePermission();
                } else {
                    openApp();
                }
            }
        }, 2000);

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openApp();
            return;
        }

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
    }

    private void openApp() {
        new AppLoader().start();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openApp();
        } else {
            Dialog.create(this).setTitle(R.string.warning)
                    .setMessage(R.string.no_write_external_storage_permission)
                    .setCancelable(false)
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

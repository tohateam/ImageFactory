package crixec.app.imagefactory.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.adapter.FunctionAdapter;
import crixec.app.imagefactory.adapter.OnRecyclerViewItemClickListener;
import crixec.app.imagefactory.bean.Function;
import crixec.app.imagefactory.core.Constant;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.Toolbox;
import crixec.app.imagefactory.util.XmlDataUtils;
import crixec.app.imagefactory.util.loader.ChangeLogLoader;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnRecyclerViewItemClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private FunctionAdapter adapter;
    private List<Function> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initContainer();

        if (!XmlDataUtils.getBoolean(Constant.KEY_IS_INITED)) {
            // do first open app
            View contentView = getLayoutInflater().inflate(R.layout.layout_disclaimer, null, false);
            final AppCompatCheckBox checker = (AppCompatCheckBox) contentView.findViewById(R.id.checker);
            final AppCompatButton okButton = (AppCompatButton) contentView.findViewById(R.id.ok);
            final AppCompatButton cancelButton = (AppCompatButton) contentView.findViewById(R.id.cancel);
            final AlertDialog.Builder dialogBuilder = Dialog.create(this);
            final AlertDialog dialog = dialogBuilder.setTitle(R.string.disclaimer)
                    .setView(contentView)
                    .setCancelable(false)
                    .create();
            checker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    okButton.setEnabled(isChecked);
                }
            });
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    XmlDataUtils.putBoolean(Constant.KEY_IS_INITED, true);
                    showChangeLog();
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    finish();
                }
            });
            dialog.show();
        }
    }

    private void initContainer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLog();
            }
        });
        ((AppCompatTextView) navigationView.getHeaderView(0).findViewById(R.id.version)).setText(DeviceUtils.getVersionName(this));
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        addFunctions();
        adapter = new FunctionAdapter(getLayoutInflater(), datas, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void showChangeLog() {
        View content = getLayoutInflater().inflate(R.layout.large_text_view, null, false);
        TextView textView = (TextView) content.findViewById(R.id.content);
        new ChangeLogLoader(textView).execute();
        Dialog.create(MainActivity.this).setTitle(R.string.change_log)
                .setView(content)
                .setPositiveButton(android.R.string.ok, null)
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.drawer_about: {
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_ABOUT);
                break;
            }
            case R.id.drawer_setting: {
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_SETTING);
                break;
            }
            case R.id.drawer_logcat: {
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_LOGCAT);
                break;
            }
            case R.id.reboot_normal: {
                showRebootConfirmDialog(R.string.reboot_normal_title, Toolbox.REBOOT_REBOOT);
                return false;
            }
            case R.id.reboot_hot: {
                showRebootConfirmDialog(R.string.reboot_hot_title, Toolbox.REBOOT_HOTREBOOT);
                return false;
            }
            case R.id.reboot_recovery: {
                showRebootConfirmDialog(R.string.reboot_recovery_title, Toolbox.REBOOT_RECOVERY);
                return false;
            }
            case R.id.reboot_bootloader: {
                showRebootConfirmDialog(R.string.reboot_bootloader_title, Toolbox.REBOOT_BOOTLOADER);
                return false;
            }
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void showRebootConfirmDialog(int titleRes, final int action) {
        Dialog.create(this).setTitle(titleRes).setMessage(getString(R.string.are_you_sure_you_want_to_do_this)).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toolbox.reboot(action);
            }
        }).setNegativeButton(android.R.string.no, null).setCancelable(true).show();
    }

    private boolean nextExit = false;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (nextExit == true) {
                Process.killProcess(Process.myPid());
            } else {
                Toast.makeText(getString(R.string.press_again_to_exit), 1000);
                nextExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextExit = false;
                    }
                }, 1000);
            }
        }
    }

    private void addFunctions() {
        Function unpackBoot = new Function(getString(R.string.function_unpack_boot_image), getString(R.string.function_unpack_boot_image_summary));
        Function repackBoot = new Function(getString(R.string.function_repack_boot_image), getString(R.string.function_repack_boot_image_summary));
        Function portBoot = new Function(getString(R.string.function_port_boot_image), getString(R.string.function_port_boot_image_summary));
        Function backupBoot = new Function(getString(R.string.function_backup_boot_image), getString(R.string.function_backup_boot_image_summary));
        Function flashBoot = new Function(getString(R.string.function_flash_boot_image), getString(R.string.function_flash_boot_image_summary));
        Function simg2img = new Function(getString(R.string.function_simg2img), getString(R.string.function_simg2img_summary));
        Function img2simg = new Function(getString(R.string.function_img2simg), getString(R.string.function_img2simg_summary));
        Function sdat2img = new Function(getString(R.string.function_sdat2img), getString(R.string.function_sdat2img_summary));
        Function unpackapp = new Function(getString(R.string.function_split_app), getString(R.string.function_split_app_summary));
        datas.add(unpackBoot);
        datas.add(repackBoot);
        datas.add(portBoot);
        datas.add(flashBoot);
        datas.add(backupBoot);
        datas.add(simg2img);
        datas.add(img2simg);
        datas.add(sdat2img);
        datas.add(unpackapp);
    }

    @Override
    public void onClick(View view, int pos) {
        Intent intent = null;
        switch (pos) {
            case 0:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_UNPACK_BOOT_IMAGE);
                break;
            case 1:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_REPACK_BOOT_IMAGE);
                break;
            case 2:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_PORT_BOOT_IMAGE);
                break;
            case 3:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_FLASH_BOOT_IMAGE);
                break;
            case 4:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_BACKUP_BOOT_IMAGE);
                break;
            case 5:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_SIMG2IMG);
                break;
            case 6:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_IMG2SIMG);
                break;
            case 7:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_SDAT2IMG);
                break;
            case 8:
                intent = new Intent(this, FunctionActivity.class);
                intent.putExtra(FunctionActivity.TOKEN, FunctionActivity.FUNCTION_SPLIT_APP);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}

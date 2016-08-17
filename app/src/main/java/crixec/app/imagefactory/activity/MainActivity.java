package crixec.app.imagefactory.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.adapter.MainPageAdapter;
import crixec.app.imagefactory.core.Constant;
import crixec.app.imagefactory.function.bootimage.BootImageFragment;
import crixec.app.imagefactory.function.convertimage.ConvertImageFragment;
import crixec.app.imagefactory.function.firmware.FirmwareFragment;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.Toolbox;
import crixec.app.imagefactory.util.XmlDataUtils;
import crixec.app.imagefactory.util.loader.ChangeLogLoader;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    private ArrayList<String> names = new ArrayList<String>();
    private MainPageAdapter fragmentAdapter;
    private String TAG = "MainActivity";

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
        viewPager = (ViewPager) findViewById(R.id.activity_main_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.activity_main_tablayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // add main page tabs
        BootImageFragment bootImageFragment = new BootImageFragment();
        ConvertImageFragment sparseImageFragment = new ConvertImageFragment();
        final FirmwareFragment firmwareFragment = new FirmwareFragment();
        fragments.add(bootImageFragment);
        fragments.add(sparseImageFragment);
        fragments.add(firmwareFragment);
        names.add(getString(R.string.function_boot_image));
        names.add(getString(R.string.function_convert_image));
        names.add(getString(R.string.function_firmware_tool));
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        fragmentAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(names.get(0));
        tabLayout.getTabAt(1).setText(names.get(1));
        tabLayout.getTabAt(2).setText(names.get(2));
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLog();
            }
        });
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
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;
            }
            case R.id.drawer_setting: {
                intent = new Intent(MainActivity.this, SettingActivity.class);
                break;
            }
            case R.id.drawer_logcat: {
                intent = new Intent(MainActivity.this, LogcatActivity.class);
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
            case R.id.reboot_shutdown: {
                showRebootConfirmDialog(R.string.reboot_shutdown_title, Toolbox.REBOOT_SHUTDOWN);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}

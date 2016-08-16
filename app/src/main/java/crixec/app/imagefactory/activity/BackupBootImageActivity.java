package crixec.app.imagefactory.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.spot.SpotManager;

import java.io.File;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.function.bootimage.Device;
import crixec.app.imagefactory.function.bootimage.DeviceGetter;
import crixec.app.imagefactory.function.bootimage.DeviceManager;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FlashUtils;

public class BackupBootImageActivity extends BaseChildActivity implements TextWatcher {
    private TextInputLayout mDevPath;
    private AppCompatSpinner spinner;
    private TextInputLayout outfile;
    private AppCompatButton mDoBackup;

    public static final String OUTPUT_PREFIX = "Kernel_Backup_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_boot_image);
        applyToolbar(R.id.toolbar);
        spinner = (AppCompatSpinner) findViewById(R.id.activity_backup_sp_type);
        outfile = (TextInputLayout) findViewById(R.id.backup_output_filename);
        mDevPath = (TextInputLayout) findViewById(R.id.backup_partition_path);
        mDoBackup = (AppCompatButton) findViewById(R.id.perform_task);
        mDoBackup.setEnabled(false);
        outfile.getEditText().addTextChangedListener(this);
        mDevPath.getEditText().addTextChangedListener(this);
        outfile.getEditText().setText("");
        mDevPath.getEditText().setText("");
        spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                new DeviceGetter(BackupBootImageActivity.this, mDevPath.getEditText(), p3).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });
        spinner.setAdapter(getAdapter());
        outfile.getEditText().setText(OUTPUT_PREFIX + DeviceUtils.getSystemTime() + ".img");
        mDoBackup.setOnClickListener(new AppCompatButton.OnClickListener() {

            @Override
            public void onClick(View p1) {
                // TODO: Implement this method
                new DoBackup(new File(mDevPath.getEditText().getText().toString()), new File(ImageFactory.DATA_PATH, outfile.getEditText().getText().toString())).execute();
            }
        });
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
        adLayout.addView(adView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayAdapter getAdapter() {
        return new ArrayAdapter(BackupBootImageActivity.this, android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.partition_type));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(outfile.getEditText().getText().toString().trim())) {
            outfile.setErrorEnabled(true);
            outfile.setError(getString(R.string.output_filename_cannot_be_empty));
        } else {
            outfile.setError(null);
            outfile.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(mDevPath.getEditText().getText().toString().trim())) {
            mDevPath.setErrorEnabled(true);
            mDevPath.setError(getString(R.string.source_file_cannot_be_empty));
        } else {
            mDevPath.setError(null);
            mDevPath.setErrorEnabled(false);
        }
        if (!mDevPath.isErrorEnabled() && !outfile.isErrorEnabled()) {
            mDoBackup.setEnabled(true);
        } else {
            mDoBackup.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class DoBackup extends AsyncTask<Void, Void, File> {
        private File dev;
        private File out;
        private ProgressDialog dialog;

        public DoBackup(File dev, File out) {
            this.dev = dev;
            this.out = out;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(BackupBootImageActivity.this);
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setCancelable(false);
            dialog.setMessage(getString(R.string.backuping));
            dialog.show();
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            dialog.dismiss();
            if (file != null) {
                Dialog.create(BackupBootImageActivity.this).setTitle(R.string.backup_success)
                        .setMessage(String.format(getString(R.string.backuped_to_file), out.getPath())).setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeviceUtils.openFile(BackupBootImageActivity.this, out);
                    }
                }).setNegativeButton(android.R.string.cancel, null).show();
                ImageFactory.show(BackupBootImageActivity.this);
            } else {
                Toast.makeShortText(getString(R.string.operation_failed));
            }
        }

        @Override
        protected File doInBackground(Void... params) {
            if (FlashUtils.backup(dev, out)) return out;
            return null;
        }
    }
}

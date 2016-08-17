package crixec.app.imagefactory.activity;

import android.app.ProgressDialog;
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

import java.io.File;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.function.bootimage.DeviceGetter;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.FileUtils;
import crixec.app.imagefactory.util.FlashUtils;


public class FlashBootImageActivity extends BaseChildActivity implements TextWatcher {


    private TextInputLayout etBootimg;
    private TextInputLayout etTarget;
    private AppCompatButton btDoFlash;
    private AppCompatSpinner spinner;
    private AppCompatButton btSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_boot_image);
        applyToolbar(R.id.toolbar);
        etBootimg = (TextInputLayout) findViewById(R.id.filename);
        etTarget = (TextInputLayout) findViewById(R.id.target_partition);
        btDoFlash = (AppCompatButton) findViewById(R.id.perform_task);
        btDoFlash.setEnabled(false);
        btSelect = (AppCompatButton) findViewById(R.id.select_file);
        btSelect.setOnClickListener(new AppCompatButton.OnClickListener() {

            @Override
            public void onClick(View p1) {
                // TODO: Implement this method
                new FileChooseDialog(FlashBootImageActivity.this).
                        choose("boot.img & recovery.img", new FileChooseDialog.Callback() {

                            @Override
                            public void onSelected(File file) {
                                // TODO: Implement this method
                                etBootimg.getEditText().setText(file.getAbsolutePath());
                            }
                        });
            }
        });
        etBootimg.getEditText().addTextChangedListener(this);
        etTarget.getEditText().addTextChangedListener(this);
        etBootimg.getEditText().setText("");
        etTarget.getEditText().setText("");
        spinner = (AppCompatSpinner) findViewById(R.id.activity_flash_sp_type);
        spinner.setAdapter(getAdapter());
        spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                new DeviceGetter(FlashBootImageActivity.this, etTarget.getEditText(), p3).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });
        spinner.setAdapter(getAdapter());
        btDoFlash.setOnClickListener(new AppCompatButton.OnClickListener() {

            @Override
            public void onClick(View p1) {
                // TODO: Implement this method
                new DoFlash(new File(etBootimg.getEditText().getText().toString()), new File(etTarget.getEditText().getText().toString())).execute();
            }
        });
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
        return new ArrayAdapter(FlashBootImageActivity.this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.partition_type));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(etBootimg.getEditText().getText().toString().trim())) {
            etBootimg.setErrorEnabled(true);
            etBootimg.setError(getString(R.string.input_filename_cannot_be_empty));
        } else if (!FileUtils.isFileExists(new File(etBootimg.getEditText().getText().toString()))) {
            etBootimg.setErrorEnabled(true);
            etBootimg.setError(getString(R.string.source_file_not_exists));
        } else {
            etBootimg.setError(null);
            etBootimg.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(etTarget.getEditText().getText().toString().trim())) {
            etTarget.setErrorEnabled(true);
            etTarget.setError(getString(R.string.source_file_cannot_be_empty));
        } else {
            etTarget.setError(null);
            etTarget.setErrorEnabled(false);
        }
        if (!etBootimg.isErrorEnabled() && !etBootimg.isErrorEnabled()) {
            btDoFlash.setEnabled(true);
        } else {
            btDoFlash.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class DoFlash extends AsyncTask<Void, Void, File> {
        private File dev;
        private File file;
        private ProgressDialog dialog;

        public DoFlash(File file, File dev) {
            this.file = file;
            this.dev = dev;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(FlashBootImageActivity.this);
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setCancelable(false);
            dialog.setMessage(getString(R.string.flashing));
            dialog.show();
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            dialog.dismiss();
            if (file != null) {
                Dialog.create(FlashBootImageActivity.this)
                        .setTitle(R.string.succeed)
                        .setMessage(String.format(getString(R.string.flashed_file_from), file.getPath()))
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else {
                Toast.makeShortText(getString(R.string.operation_failed));
            }
        }

        @Override
        protected File doInBackground(Void... params) {
            if (FlashUtils.flash(file, dev))
                return file;
            return null;
        }
    }
}


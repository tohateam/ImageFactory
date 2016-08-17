package crixec.app.imagefactory.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import java.io.File;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.core.Invoker;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FileUtils;

/**
 * Created by crixec on 2016/8/3.
 */
public class UnpackBootImageActivity extends BaseChildActivity implements View.OnClickListener, TextWatcher {
    private TextInputLayout sourceBootImage;
    private TextInputLayout outputDirectory;
    private AppCompatButton selectFile;
    private AppCompatButton performTask;
    private String TAG = "UnpackBootImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpack_boot_image);
        applyToolbar(R.id.toolbar);
        sourceBootImage = (TextInputLayout) findViewById(R.id.unpack_boot_image_source_file_path);
        outputDirectory = (TextInputLayout) findViewById(R.id.unpack_boot_image_output_directory_name);
        selectFile = (AppCompatButton) findViewById(R.id.unpack_boot_image_select_file);
        selectFile.setOnClickListener(this);
        performTask = (AppCompatButton) findViewById(R.id.unpack_boot_image_perform_task);
        performTask.setOnClickListener(this);
        sourceBootImage.getEditText().addTextChangedListener(this);
        outputDirectory.getEditText().addTextChangedListener(this);
        sourceBootImage.getEditText().setText("");
        outputDirectory.getEditText().setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unpack_boot_image_select_file: {
                new FileChooseDialog(this).choose("boot.img & recovery.img", new FileChooseDialog.Callback() {
                    @Override
                    public void onSelected(File file) {
                        sourceBootImage.getEditText().setText(file.getAbsolutePath());
                        if (TextUtils.isEmpty(outputDirectory.getEditText().getText().toString().trim())) {
                            outputDirectory.getEditText().setText(new File(sourceBootImage.getEditText().getText().toString()).getName() + "_unpacked");
                        }
                    }
                });
                break;
            }
            case R.id.unpack_boot_image_perform_task: {
                new UnpackbootimgTask(sourceBootImage.getEditText().getText().toString(), outputDirectory.getEditText().getText().toString()).execute();
                performTask.setEnabled(false);
                break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        File sourceFile = new File(sourceBootImage.getEditText().getText().toString());
        File targetFile = new File(ImageFactory.DATA_PATH, outputDirectory.getEditText().getText().toString());

        if (TextUtils.isEmpty(outputDirectory.getEditText().getText().toString().trim())) {
            outputDirectory.setError(getString(R.string.output_directory_cannot_be_empty));
            outputDirectory.setErrorEnabled(true);
        } else if (FileUtils.isFileExists(targetFile)) {
            outputDirectory.setError(getString(R.string.output_directory_already_exists));
            outputDirectory.setErrorEnabled(true);
        } else {
            outputDirectory.setError(null);
            outputDirectory.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(sourceBootImage.getEditText().getText().toString().trim())) {
            sourceBootImage.setError(getString(R.string.source_file_cannot_be_empty));
            sourceBootImage.setErrorEnabled(true);
        } else if (!FileUtils.isFileExists(sourceFile)) {
            sourceBootImage.setError(getString(R.string.source_file_not_exists));
            sourceBootImage.setErrorEnabled(true);
        } else {
            sourceBootImage.setError(null);
            sourceBootImage.setErrorEnabled(false);
        }
        if (!outputDirectory.isErrorEnabled() && !sourceBootImage.isErrorEnabled()) {
            performTask.setEnabled(true);
        } else {
            performTask.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    class UnpackbootimgTask extends AsyncTask<String, String, Boolean> {

        private String outputDirName;
        private String bootImageFile;
        private ProgressDialog dialog;

        public UnpackbootimgTask(String bootImage, String outputDir) {
            this.outputDirName = outputDir;
            this.bootImageFile = bootImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UnpackBootImageActivity.this);
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setCancelable(false);
            dialog.setMessage(getString(R.string.unpacking));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            super.onPostExecute(msg);
            final File file = new File(ImageFactory.DATA_PATH, outputDirName);
            if (msg) {
                Dialog.create(UnpackBootImageActivity.this)
                        .setTitle(R.string.succeed)
                        .setMessage(String.format(getString(R.string.unpacked_to_directory), file.getPath()))
                        .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeviceUtils.openFolder(UnpackBootImageActivity.this, file);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .show();
            } else {
                Toast.makeShortText(getString(R.string.operation_failed));
            }
            dialog.dismiss();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            File bootImage = new File(bootImageFile);
            File outputDir = new File(ImageFactory.DATA_PATH, outputDirName);
            Debug.i(TAG, "bootImage=" + bootImage.getPath() + " outputDir=" + outputDir.getPath());
            if (!Invoker.unpackbootimg(bootImage, outputDir)) {
                return false;
            }
            File ramdiskCpioGz = new File(outputDir, "ramdisk.cpio.gz");
            if (!Invoker.decompressGzip(ramdiskCpioGz)) {
                Debug.i(TAG, "decompress ramdisk.cpio.gz failure");
                return false;
            }
            Debug.i(TAG, "decompress ramdisk.cpio.gz successful");
            File ramdiskCpio = new File(outputDir, "ramdisk.cpio");
            File ramdiskDir = new File(outputDir, "ramdisk");
            if (!Invoker.uncpio(ramdiskCpio, ramdiskDir)) {
                Debug.i(TAG, "decompress ramdisk.cpio failure");
                return false;
            }
            Debug.i(TAG, "decompress ramdisk.cpio successful");
            return true;
        }

    }
}

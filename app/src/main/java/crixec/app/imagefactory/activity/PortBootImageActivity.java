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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

import java.io.File;
import java.util.ArrayList;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.core.Invoker;

/**
 * Created by crixec on 2016/8/3.
 */
public class PortBootImageActivity extends BaseChildActivity implements TextWatcher {

    private AppCompatSpinner baseSpinner;
    private AppCompatSpinner sampleSpinner;
    private TextInputLayout outfile;
    private AppCompatButton btRepack;
    private ArrayAdapter adapter = null;
    private ArrayList<String> list;
    private String TAG = "PortBootImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_boot_image);
        applyToolbar(R.id.toolbar);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter(PortBootImageActivity.this, android.R.layout.simple_spinner_dropdown_item, list);
        baseSpinner = (AppCompatSpinner) findViewById(R.id.base_spinner);
        sampleSpinner = (AppCompatSpinner) findViewById(R.id.sample_spinner);
        baseSpinner.setAdapter(adapter);
        sampleSpinner.setAdapter(adapter);
        outfile = (TextInputLayout) findViewById(R.id.repack_boot_image_output_file_name);
        outfile.getEditText().addTextChangedListener(this);
        btRepack = (AppCompatButton) findViewById(R.id.perform_task);
        btRepack.setOnClickListener(new AppCompatButton.OnClickListener() {

            @Override
            public void onClick(View p1) {
                // TODO: Implement this method
                File base = new File(ImageFactory.DATA_PATH, list.get(baseSpinner.getSelectedItemPosition()));
                File sample = new File(ImageFactory.DATA_PATH, list.get(sampleSpinner.getSelectedItemPosition()));
                String name = outfile.getEditText().getText().toString();
                new PortTask(base, sample, name).execute();

            }
        });
        outfile.getEditText().setText("");
        new LoadUnpacked().execute();
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
        LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
        adLayout.addView(adView);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String name = outfile.getEditText().getText().toString();
        if (TextUtils.isEmpty(name.trim())) {
            outfile.setErrorEnabled(true);
            outfile.setError(getString(R.string.output_filename_cannot_be_empty));
            btRepack.setEnabled(false);
        } else {
            if (baseSpinner.getSelectedItemPosition() > -1 && sampleSpinner.getSelectedItemPosition() > -1) {
                outfile.setError(null);
                outfile.setErrorEnabled(false);
                btRepack.setEnabled(true);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class LoadUnpacked extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PortBootImageActivity.this);
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();
            list.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            File dir = ImageFactory.DATA_PATH;
            if (dir == null || !dir.exists()) return null;
            for (File folder : dir.listFiles()) {
                File f = new File(folder, ".config");
                if (f.exists()) {
                    list.add(folder.getName());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    class PortTask extends AsyncTask<String, String, Boolean> {

        private File baseDir;
        private File sampleDir;
        private String outputFilename;
        private ProgressDialog dialog;

        public PortTask(File baseDir, File sampleDir, String outputFilename) {
            this.baseDir = baseDir;
            this.sampleDir = sampleDir;
            this.outputFilename = outputFilename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(PortBootImageActivity.this);
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setCancelable(false);
            dialog.setMessage(getString(R.string.porting));
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            super.onPostExecute(msg);
            if (msg) {
                final File file = new File(ImageFactory.DATA_PATH, outputFilename);
                Dialog.create(PortBootImageActivity.this)
                        .setTitle(R.string.succeed)
                        .setMessage(String.format(getString(R.string.repacked_to_file), file.getPath()))
                        .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeviceUtils.openFile(PortBootImageActivity.this, file);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setCancelable(false)
                        .show();
                ImageFactory.show(PortBootImageActivity.this);
            } else {
                Toast.makeShortText(getString(R.string.operation_failed));
            }
            dialog.dismiss();
        }


        @Override
        protected Boolean doInBackground(String... params) {
            File[] files = new File[]{
                    new File(baseDir, ".config"),
                    new File(baseDir, "zImage"),
                    new File(baseDir, "second"),
                    new File(sampleDir, "cpio.list"),
                    new File(sampleDir, "ramdisk"),
                    new File(baseDir, "dt.img"),
                    new File(ImageFactory.DATA_PATH, outputFilename)
            };
            for (int i = 0; i < 6; i++) {
                if (!files[i].exists()) {
                    Debug.i(TAG, "cannot find : " + files[i].getPath());
                    return false;
                }
            }
            File ramdiskCpio = new File(sampleDir, "ramdisk.cpio");
            if (!Invoker.mkcpio(files[3], ramdiskCpio)) {
                Debug.i(TAG, "compress ramdisk.cpio failure");
                return false;
            }
            Debug.i(TAG, "compress ramdisk.cpio successful");
            File ramdiskCpioGz = new File(sampleDir, "ramdisk.cpio.gz");
            if (!Invoker.compressGzip(ramdiskCpio)) {
                Debug.i(TAG, "compress ramdisk.cpio.gz failure");
                return false;
            }
            Debug.i(TAG, "compress ramdisk.cpio.gz successful");
            if (!Invoker.mkbootimg(files[0], files[1], ramdiskCpioGz, files[2], files[5], files[6])) {
                return false;
            }
            return true;

        }

    }

}

package crixec.app.imagefactory.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.BaseActivity;
import crixec.app.imagefactory.core.Debug;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.core.Invoker;
import crixec.app.imagefactory.ui.TerminalDialog;
import crixec.app.imagefactory.util.DeviceUtils;

/**
 * Created by crixec on 16-8-24.
 */
public class PortBootImageFragment extends BaseFragment implements TextWatcher {
    private AppCompatSpinner baseSpinner;
    private AppCompatSpinner sampleSpinner;
    private TextInputLayout outfile;
    private AppCompatButton btRepack;
    private ArrayAdapter<String> adapter = null;
    private ArrayList<String> list;
    private String TAG = "PortBootImageActivity";

    public static BaseFragment newInstance(BaseActivity activity) {
        PortBootImageFragment fragment = new PortBootImageFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = getContentView();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_port_boot_image, container, false);
            setContentView(root);
            list = new ArrayList<>();
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
            baseSpinner = (AppCompatSpinner) findViewById(R.id.base_spinner);
            sampleSpinner = (AppCompatSpinner) findViewById(R.id.sample_spinner);
            baseSpinner.setAdapter(adapter);
            sampleSpinner.setAdapter(adapter);
            outfile = (TextInputLayout) findViewById(R.id.repack_boot_image_output_file_name);
            btRepack = (AppCompatButton) findViewById(R.id.perform_task);
            btRepack.setOnClickListener(new AppCompatButton.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    // TODO: Implement this method
                    File base = new File(ImageFactory.KERNEL_UNPACKED, list.get(baseSpinner.getSelectedItemPosition()));
                    File sample = new File(ImageFactory.KERNEL_UNPACKED, list.get(sampleSpinner.getSelectedItemPosition()));
                    String name = outfile.getEditText().getText().toString();
                    new PortTask(base, sample, name).execute();

                }
            });
            outfile.getEditText().addTextChangedListener(this);
            new LoadUnpacked().execute();
        }
        return root;
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
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCancelable(false);
            dialog.show();
            list.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            File dir = ImageFactory.KERNEL_UNPACKED;
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
        private TerminalDialog dialog;

        public PortTask(File baseDir, File sampleDir, String outputFilename) {
            this.baseDir = baseDir;
            this.sampleDir = sampleDir;
            this.outputFilename = outputFilename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new TerminalDialog(getActivity());
            dialog.setTitle(R.string.porting);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean msg) {
            super.onPostExecute(msg);
            final File file = new File(ImageFactory.KERNEL_UNPACKED, outputFilename);
            if (msg) {
                dialog.writeStdout(String.format(getString(R.string.repacked_to_file), file.getPath()));
                dialog.setSecondButton(R.string.browse, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeviceUtils.openFile(getActivity(), file);
                    }
                });
            } else {
                dialog.writeStderr(String.format(getString(R.string.operation_failed), file.getPath()));
            }
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
                    new File(ImageFactory.KERNEL_UNPACKED, outputFilename)
            };
            for (int i = 0; i < 6; i++) {
                if (!files[i].exists()) {
                    Debug.i(TAG, "cannot find : " + files[i].getPath());
                    return false;
                }
            }
            File ramdiskCpio = new File(sampleDir, "ramdisk.cpio");
            if (!Invoker.mkcpio(files[3], ramdiskCpio, dialog)) {
                Debug.i(TAG, "compress ramdisk.cpio failure");
                return false;
            }
            Debug.i(TAG, "compress ramdisk.cpio successful");
            File ramdiskCpioGz = new File(sampleDir, "ramdisk.cpio.gz");
            if (!Invoker.compressGzip(ramdiskCpio, dialog)) {
                Debug.i(TAG, "compress ramdisk.cpio.gz failure");
                return false;
            }
            Debug.i(TAG, "compress ramdisk.cpio.gz successful");
            return Invoker.mkbootimg(files[0], files[1], ramdiskCpioGz, files[2], files[5], files[6], dialog);

        }

    }

}

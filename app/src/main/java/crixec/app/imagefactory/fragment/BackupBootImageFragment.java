package crixec.app.imagefactory.fragment;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.File;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.BaseActivity;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.TerminalDialog;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FlashUtils;
import crixec.app.imagefactory.util.bootimage.DeviceGetter;

/**
 * Created by crixec on 16-8-24.
 */
public class BackupBootImageFragment extends BaseFragment implements TextWatcher {
    private TextInputLayout mDevPath;
    private AppCompatSpinner spinner;
    private TextInputLayout outfile;
    private AppCompatButton mDoBackup;

    public static BaseFragment newInstance(BaseActivity activity) {
        BackupBootImageFragment fragment = new BackupBootImageFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = getContentView();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_backup_boot_image, container, false);
            setContentView(root);
            spinner = (AppCompatSpinner) findViewById(R.id.activity_backup_sp_type);
            outfile = (TextInputLayout) findViewById(R.id.backup_output_filename);
            mDevPath = (TextInputLayout) findViewById(R.id.backup_partition_path);
            mDoBackup = (AppCompatButton) findViewById(R.id.perform_task);
            mDoBackup.setEnabled(false);
            outfile.getEditText().addTextChangedListener(this);
            mDevPath.getEditText().addTextChangedListener(this);
            spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                    // TODO: Implement this method
                    new DeviceGetter(getActivity(), mDevPath.getEditText(), p3).execute();

                }

                @Override
                public void onNothingSelected(AdapterView<?> p1) {
                    // TODO: Implement this method
                }
            });
            spinner.setAdapter(getAdapter());
            mDoBackup.setOnClickListener(new AppCompatButton.OnClickListener() {

                @Override
                public void onClick(View p1) {
                    // TODO: Implement this method
                    new DoBackup(new File(mDevPath.getEditText().getText().toString()), new File(ImageFactory.KERNEL_BACKUPS, outfile.getEditText().getText().toString())).execute();
                }
            });
        }
        return root;
    }

    public ArrayAdapter getAdapter() {
        return new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item,
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
        private TerminalDialog dialog;

        public DoBackup(File dev, File out) {
            this.dev = dev;
            this.out = out;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new TerminalDialog(getActivity());
            dialog.setTitle(R.string.backuping);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final File file) {
            super.onPostExecute(file);
            if (file != null) {
                dialog.writeStdout(String.format(getString(R.string.backup_to_file), file.getPath()));
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
        protected File doInBackground(Void... params) {
            return FlashUtils.backup(dev, out, dialog) ? out : dev;
        }
    }
}

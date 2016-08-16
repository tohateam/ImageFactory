package crixec.app.imagefactory.function.convertimage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FileUtils;
import crixec.app.imagefactory.core.Invoker;

public class Sdat2imgFragment extends Fragment implements View.OnClickListener, TextWatcher {
    private View root;
    private AppCompatButton selectTransfer;
    private AppCompatButton selectDat;
    private AppCompatButton perfromTask;
    private TextInputLayout outputFile;
    private TextInputLayout transferPath;
    private TextInputLayout datPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: Implement this method
        if (root == null) {
            root = inflater.inflate(R.layout.layout_sdat2img, container, false);
            selectTransfer = (AppCompatButton) findViewById(R.id.sdat2img_select_transferl_list);
            selectDat = (AppCompatButton) findViewById(R.id.sdat2img_select_sysdat_image);
            datPath = (TextInputLayout) findViewById(R.id.sdat2img_sysdat_image_path);
            transferPath = (TextInputLayout) findViewById(R.id.sdat2img_transfer_path);
            outputFile = (TextInputLayout) findViewById(R.id.sdat2img_sysdat_image_output_name);
            perfromTask = (AppCompatButton) findViewById(R.id.sdat2img_sysdat_image_perform_task);
            selectDat.setOnClickListener(this);
            selectTransfer.setOnClickListener(this);
            perfromTask.setOnClickListener(this);
            datPath.getEditText().addTextChangedListener(this);
            transferPath.getEditText().addTextChangedListener(this);
            outputFile.getEditText().addTextChangedListener(this);
            datPath.getEditText().setText("");
            transferPath.getEditText().setText("");
            outputFile.getEditText().setText("");
        }
        return root;
    }

    public View findViewById(int id) {
        return root.findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdat2img_select_sysdat_image:
                new FileChooseDialog(getActivity()).choose("system.new.dat", new FileChooseDialog.Callback() {
                    @Override
                    public void onSelected(File file) {
                        datPath.getEditText().setText(file.getPath());
                    }
                });
                break;
            case R.id.sdat2img_select_transferl_list:
                new FileChooseDialog(getActivity()).choose("system.transfer.list", new FileChooseDialog.Callback() {
                    @Override
                    public void onSelected(File file) {
                        transferPath.getEditText().setText(file.getPath());
                    }
                });
                break;
            case R.id.sdat2img_sysdat_image_perform_task:
                File transferFile = new File(getText(transferPath));
                File datFile = new File(getText(datPath));
                File outFile = new File(ImageFactory.DATA_PATH, getText(outputFile));
                new DoConvert(transferFile, datFile, outFile).execute();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        File dat = new File(getText(datPath));
        File list = new File(getText(transferPath));
        if (TextUtils.isEmpty(getText(outputFile).trim())) {
            outputFile.setErrorEnabled(true);
            outputFile.setError(getString(R.string.output_filename_cannot_be_empty));
        } else {
            outputFile.setError(null);
            outputFile.setErrorEnabled(false);
        }
        if (!FileUtils.isFileExists(dat)) {
            datPath.setErrorEnabled(true);
            datPath.setError(getString(R.string.source_file_not_exists));
        } else if (TextUtils.isEmpty(getText(datPath).trim())) {
            datPath.setErrorEnabled(true);
            datPath.setError(getString(R.string.input_filename_cannot_be_empty));
        } else {
            datPath.setError(null);
            datPath.setErrorEnabled(false);
        }
        if (!FileUtils.isFileExists(list)) {
            transferPath.setErrorEnabled(true);
            transferPath.setError(getString(R.string.source_file_not_exists));
        } else if (TextUtils.isEmpty(getText(transferPath).trim())) {
            transferPath.setErrorEnabled(true);
            transferPath.setError(getString(R.string.input_filename_cannot_be_empty));
        } else {
            transferPath.setError(null);
            transferPath.setErrorEnabled(false);
        }
        if (!outputFile.isErrorEnabled() && !datPath.isErrorEnabled() && !transferPath.isErrorEnabled()) {
            perfromTask.setEnabled(true);
        } else {
            perfromTask.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getText(TextInputLayout inputLayout) {
        return inputLayout.getEditText().getText().toString();
    }

    class DoConvert extends AsyncTask<Void, Void, File> {
        private File transferFile;
        private File datFile;
        private File outputFile;
        private ProgressDialog dialog;

        public DoConvert(File transferFile, File datFile, File outputFile) {
            this.outputFile = outputFile;
            this.transferFile = transferFile;
            this.datFile = datFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setMessage(getString(R.string.converting));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final File file) {
            super.onPostExecute(file);
            dialog.dismiss();
            if (file != null) {
                Dialog.create(getActivity()).setTitle(R.string.succeed).setMessage(String.format(getString(R.string.converted_to_file), file.getPath()))
                        .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeviceUtils.openFile(getActivity(), file);
                            }
                        })
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
                ImageFactory.show(getActivity());
            } else {
                Toast.makeShortText(getString(R.string.operation_failed));
            }
        }

        @Override
        protected File doInBackground(Void... params) {
            if (Invoker.sdat2img(transferFile, datFile, outputFile)) {
                return outputFile;
            }
            return null;
        }
    }

}


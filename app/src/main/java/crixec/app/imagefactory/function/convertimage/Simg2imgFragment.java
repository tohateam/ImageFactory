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
import crixec.app.imagefactory.core.Invoker;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FileUtils;

public class Simg2imgFragment extends Fragment implements TextWatcher {
    private View root;
    private TextInputLayout sparseImage;
    private TextInputLayout rawImage;
    private AppCompatButton selectFile;
    private AppCompatButton performTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// TODO: Implement this method
        if (root == null) {
            root = inflater.inflate(R.layout.layout_simg2img, container, false);
            sparseImage = (TextInputLayout) findViewById(R.id.simg2img_image_path);
            rawImage = (TextInputLayout) findViewById(R.id.simg2img_output_name);
            sparseImage.getEditText().addTextChangedListener(this);
            rawImage.getEditText().addTextChangedListener(this);
            selectFile = (AppCompatButton) findViewById(R.id.simg2img_select_file);
            performTask = (AppCompatButton) findViewById(R.id.simg2img_perform_task);
            performTask.setEnabled(false);
            performTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DoConvert(new File(sparseImage.getEditText().getText().toString()), new File(ImageFactory.DATA_PATH, rawImage.getEditText().getText().toString())).execute();
                }
            });
            selectFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FileChooseDialog(getActivity()).choose(getString(R.string.sparse_image), new FileChooseDialog.Callback() {
                        @Override
                        public void onSelected(File file) {
                            sparseImage.getEditText().setText(file.getPath());
                        }
                    });
                }
            });
            sparseImage.getEditText().setText("");
            rawImage.getEditText().setText("");
        }

        return root;
    }

    public View findViewById(int id) {
        return root.findViewById(id);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        File file = new File(sparseImage.getEditText().getText().toString());
        if (TextUtils.isEmpty(rawImage.getEditText().getText().toString().trim())) {
            rawImage.setErrorEnabled(true);
            rawImage.setError(getString(R.string.output_filename_cannot_be_empty));
        } else {
            rawImage.setError(null);
            rawImage.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(sparseImage.getEditText().getText().toString().trim())) {
            sparseImage.setErrorEnabled(true);
            sparseImage.setError(getString(R.string.input_filename_cannot_be_empty));
        } else if (!FileUtils.isFileExists(file)) {
            sparseImage.setErrorEnabled(true);
            sparseImage.setError(getString(R.string.source_file_not_exists));
        } else {
            sparseImage.setError(null);
            sparseImage.setErrorEnabled(false);
        }
        if (!rawImage.isErrorEnabled() && !sparseImage.isErrorEnabled()) {
            performTask.setEnabled(true);
        } else {
            performTask.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class DoConvert extends AsyncTask<Void, Void, File> {
        private File from;
        private File to;
        private ProgressDialog dialog;

        public DoConvert(File from, File to) {
            this.from = from;
            this.to = to;
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
            } else {
                Toast.makeShortText(getString(R.string.operation_failed));
            }
        }

        @Override
        protected File doInBackground(Void... params) {
            if (Invoker.simg2img(from, to)) {
                return to;
            }
            return null;
        }
    }
}

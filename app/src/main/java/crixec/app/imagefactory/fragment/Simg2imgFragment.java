package crixec.app.imagefactory.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.BaseActivity;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.core.Invoker;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.ui.TerminalDialog;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FileUtils;

public class Simg2imgFragment extends BaseFragment implements TextWatcher {
    private TextInputLayout sparseImage;
    private TextInputLayout rawImage;
    private AppCompatButton selectFile;
    private AppCompatButton performTask;

    public static BaseFragment newInstance(BaseActivity activity) {
        Simg2imgFragment fragment = new Simg2imgFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
// TODO: Implement this method
        View root = getContentView();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_simg2img, container, false);
            setContentView(root);
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
                    new DoConvert(new File(sparseImage.getEditText().getText().toString()), new File(ImageFactory.IMAGE_CONVERTED, rawImage.getEditText().getText().toString())).execute();
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
        }

        return root;
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
        private TerminalDialog dialog;

        public DoConvert(File from, File to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new TerminalDialog(getActivity());
            dialog.setTitle(R.string.converting);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final File file) {
            super.onPostExecute(file);
            if (file != null) {
                dialog.writeStdout(String.format(getString(R.string.converted_to_file), file.getPath()));
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
            return Invoker.simg2img(from, to, dialog) ? to : from;
        }
    }
}

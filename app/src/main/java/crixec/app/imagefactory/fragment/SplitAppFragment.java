package crixec.app.imagefactory.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

/**
 * Created by crixec on 2016/8/2.
 */
public class SplitAppFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private AppCompatButton selectFile;
    private AppCompatButton performTask;
    private TextInputLayout firmwarePath;
    private TextInputLayout outputPath;

    public static BaseFragment newInstance(BaseActivity activity) {
        SplitAppFragment fragment = new SplitAppFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = getContentView();
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_split_app, container, false);
            setContentView(rootView);
            selectFile = (AppCompatButton) findViewById(R.id.firmware_select_file);
            performTask = (AppCompatButton) findViewById(R.id.firmware_perform_task);
            firmwarePath = (TextInputLayout) findViewById(R.id.firmware_file_path);
            outputPath = (TextInputLayout) findViewById(R.id.firmware_output_path);
            selectFile.setOnClickListener(this);
            performTask.setOnClickListener(this);
            firmwarePath.getEditText().addTextChangedListener(this);
            outputPath.getEditText().addTextChangedListener(this);
            performTask.setEnabled(false);
            firmwarePath.getEditText().setText("");
            outputPath.getEditText().setText("");

        }
        return rootView;
    }


    @Override
    public void onClick(View v) {
        String name = "UPDATE.APP";

        switch (v.getId()) {
            case R.id.firmware_select_file:
                new FileChooseDialog(getActivity()).choose(name, new FileChooseDialog.Callback() {
                    @Override
                    public void onSelected(File file) {
                        firmwarePath.getEditText().setText(file.getPath());
                    }
                });
                break;
            case R.id.firmware_perform_task:
                new DoExtract(new File(getText(firmwarePath)), new File(ImageFactory.IMAGE_CONVERTED, getText(outputPath))).execute();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        File file = new File(getText(firmwarePath));
        if (!FileUtils.isFileExists(file)) {
            firmwarePath.setErrorEnabled(true);
            firmwarePath.setError(getString(R.string.source_file_not_exists));
        } else if (TextUtils.isEmpty(getText(firmwarePath).trim())) {
            firmwarePath.setErrorEnabled(true);
            firmwarePath.setError(getString(R.string.input_filename_cannot_be_empty));
        } else {
            firmwarePath.setError(null);
            firmwarePath.setErrorEnabled(false);
        }
        if (TextUtils.isEmpty(getText(outputPath).trim())) {
            outputPath.setError(getString(R.string.output_directory_cannot_be_empty));
            outputPath.setErrorEnabled(true);
        } else {
            outputPath.setError(null);
            outputPath.setErrorEnabled(false);
        }
        if (!firmwarePath.isErrorEnabled() && !outputPath.isErrorEnabled()) {
            performTask.setEnabled(true);
        } else {
            performTask.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public String getText(TextInputLayout inputLayout) {
        return inputLayout.getEditText().getText().toString();
    }

    class DoExtract extends AsyncTask<Void, Void, File> {
        private File from;
        private File to;
        private TerminalDialog dialog;

        public DoExtract(File from, File to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new TerminalDialog(getActivity());
            dialog.setTitle(R.string.extracting);
            dialog.show();
        }

        @Override
        protected void onPostExecute(final File file) {
            super.onPostExecute(file);
            if (file != null) {
                dialog.writeStdout(String.format(getString(R.string.extracted_to_directory), file.getPath()));
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
            return Invoker.unpackapp(from, to, dialog) ? to : from;
        }
    }

}

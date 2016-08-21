package crixec.app.imagefactory.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.ShellUtils;

/**
 * Created by crixec on 16-8-20.
 */
public class TerminalDialog extends AppCompatDialog implements View.OnClickListener, ShellUtils.Result {
    private Context context;
    private AppCompatButton close;
    private AppCompatButton second;
    private AppCompatTextView output;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private AppCompatTextView title;
    private boolean isFinish = true;
    private SpannableStringBuilder stringBuilder = new SpannableStringBuilder();


    public TerminalDialog(Context context) {
        super(context, R.style.AppTheme_Terminal_Dialog);
        setContentView(R.layout.layout_terminal);
        this.context = context;
        setCancelable(false);
        output = (AppCompatTextView) findViewById(R.id.content);
        scrollView = (ScrollView) findViewById(R.id.content_parent);
        close = (AppCompatButton) findViewById(R.id.close);
        second = (AppCompatButton) findViewById(R.id.second);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        title = (AppCompatTextView) findViewById(R.id.title);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (DeviceUtils.getScrenWidth() * 0.9);
        params.height = (int) (DeviceUtils.getScreenHeight() * 0.7);
        getWindow().setAttributes(params);
        close.setOnClickListener(this);
        output.setTypeface(Typeface.MONOSPACE);
        output.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24 / DeviceUtils.getScreenDensity());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.title.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        this.title.setText(titleId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close: {
                dismiss();
                break;
            }
        }
    }

    private void appendToOutput(final SpannableString str) {
        if (output == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stringBuilder.append(str);
                output.setText(stringBuilder);
                output.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        if (isFinish) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    public void setSecondButton(int textId, View.OnClickListener ocl) {
        second.setVisibility(View.VISIBLE);
        second.setText(textId);
        second.setOnClickListener(ocl);
    }

    private Activity getActivity() {
        return ((Activity) context);
    }

    public void clear() {
        stringBuilder.clear();
        output.setText("");
    }

    public void writeStdout(String text) {
        this.onStdout(text);
    }

    public void writeStderr(String text) {
        this.onStderr(text);
    }

    @Override
    public void onStdout(String text) {
        SpannableString str = new SpannableString(text + "\n");
        str.setSpan(new ForegroundColorSpan(getColor(R.color.Terminal_Stdout)), 0, text.length(), 0);
        appendToOutput(str);
    }

    @Override
    public void onStderr(String text) {
        SpannableString str = new SpannableString(text + "\n");
        str.setSpan(new ForegroundColorSpan(getColor(R.color.Terminal_Stderr)), 0, text.length(), 0);
        appendToOutput(str);
    }

    @Override
    public void onCommand(String command) {
        isFinish = false;
        SpannableString str = new SpannableString(command + "\n");
        str.setSpan(new ForegroundColorSpan(getColor(R.color.Terminal_Command)), 0, command.length(), 0);
        appendToOutput(str);
    }

    public int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, getActivity().getTheme());
        }
        return context.getResources().getColor(id);
    }

    @Override
    public void onFinish(final int resultCode) {
        isFinish = true;
        SpannableString str = new SpannableString("RESULT_CODE : " + resultCode + "\n");
        str.setSpan(new ForegroundColorSpan(getColor(R.color.Terminal_ResultCode)), 0, str.length(), 0);
        appendToOutput(str);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                close.setVisibility(View.VISIBLE);
                setCancelable(true);
            }
        });
    }
}

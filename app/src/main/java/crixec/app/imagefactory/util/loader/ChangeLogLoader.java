package crixec.app.imagefactory.util.loader;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;

import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.util.FileUtils;

/**
 * Created by Crixec on 2016/8/14.
 */

public class ChangeLogLoader extends AsyncTask<Void, Void, StringBuilder> {
    private TextView textView;

    public ChangeLogLoader(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        super.onPostExecute(stringBuilder);
        textView.setText(stringBuilder);
    }

    @Override
    protected StringBuilder doInBackground(Void... params) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(FileUtils.readInputStream(ImageFactory.getApp().getAssets().open("changelog.txt")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb;
    }
}

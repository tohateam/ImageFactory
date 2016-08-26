package crixec.app.imagefactory.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.BaseActivity;
import crixec.app.imagefactory.core.Debug;

/**
 * Created by crixec on 16-8-24.
 */
public class LogcatFragment extends BaseFragment {
    private AppCompatTextView content = null;

    public static BaseFragment newInstance(BaseActivity activity) {
        Sdat2imgFragment fragment = new Sdat2imgFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = getContentView();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_logcat, container, false);
            setContentView(root);
            content = (AppCompatTextView) findViewById(R.id.content);
            content.setVerticalScrollBarEnabled(true);
            new LogcatReader(content).execute();
        }
        return root;
    }

    class LogcatReader extends AsyncTask<Void, Void, StringBuilder> {
        private TextView textView;

        public LogcatReader(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected void onPostExecute(StringBuilder stringBuilder) {
            super.onPostExecute(stringBuilder);
            textView.setText(stringBuilder);
        }

        @Override
        protected StringBuilder doInBackground(Void... params) {
            return Debug.getContent();
        }
    }
}

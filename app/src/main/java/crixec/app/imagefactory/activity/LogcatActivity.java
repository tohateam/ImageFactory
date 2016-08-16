package crixec.app.imagefactory.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import net.youmi.android.spot.SpotManager;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.Debug;

public class LogcatActivity extends BaseChildActivity {

    private AppCompatTextView content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logcat);
        applyToolbar(R.id.toolbar);
        content = (AppCompatTextView) findViewById(R.id.content);
        content.setHorizontallyScrolling(true);
        content.setHorizontalScrollBarEnabled(true);
        content.setVerticalScrollBarEnabled(true);
        new LogcatReader(content).execute();
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

package crixec.app.imagefactory.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import crixec.app.imagefactory.R;

/**
 * Created by crixec on 2016/8/3.
 */
public class BaseChildActivity extends BaseActivity {

    public void applyToolbar(int id) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getSupportActionBar() != null) {
            if (item.getItemId() == android.R.id.home) {
                this.finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

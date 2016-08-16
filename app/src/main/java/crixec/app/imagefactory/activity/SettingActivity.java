package crixec.app.imagefactory.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import crixec.app.imagefactory.R;

public class SettingActivity extends BaseChildActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        applyToolbar(R.id.toolbar);
        getFragmentManager().beginTransaction().replace(R.id.setting_content, new SettingFragment()).commit();
    }

}

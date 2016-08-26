package crixec.app.imagefactory.activity;

import android.os.Bundle;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.fragment.UnpackFirmwareFragment;
import crixec.app.imagefactory.fragment.Img2simgFragment;
import crixec.app.imagefactory.fragment.Sdat2imgFragment;
import crixec.app.imagefactory.fragment.Simg2imgFragment;

public class FunctionActivity extends BaseChildActivity {

    public static final String TYPE_SIMG2IMG = "simg2img";
    public static final String TYPE_IMG2SIMG = "img2simg";
    public static final String TYPE_SDAT2IMG = "sdat2img";
    public static final String TYPE_UNPACKAPP = "unpackapp";
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_image);
        applyToolbar(R.id.toolbar);
        setType(getIntent().getStringExtra("TYPE"));
        if (type.equals(TYPE_IMG2SIMG)) {
            setTitle("Img2simg");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Img2simgFragment.newInstance(this)).commit();
        } else if (type.equals(TYPE_SIMG2IMG)) {
            setTitle("Simg2img");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Simg2imgFragment.newInstance(this)).commit();
        } else if (type.equals(TYPE_SDAT2IMG)) {
            setTitle("Sdat2img");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Sdat2imgFragment.newInstance(this)).commit();
        } else if (type.equals(TYPE_UNPACKAPP)) {
            setTitle("Split APP");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, UnpackFirmwareFragment.newInstance(this)).commit();
        } else return;
    }

    public void setType(String type) {
        this.type = type;
    }
}

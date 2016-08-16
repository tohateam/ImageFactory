package crixec.app.imagefactory.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.adapter.AboutAdapter;
import crixec.app.imagefactory.ui.Dialog;

public class AboutActivity extends BaseChildActivity implements AdapterView.OnItemClickListener {


    private ListView listView;
    private AboutAdapter adapter;
    private List<Item> list = new ArrayList<Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        applyToolbar(R.id.toolbar);
        listView = (ListView) findViewById(R.id.about_listview);
        list.add(new Item("Crixec", "", "Email : 79236107@qq.com or crixec@gmail.com\nQQ : 79236107", "https://github.com/Crixec"));
        list.add(new Item("Android Open Source Project", "Google", "Android is an open source software stack for a wide range of mobile devices and a corresponding open source project led by Google. This site offers the information and source code you need to create custom variants of the Android stack, port devices and accessories to the Android platform, and ensure your devices meet compatibility requirements.", "https://source.android.com/"));
        list.add(new Item("Cyanogenmod", " CyanogenMod", "CyanogenMod is an aftermarket firmware for a number of cell phones based on the open-source Android operating system. It offers features not found in the official Android based firmwares of vendors.", "http://www.cyanogenmod.org/"));
        list.add(new Item("Android Support Library", "Google", "The Android Support Library offers a number of features that are not built into the framework. These libraries offer backward-compatible versions of new features, provide useful UI elements that are not included in the framework, and provide a range of utilities that apps can draw on.", "https://developer.android.com/topic/libraries/support-library/index.html"));
        list.add(new Item("unpackapp", "scue", "A tool for unpack huawei app file.", "https://github.com/scue/unpackapp"));
        list.add(new Item("unpackcpb", "scue", "A tool for unpack coolpad cpb file.", "https://github.com/scue/unpackcpb"));
        adapter = new AboutAdapter(this, list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinQQGroup();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String url = list.get(position).getUrl();
        Dialog.create(this).setTitle(R.string.browse_home_page)
                .setMessage(url)
                .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    public boolean joinQQGroup() {
        String key = "B4BqetfGvLXZyyIU6p-3qoqF9elTYx2A";
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static class Item {
        String text1;
        String text2;
        String text3;
        String url;

        public Item(String text1, String text2, String text3, String url) {
            this.text1 = text1;
            this.text2 = text2;
            this.text3 = text3;
            this.url = url;
        }

        public String getUrl() {
            return url;
        }


        public String getText1() {
            return text1;
        }


        public String getText2() {
            return text2;
        }

        public String getText3() {
            return text3;
        }

    }
}

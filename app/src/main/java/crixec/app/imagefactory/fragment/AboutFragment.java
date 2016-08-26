package crixec.app.imagefactory.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.BaseActivity;
import crixec.app.imagefactory.adapter.AboutAdapter;
import crixec.app.imagefactory.bean.AboutItem;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.util.DeviceUtils;

/**
 * Created by crixec on 16-8-24.
 */
public class AboutFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private AboutAdapter adapter;
    private List<AboutItem> list = new ArrayList<>();

    public static BaseFragment newInstance(BaseActivity activity) {
        AboutFragment fragment = new AboutFragment();
        fragment.setActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = getContentView();
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_about, container, false);
            setContentView(root);
            listView = (ListView) findViewById(R.id.about_listview);
            list.add(new AboutItem("Android Open Source Project", "Google", "Android is an open source software stack for a wide range of mobile devices and a corresponding open source project led by Google. This site offers the information and source code you need to create custom variants of the Android stack, port devices and accessories to the Android platform, and ensure your devices meet compatibility requirements.", "https://source.android.com/"));
            list.add(new AboutItem("Cyanogenmod", " CyanogenMod", "CyanogenMod is an aftermarket firmware for a number of cell phones based on the open-source Android operating system. It offers features not found in the official Android based firmwares of vendors.", "http://www.cyanogenmod.org/"));
            list.add(new AboutItem("Android Support Library", "Google", "The Android Support Library offers a number of features that are not built into the framework. These libraries offer backward-compatible versions of new features, provide useful UI elements that are not included in the framework, and provide a range of utilities that apps can draw on.", "https://developer.android.com/topic/libraries/support-library/index.html"));
            list.add(new AboutItem("unpackapp", "scue", "A tool for unpack huawei app file.", "https://github.com/scue/unpackapp"));
            adapter = new AboutAdapter(getActivity(), list);
            listView.setOnItemClickListener(this);
            listView.setAdapter(adapter);
            findViewById(R.id.me).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeviceUtils.openUrl(getActivity(), "http://crixec.top");
                }
            });
            findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinQQGroup();
                }
            });
        }
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String url = list.get(position).getUrl();
        Dialog.create(getActivity()).setTitle(R.string.browse_home_page)
                .setMessage(url)
                .setPositiveButton(R.string.browse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeviceUtils.openUrl(getActivity(), url);
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

}

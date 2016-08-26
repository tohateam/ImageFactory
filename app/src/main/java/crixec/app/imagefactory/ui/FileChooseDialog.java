package crixec.app.imagefactory.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.util.DeviceUtils;
import crixec.app.imagefactory.util.FileComparator;
import crixec.app.imagefactory.util.FileUtils;
import crixec.app.imagefactory.util.XmlDataUtils;

public class FileChooseDialog extends AppCompatDialog {
    private Context mContext;

    private ListView mListView;
    private AppCompatTextView mTextView;
    private File path;
    private ArrayList<File> list;
    private Callback mCallback;


    public FileChooseDialog(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.file_selector_view);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (DeviceUtils.getScrenWidth() * 0.9);
        params.height = (int) (DeviceUtils.getScreenHeight() * 0.9);
        getWindow().setAttributes(params);
    }

    public void choose(String title, Callback callback) {
        this.mCallback = callback;
        setTitle(title);
        if (XmlDataUtils.getString("LAST_PATH").equals("")) {
            XmlDataUtils.putString("LAST_PATH", "/");
        }
        String p = XmlDataUtils.getString("LAST_PATH");
        if (!new File(p).canRead()) {
            p = "/";
            XmlDataUtils.putString("LAST_PATH", "/");
        }
        path = new File(p);
        File selected = path;
        mListView = (ListView) findViewById(R.id.file_selector_view_listview);
        mTextView = (AppCompatTextView) findViewById(R.id.current_path);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(path.getPath());
        mListView.setAdapter(getAdapter(listDirectory(path)));
        mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                final File file = list.get(p3);
                if (file.isDirectory() && !file.canRead()) {
                    Toast.makeLongText(getString(R.string.no_permission_to_access));
                    return;
                } else if (file.isDirectory()) {
                    if (p3 == 0) {
                        path = (file.getParentFile() == null ? file : file.getParentFile());
                    } else {
                        path = file;
                    }
                    XmlDataUtils.putString("LAST_PATH", path.getAbsolutePath());
                    mListView.setAdapter(getAdapter(listDirectory(path)));
                } else if (file.isFile()) {
                    AlertDialog.Builder dialogBuilder = Dialog.create(mContext);
                    dialogBuilder.setTitle(getString(R.string.select_file))
                            .setMessage(file.getAbsolutePath())
                            .setPositiveButton(android.R.string.yes, new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    // TODO: Implement this method
                                    mCallback.onSelected(file);
                                    dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
                mTextView.setText(path.getPath());
            }
        });
        show();
    }

    public SimpleAdapter getAdapter(ArrayList<File> list) {
        ArrayList<HashMap<String, ?>> itemList = new ArrayList<HashMap<String, ?>>();
        for (int i = 0; i < list.size(); i++) {
            File file = list.get(i);
            int image = R.drawable.ic_file;
            if (file.isDirectory()) {
                image = R.drawable.ic_folder;
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (i == 0) {
                map.put("NAME", getString(R.string.upper_path));
                map.put("IMAGE", R.drawable.ic_up_foler);
                map.put("TIME", "");
            } else {
                map.put("NAME", file.getName());
                map.put("IMAGE", image);
                map.put("TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified())) + "  " + FileUtils.parseFileSize(file.length()));
            }
            itemList.add(map);
        }
        return new SimpleAdapter(mContext, itemList, R.layout.file_selector_view_item, new String[]{"IMAGE", "NAME", "TIME"}, new int[]{R.id.file_icon, R.id.file_name, R.id.file_detail});
    }

    public ArrayList<File> listDirectory(File dir) {
        list = new ArrayList<File>();
        File[] childFiles = dir.listFiles();
        for (File file : childFiles) {
            list.add(file);
        }
        Collections.sort(list, new FileComparator());
        list.add(0, dir);
        return list;
    }

    private CharSequence getString(int id) {
        // TODO: Implement this method
        return mContext.getString(id);
    }

    public static interface Callback {
        void onSelected(File file);
    }
}

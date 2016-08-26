package crixec.app.imagefactory.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AndroidRuntimeException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.adapter.CpioListAdapter;
import crixec.app.imagefactory.adapter.OnRecyclerViewItemClickListener;
import crixec.app.imagefactory.util.cpioeditor.CpioEntity;
import crixec.app.imagefactory.util.cpioeditor.CpioListGenerator;
import crixec.app.imagefactory.util.cpioeditor.CpioListParser;
import crixec.app.imagefactory.ui.Dialog;
import crixec.app.imagefactory.ui.FileChooseDialog;
import crixec.app.imagefactory.ui.Toast;

public class CpioEditorActivity extends BaseActivity implements OnRecyclerViewItemClickListener {

    private File cpioListFile;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CpioListAdapter adapter;
    private List<CpioEntity> datas = new ArrayList<>();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpio_editor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.create(CpioEditorActivity.this).setItems(R.array.cpio_new_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // new file
                            new FileChooseDialog(CpioEditorActivity.this).choose(getString(R.string.add_new_file), new FileChooseDialog.Callback() {
                                @Override
                                public void onSelected(File file) {
                                    final CpioEntity entity = new CpioEntity();
                                    entity.setPermission("0755");
                                    entity.setSource(file.getPath());
                                    entity.setGid("0");
                                    entity.setUid("0");
                                    entity.setTarget(file.getName());
                                    entity.setType(CpioListParser.ENTITY_FILE);
                                    final View view = getLayoutInflater().inflate(R.layout.layout_cpio_add_to_rootfs, null, false);
                                    final AppCompatEditText editText = (AppCompatEditText) view.findViewById(R.id.text1);
                                    Dialog.create(CpioEditorActivity.this).setView(view).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!TextUtils.isEmpty(editText.getText().toString())) {
                                                entity.setTarget(editText.getText().toString());
                                                editCpioItem(entity, false);
                                            }
                                        }
                                    }).setNegativeButton(android.R.string.cancel, null)
                                            .setCancelable(false)
                                            .setTitle(R.string.edit_target)
                                            .show();
                                }
                            });
                        } else if (which == 1) {
                            // new folder
                            final CpioEntity entity = new CpioEntity();
                            entity.setPermission("0755");
                            entity.setGid("0");
                            entity.setUid("0");
                            entity.setType(CpioListParser.ENTITY_DIR);
                            final View view = getLayoutInflater().inflate(R.layout.layout_cpio_add_to_rootfs, null, false);
                            final AppCompatEditText editText = (AppCompatEditText) view.findViewById(R.id.text1);
                            editText.setText("new folder");
                            Dialog.create(CpioEditorActivity.this).setView(view).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                                        entity.setTarget(editText.getText().toString());
                                        File file = new File(cpioListFile.getParentFile(), "ramdisk/" + entity.getTarget());
                                        file.mkdirs();
                                        editCpioItem(entity, false);
                                    }
                                }
                            }).setNegativeButton(android.R.string.cancel, null)
                                    .setCancelable(false)
                                    .setTitle(R.string.edit_target)
                                    .show();
                        }
                    }
                }).setTitle(R.string.new_cpio_item).setCancelable(true).show();
            }
        });
        String cpioListFilePath = getIntent().getData().getPath();
        if (!changeCpioListFile(cpioListFilePath)) {
            Toast.makeShortText("打开cpio.list失败");
            return;
        }
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new CpioListAdapter(getLayoutInflater(), datas, this);
        recyclerView.setAdapter(adapter);
        new LoadCpioList(cpioListFile).execute();

    }

    public boolean changeCpioListFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.canRead() && file.canWrite()) {
            cpioListFile = file;
            String path = cpioListFile.getPath();
            String sn = path.substring(0, path.lastIndexOf("/"));
            toolbar.setSubtitle(path.substring(sn.lastIndexOf("/") + 1, path.length()));
            return true;
        }
        return false;
    }

    public void saveCpioItems() {
        CpioListGenerator generator = CpioListGenerator.toFile(cpioListFile);
        try {
            generator.prepare();
            generator.writeAll(datas);
            generator.close();
        } catch (IOException e) {
            Toast.makeShortText(getString(R.string.operation_failed));
        }
        new LoadCpioList(cpioListFile).execute();
    }

    @Override
    public void onClick(View view, final int pos) {
        Dialog.create(this).setItems(R.array.cpio_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    editCpioItem(datas.get(pos), true);
                } else if (which == 1) {
                    datas.remove(pos);
                    saveCpioItems();
                }
            }
        }).setCancelable(true).show();
    }

    private void editCpioItem(final CpioEntity entity, final boolean isIn) {
        final View root = getLayoutInflater().inflate(R.layout.layout_cpio_item, null, false);
        final AppCompatTextView target = (AppCompatTextView) root.findViewById(R.id.cpio_target);
        final AppCompatTextView source = (AppCompatTextView) root.findViewById(R.id.cpio_source);
        final AppCompatEditText permission = (AppCompatEditText) root.findViewById(R.id.cpio_perimission);
        final AppCompatEditText gid = (AppCompatEditText) root.findViewById(R.id.cpio_gid);
        final AppCompatEditText uid = (AppCompatEditText) root.findViewById(R.id.cpio_uid);
        target.setText(entity.getTarget());
        source.setText(entity.getSource());
        permission.setText(entity.getPermission());
        gid.setText(entity.getGid());
        uid.setText(entity.getUid());
        Dialog.create(this)
                .setView(root)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(target.getText().toString()) && !TextUtils.isEmpty(permission.getText().toString()) && !TextUtils.isEmpty(gid.getText().toString()) && !TextUtils.isEmpty(uid.getText().toString())) {
                            entity.setTarget(target.getText().toString());
                            entity.setSource(source.getText().toString());
                            entity.setPermission(permission.getText().toString());
                            entity.setUid(uid.getText().toString());
                            entity.setGid(gid.getText().toString());
                            if (!isIn)
                                datas.add(entity);
                            saveCpioItems();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(true)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_save) {
            saveCpioItems();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cpio, menu);
        return true;
    }

    class LoadCpioList extends AsyncTask<Void, Void, Boolean> {
        private ProgressDialog dialog;
        private File file;

        public LoadCpioList(File file) {
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(CpioEditorActivity.this);
            dialog.setProgressStyle(R.style.ProgressBar);
            dialog.setMessage("Parsing cpio lsit file...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.dismiss();
            if (aBoolean) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeShortText("Parse cpio list file failed!");
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//
//            }
            CpioListParser parser = CpioListParser.fromFile(file);
            try {
                parser.parse();
                datas.clear();
                datas.addAll(parser.getEntities());
            } catch (IOException e) {
                throw new AndroidRuntimeException(e);
            }
            return true;
        }
    }

}

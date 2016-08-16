package crixec.app.imagefactory.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.activity.AboutActivity;

/**
 * Created by Crixec on 2016/8/12.
 */
public class AboutAdapter extends BaseAdapter {
    private List<AboutActivity.Item> list = new ArrayList<AboutActivity.Item>();
    private Context context;

    public AboutAdapter(Context context, List<AboutActivity.Item> list) {
        this.context = context;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_about_item, null, false);
            holder = new Holder();
            holder.text1 = (AppCompatTextView) convertView.findViewById(R.id.text1);
            holder.text2 = (AppCompatTextView) convertView.findViewById(R.id.text2);
            holder.text3 = (AppCompatTextView) convertView.findViewById(R.id.text3);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        AboutActivity.Item item = list.get(position);
        holder.text1.setText(item.getText1());
        holder.text2.setText(item.getText2());
        holder.text3.setText(item.getText3());
        return convertView;
    }

    private class Holder {
        AppCompatTextView text1;
        AppCompatTextView text2;
        AppCompatTextView text3;
    }
}
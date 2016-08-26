package crixec.app.imagefactory.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.util.cpioeditor.CpioEntity;
import crixec.app.imagefactory.util.cpioeditor.CpioListParser;

/**
 * Created by crixec on 16-8-22.
 */
public class CpioListAdapter extends BaseRecyclerAdapter {
    private List<CpioEntity> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private OnRecyclerViewItemClickListener onItemClickListener;

    public CpioListAdapter(LayoutInflater inflater, List<CpioEntity> datas, OnRecyclerViewItemClickListener onItemClickListener) {
        this.datas = datas;
        this.inflater = inflater;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.file_selector_view_item, parent, false);
        CpioViewHolder holder = new CpioViewHolder(view);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder mHolder, int position) {
        CpioEntity entity = datas.get(position);
        CpioViewHolder holder = (CpioViewHolder) mHolder;
        if (entity == null) return;
        if (entity.getType().equals(CpioListParser.ENTITY_DIR)) {
            holder.fileIcon.setImageResource(R.drawable.ic_folder);
        } else {
            holder.fileIcon.setImageResource(R.drawable.ic_file);
        }
        holder.fileName.setText(entity.getTarget());
        holder.fileDetail.setText(entity.getSource());
        holder.setWhich(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class CpioViewHolder extends BaseViewHolder {
        private AppCompatImageView fileIcon;
        private AppCompatTextView fileName;
        private AppCompatTextView fileDetail;

        public CpioViewHolder(final View itemView) {
            super(itemView);
            fileIcon = (AppCompatImageView) itemView.findViewById(R.id.file_icon);
            fileName = (AppCompatTextView) itemView.findViewById(R.id.file_name);
            fileDetail = (AppCompatTextView) itemView.findViewById(R.id.file_detail);
        }
    }

}

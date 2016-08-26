package crixec.app.imagefactory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by crixec on 16-8-23.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements OnRecyclerViewItemClickListener {
    private OnRecyclerViewItemClickListener onItemClickListener;
    private int which;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public int getWhich() {
        return which;
    }

    public void setWhich(int which) {
        this.which = which;
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseViewHolder.this.onClick(v, which);
            }
        });
    }

    @Override
    public void onClick(View view, int pos) {
        if (onItemClickListener != null)
            onItemClickListener.onClick(view, pos);
    }
}

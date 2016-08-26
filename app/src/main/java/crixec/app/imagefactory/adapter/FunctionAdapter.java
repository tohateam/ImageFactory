package crixec.app.imagefactory.adapter;

import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.R;
import crixec.app.imagefactory.bean.Function;

/**
 * Created by crixec on 16-8-23.
 */
public class FunctionAdapter extends BaseRecyclerAdapter {
    private List<Function> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private OnRecyclerViewItemClickListener onItemClickListener;

    public FunctionAdapter(LayoutInflater inflater, List<Function> datas, OnRecyclerViewItemClickListener onItemClickListener) {
        this.datas = datas;
        this.inflater = inflater;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_function_item, parent, false);
        FunctionViewHolder holder = new FunctionViewHolder(view);
        holder.setOnItemClickListener(onItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FunctionViewHolder mHolder = (FunctionViewHolder) holder;
        Function function = datas.get(position);
        if (function == null) return;
        mHolder.functionName.setText(function.getName());
        mHolder.functionDescription.setText(function.getDescription());
        mHolder.setWhich(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class FunctionViewHolder extends BaseViewHolder {
        private AppCompatTextView functionName;
        private AppCompatTextView functionDescription;

        public FunctionViewHolder(final View itemView) {
            super(itemView);
            functionName = (AppCompatTextView) itemView.findViewById(R.id.function_name);
            functionDescription = (AppCompatTextView) itemView.findViewById(R.id.function_description);
        }
    }
}

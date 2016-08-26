package crixec.app.imagefactory.adapter;

import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import crixec.app.imagefactory.function.cpioeditor.CpioEntity;

/**
 * Created by crixec on 16-8-23.
 */
public class FunctionAdapter extends BaseRecyclerAdapter{
    private List<CpioEntity> datas = new ArrayList<>();
    private LayoutInflater inflater;
    private OnRecyclerViewItemClickListener onItemClickListener;

    public FunctionAdapter(LayoutInflater inflater, List<CpioEntity> datas, OnRecyclerViewItemClickListener onItemClickListener) {
        this.datas = datas;
        this.inflater = inflater;
        this.onItemClickListener = onItemClickListener;
    }
}

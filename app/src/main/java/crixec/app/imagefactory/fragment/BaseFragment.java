package crixec.app.imagefactory.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import crixec.app.imagefactory.activity.BaseActivity;

/**
 * Created by crixec on 16-8-23.
 */
public class BaseFragment extends Fragment {
    private BaseActivity activity;
    private View contentView;

//    public static BaseFragment newInstance(BaseActivity activity) {
//        BaseFragment fragment = new BaseFragment();
//        fragment.setActivity(activity);
//        return fragment;
//    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    public View findViewById(int id) {
        return contentView == null ? null : contentView.findViewById(id);
    }

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public Context getContext() {
        return activity;
    }
}

package crixec.app.imagefactory.fragment;

import crixec.app.imagefactory.activity.BaseActivity;

/**
 * Created by crixec on 16-8-24.
 */
public class PortBootImageFragment extends BaseFragment{
    public static BaseFragment newInstance(BaseActivity activity) {
        PortBootImageFragment fragment = new PortBootImageFragment();
        fragment.setActivity(activity);
        return fragment;
    }
}

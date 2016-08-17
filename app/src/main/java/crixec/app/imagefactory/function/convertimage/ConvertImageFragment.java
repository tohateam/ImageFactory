package crixec.app.imagefactory.function.convertimage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import crixec.app.imagefactory.R;

/**
 * Created by crixec on 2016/8/2.
 */
public class ConvertImageFragment extends Fragment {

    private View rootView;
    private AppCompatSpinner spinner;
    private Simg2imgFragment mSimg2img;
    private Sdat2imgFragment mSdat2img;
    private Img2simgFragment mImg2simg;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null)
            rootView = inflater.inflate(R.layout.layout_convert_image, container, false);
        mSimg2img = new Simg2imgFragment();
        mSdat2img = new Sdat2imgFragment();
        mImg2simg = new Img2simgFragment();
        fragment = mSimg2img;
        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(getAdapter());
        getChildFragmentManager().beginTransaction().add(R.id.content, mSdat2img).hide(mSdat2img).add(R.id.content, mImg2simg).hide(mImg2simg).add(R.id.content, mSimg2img).commit();
        spinner.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                // TODO: Implement this method
                getChildFragmentManager().popBackStack();
                if (p3 == 0) {
                    getChildFragmentManager().beginTransaction().hide(fragment).show(mSimg2img).commit();
                    fragment = mSimg2img;
                } else if (p3 == 1) {
                    getChildFragmentManager().beginTransaction().hide(fragment).show(mImg2simg).commit();
                    fragment = mImg2simg;
                } else {
                    getChildFragmentManager().beginTransaction().hide(fragment).show(mSdat2img).commit();
                    fragment = mSdat2img;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> p1) {
                // TODO: Implement this method
            }
        });
        return rootView;
    }


    public View findViewById(int id) {
        return rootView.findViewById(id);
    }

    public ArrayAdapter getAdapter() {
        return new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.convert_image_items));
    }
}

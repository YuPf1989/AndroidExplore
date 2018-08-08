package com.rain.androidexplore.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/7 15:12
 * Description:
 */
public class AddViewFragment extends Fragment {
    private int mLayoutId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mLayoutId = args.getInt("layoutId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate =  inflater.inflate(R.layout.fragment_add_view, null);
        ViewStub viewStub = inflate.findViewById(R.id.add_view);
        viewStub.setLayoutResource(mLayoutId);
        viewStub.inflate();
        return inflate;
    }

    public static AddViewFragment newInstance(@LayoutRes int layoutId) {
        Bundle args = new Bundle();
        args.putInt("layoutId",layoutId);
        AddViewFragment fragment = new AddViewFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

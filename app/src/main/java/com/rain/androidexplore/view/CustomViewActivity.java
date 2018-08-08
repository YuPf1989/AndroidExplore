package com.rain.androidexplore.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/7 15:02
 * Description:
 */
public class CustomViewActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        findViewById(R.id.btn_add_circle_view).setOnClickListener(this);
        findViewById(R.id.btn_add_circle_view2).setOnClickListener(this);

        mFragmentManager = getSupportFragmentManager();

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_add_circle_view:
                transaction.replace(R.id.content, AddViewFragment.newInstance(R.layout.layout_circle_view));
                break;

            case R.id.btn_add_circle_view2:
                transaction.replace(R.id.content, AddViewFragment.newInstance(R.layout.layout_my_scroll_view));
                break;
        }

        transaction.commit();
    }
}

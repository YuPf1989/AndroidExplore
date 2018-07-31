package com.rain.androidexplore.about_activity_part1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.rain.androidexplore.R;
import com.rain.androidexplore.bean.UserManager;

/**
 * Author:rain
 * Date:2018/7/25 9:59
 * Description:
 * 1.演示MainActivity->TestActivity生命周期的变化
 */
public class TestActivity1 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestActivity1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        Log.e(TAG, "onCreate: uid:" + UserManager.uid);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("extra", "data");
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState: ");
        Log.e(TAG, "onRestoreInstanceState: savedInstanceState:" + savedInstanceState.getString("extra"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged: " + newConfig.orientation);
    }
}

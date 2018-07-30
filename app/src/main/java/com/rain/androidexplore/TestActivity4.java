package com.rain.androidexplore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rain.androidexplore.bean.User;

import java.io.Serializable;

/**
 * Author:rain
 * Date:2018/7/26 10:57
 * Description:
 */
public class TestActivity4 extends AppCompatActivity {
    private static final String TAG  = "TestActivity4";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra("data")) {
            User user = (User) getIntent().getSerializableExtra("data");

            Log.e(TAG, "onCreate: user:"+user.toString());
        }
    }
}

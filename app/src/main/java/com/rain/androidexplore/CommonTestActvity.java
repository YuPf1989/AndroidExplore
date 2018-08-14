package com.rain.androidexplore;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Author:rain
 * Date:2018/8/14 10:10
 * Description:
 * 用于公共的一般性的功能测试
 */
public class CommonTestActvity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_test);

        findViewById(R.id.btn_crash).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_crash:
                throw new RuntimeException("人为制造的异常，测试crashHandler");
        }
    }
}

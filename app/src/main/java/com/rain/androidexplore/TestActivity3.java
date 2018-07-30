package com.rain.androidexplore;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rain.androidexplore.bean.User;

/**
 * Author:rain
 * Date:2018/7/26 10:57
 * Description:
 */
public class TestActivity3 extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        findViewById(R.id.btn_activity4).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity4:
                User user = new User("alex", 20);
                startActivity(new Intent(this,TestActivity4.class).putExtra("data",user));
                break;
        }
    }
}

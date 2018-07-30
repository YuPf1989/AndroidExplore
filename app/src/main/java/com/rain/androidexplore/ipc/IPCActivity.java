package com.rain.androidexplore.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rain.androidexplore.R;
import com.rain.androidexplore.TestActivity3;
import com.rain.androidexplore.TestActivity4;

/**
 * Author:rain
 * Date:2018/7/26 10:49
 * Description:
 * IPC进程间通讯
 *
 */
public class IPCActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);

        findViewById(R.id.btn_activity3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity3:
                startActivity(new Intent(this, TestActivity3.class));
                break;

            case R.id.btn_activity4:
                startActivity(new Intent(this, TestActivity4.class));
                break;
        }
    }
}

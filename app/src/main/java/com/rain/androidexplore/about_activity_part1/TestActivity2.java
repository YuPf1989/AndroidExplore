package com.rain.androidexplore.about_activity_part1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rain.androidexplore.R;
import com.rain.androidexplore.bean.User;
import com.rain.androidexplore.util.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Author:rain
 * Date:2018/7/25 16:47
 * Description:
 */
public class TestActivity2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "TestActivity2";

    private TextView tv;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv.setText(((User) msg.obj).toString());
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        tv = findViewById(R.id.tv);
        findViewById(R.id.btn_read).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read:
                readFromFile();
                break;

        }
    }

    private void readFromFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(Constant.CACHE_FILE_PATH);
                if (file.exists()) {
                    try {
                        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                        User user = (User) objectInputStream.readObject();
                        Message message = Message.obtain();
                        message.obj = user;
                        message.setTarget(handler);
                        Log.e(TAG, "run: user:"+user.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

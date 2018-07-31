package com.rain.androidexplore.ipc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rain.androidexplore.R;
import com.rain.androidexplore.bean.User;
import com.rain.androidexplore.util.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Author:rain
 * Date:2018/7/26 10:57
 * Description:
 * 利用文件进行进程间通讯
 */
public class TestActivity3 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "TestActivity3";
    private static final int REQUEST_PERMISSION_CODE = 53;
    private TextView tv_content;
    private EditText et_name;
    private EditText et_age;
    private String age;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        tv_content = findViewById(R.id.tv_content);
        et_age = findViewById(R.id.et_age);
        et_name = findViewById(R.id.et_name);

        findViewById(R.id.btn_activity2).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity2:
                startActivity(new Intent("com.rain.androidexplore.ipc"));
                break;

            case R.id.btn_save:
                getInputValue();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void getInputValue() {
        age = "";
        name = "";
        age = et_age.getText().toString().trim();
        name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(age)||TextUtils.isEmpty(name)) {
            Toast.makeText(this, "信息不全", Toast.LENGTH_SHORT).show();
            return;
        }
        checkPermissionn();
    }

    // 将对象保存到文件
    private void saveToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User(name, Integer.parseInt(age));
                File folder = new File(Constant.FOLDER_PATH);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(Constant.CACHE_FILE_PATH);
                ObjectOutputStream objectOutputStream = null;

                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(user);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    // 检查读写权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionn() {
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            saveToFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限请求成功
                saveToFile();
            } else {
                Toast.makeText(this, "权限拒绝！", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

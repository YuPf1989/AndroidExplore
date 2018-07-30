package com.rain.androidexplore;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rain.androidexplore.about_activity_part1.TestActivity1;
import com.rain.androidexplore.bean.UserManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Log.e(TAG, "onCreate: " );

        if (savedInstanceState != null) {
            Log.e(TAG, "onCreate: savedInstanceState:"+savedInstanceState.getString("extra"));
        }

        findViewById(R.id.btn_activity).setOnClickListener(this);
        findViewById(R.id.btn_startMain).setOnClickListener(this);
        findViewById(R.id.btn_ipc).setOnClickListener(this);

        // 演示在不同的进程中的组件获取的静态常量的值的情况
        // 原始结果为uid = 0，此处修改为1
        UserManager.uid = 1;
        Log.e(TAG, "onCreate: uid:"+ UserManager.uid);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // activity生命周期及启动相关
            case R.id.btn_activity:
                startActivity(new Intent(this, TestActivity1.class));
                break;

            case R.id.btn_startMain:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.btn_ipc:
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(TAG, "onResume: " );
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.e(TAG, "onDestroy: ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("extra","data");
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState: ");
        Log.e(TAG, "onRestoreInstanceState: savedInstanceState:"+savedInstanceState.getString("extra"));
    }

    /**
     * 该方法在activity在activity栈中启动时，如果不是新建了一个实例，
     * 那么该方法就会调用
     * 比如启动模式为singleTask,singleTop,singleInstance
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: ");
    }
}

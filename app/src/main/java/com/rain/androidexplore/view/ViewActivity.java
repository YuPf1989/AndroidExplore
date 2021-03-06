package com.rain.androidexplore.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/2 16:31
 * Description:
 * 第三章view相关
 * View 提供了如下 5 种方法获取 View 的坐标：
 1. View.getTop()、View.getLeft()、View.getBottom()、View.getRight();
 2. View.getX()、View.getY();
 3. View.getTranslationX()、View.getTranslationY();
 4. View.getLocationOnScreen(int[] position);
 5. View.getLocationInWindow(int[] position);
 * 其中1-3获取的是当前view相对于父容器的坐标
 * 4获取的是相对于屏幕的坐标
 * 5获取的是除状态栏和actionbar之外区域的相对于屏幕的坐标
 * 第1条，获取的是view初始状态的固定坐标，不随平移改变
 *
 * 点击tv onEvent方法,返回值false执行的情况：
 * tv down->rl down->activity down move up
 *
 * 点击tv onEvent方法,返回值true执行的情况：
 * 如果某个view onEvent返回为true,则表明当前view消费了事件，则只有该view的onEvent调用
 */
public class ViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "ViewActivity";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        findViewById(R.id.btn_on_touch_event).setOnClickListener(this);
        findViewById(R.id.btn_translate).setOnClickListener(this);
        findViewById(R.id.btn_dispatch_view_event).setOnClickListener(this);
        findViewById(R.id.btn_custom_view).setOnClickListener(this);
        findViewById(R.id.btn_view_anim).setOnClickListener(this);

        // 演示anr异常,实际情形是手机出现30s的黑屏，但并没有崩溃，最后仍打开了页面
//        SystemClock.sleep(30000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_on_touch_event:
                startActivity(new Intent(this,ViewTouchEventActivity.class));
                break;

            case R.id.btn_translate:
                startActivity(new Intent(this,ViewTranslateActivity.class));
                break;

            case R.id.btn_dispatch_view_event:
                startActivity(new Intent(this,ViewDispatchEventActivity.class));
                break;

            case R.id.btn_custom_view:
                startActivity(new Intent(this,CustomViewActivity.class));
                break;


            case R.id.btn_view_anim:
                startActivity(new Intent(this,ViewAnimActivity.class));
                break;

        }
    }
}

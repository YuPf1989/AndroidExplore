package com.rain.androidexplore.window_windowmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Author:rain
 * Date:2018/8/10 9:16
 * Description:主要是window和windowmanager相关
 * window分为3类：
 * 1.应用window（activity所依附的window），层级1-99
 * 2.子window（比如dialog），必须依附父window才能存在，层级1000-1999
 * 3.系统window（toast），层级2000-2999
 * 层级越高，window层界面越靠上
 */
public class WindowManagerActivity extends AppCompatActivity {

    private Button mButton;
    private WindowManager.LayoutParams layoutParams;
    private WindowManager wmManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_windowmanager);

        // TODO: 2018/8/10 一下代码会报错
       /* wmManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mButton = new Button(this);
        mButton.setText("button");
        // 具体参数不是很清楚
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0, PixelFormat.TRANSPARENT);

        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        layoutParams.x = 100;
        layoutParams.y = 300;

        wmManager.addView(mButton,layoutParams);*/

    }
}

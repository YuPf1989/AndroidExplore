package com.rain.androidexplore.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rain.androidexplore.R;
import com.rain.androidexplore.view.widght.MyRelativeLayout;

/**
 * Author:rain
 * Date:2018/8/3 17:19
 * Description:
 * 研究事件分发机制
 * 打印结果：
 * a.默认情形下：如果没有子view消费事件：
 * 08-06 09:40:35.847 27621-27621/com.rain.androidexplore E/DispatchEventActivity: tv: ACTION_DOWN
 08-06 09:40:35.848 27621-27621/com.rain.androidexplore E/DispatchEventActivity: rl: ACTION_DOWN
 activity: ACTION_DOWN
 08-06 09:40:35.881 27621-27621/com.rain.androidexplore E/DispatchEventActivity: activity: ACTION_MOVE
 08-06 09:40:36.060 27621-27621/com.rain.androidexplore E/DispatchEventActivity: activity: ACTION_UP
 * b.activity#dispatchTouchEvent = false/true,都将接受不到点击事件，必须有系统分发
 * c.rl#onIntercept = true,
 * 08-06 10:00:32.856 1987-1987/com.rain.androidexplore E/DispatchEventActivity: rl: ACTION_DOWN
 activity: ACTION_DOWN
 08-06 10:00:32.889 1987-1987/com.rain.androidexplore E/DispatchEventActivity: activity: ACTION_UP
 *
 *
 *
 * 结论
 * 1.Touch事件分发中只有两个主角:ViewGroup和View。
 * ViewGroup包含onInterceptTouchEvent、dispatchTouchEvent、onTouchEvent三个相关事件。
 * View包含dispatchTouchEvent、onTouchEvent两个相关事件。其中ViewGroup又继承于View。
 * 2.当Acitivty接收到Touch事件时，将遍历子View进行Down事件的分发。
 * ViewGroup的遍历可以看成是递归的。
 * 分发的目的是为了找到真正要处理本次完整触摸事件的View，这个View会在onTouchuEvent结果返回true。
 * 3.当某个子View返回true时，会中止Down事件的分发，同时在ViewGroup中记录该子View。
 * 接下去的Move和Up事件将由该子View直接进行处理。由于子View是保存在ViewGroup中的，多层ViewGroup的节点结构时，
 * 上级ViewGroup保存的会是真实处理事件的View所在的ViewGroup对象:如ViewGroup0-ViewGroup1-TextView的结构中，
 * TextView返回了true，它将被保存在ViewGroup1中，而ViewGroup1也会返回true，被保存在ViewGroup0中。
 * 当Move和UP事件来时，会先从ViewGroup0传递至ViewGroup1，再由ViewGroup1传递至TextView。
 * 4.当ViewGroup中所有子View都不捕获Down事件时，将触发ViewGroup自身的onTouch事件。
 * 触发的方式是调用super.dispatchTouchEvent函数，即父类View的dispatchTouchEvent方法。
 * 在所有子View都不处理的情况下，触发Acitivity的onTouchEvent方法。
 * 5.onInterceptTouchEvent有两个作用：1.拦截Down事件的分发。2.中止Up和Move事件向目标View传递，使得目标View所在的ViewGroup捕获Up和Move事件。
 */
public class ViewDispatchEventActivity extends AppCompatActivity {
    private static final String TAG  = "DispatchEventActivity";

    private Button btn;
    private TextView tv;
    private MyRelativeLayout rl;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dispatch_view);

        // 层级关系
        // tv 在 rl 内部，btn 与 rl 同级
        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);
        rl = findViewById(R.id.rl);

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "btn: ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "btn: ACTION_MOVE");
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "btn: ACTION_UP");
                        break;
                }
                return false;
            }
        });

        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "tv: ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "tv: ACTION_MOVE");
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "tv: ACTION_UP");
                        break;
                }
                return false;
            }
        });

        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "rl: ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "rl: ACTION_MOVE");
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "rl: ACTION_UP");
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "activity: ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "activity: ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.e(TAG, "activity: ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.e(TAG, "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

}

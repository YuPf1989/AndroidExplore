package com.rain.androidexplore.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/3 11:48
 * Description:
 */
public class ViewTouchEventActivity extends AppCompatActivity {
    private static final String TAG  = "ViewTouchEventActivity";
    private TextView tv;
    private RelativeLayout rl;

    // 手势侦测
    private GestureDetector gestureDetector;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);

        // 设备判断滑动的最小距离,实际值为24px,8dp
        int slop = ViewConfiguration.get(this).getScaledTouchSlop();
        Log.e(TAG, "onCreate: slop:"+slop);


        tv = findViewById(R.id.tv);
        rl = findViewById(R.id.rl);

        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.e(TAG, "onSingleTapUp: ");
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.e(TAG, "onFling: ");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.e(TAG, "onDoubleTap: ");
                return super.onDoubleTap(e);
            }
        });

        // 当view绘制完成自动调用
        tv.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "post: " );

                Log.e(TAG, "tv.getLeft: "+tv.getLeft());
                Log.e(TAG, "tv.getTop: "+tv.getTop());
                Log.e(TAG, "tv.getRight: "+tv.getRight());
                Log.e(TAG, "tv.getBottom: "+tv.getBottom());

                Log.e(TAG, "tv.getX: "+tv.getX());
                Log.e(TAG, "tv.getY: "+tv.getY());

                Log.e(TAG, "tv.getTranslationX: "+tv.getTranslationX());
                Log.e(TAG, "tv.getTranslationY: "+tv.getTranslationY());
            }


        });


        tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "onTouch: tv.setOnTouchListener:");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouchEvent: ACTION_DOWN:"+event.getX());
                        Log.e(TAG, "onTouchEvent: ACTION_DOWN:"+event.getY());
                        Log.e(TAG, "onTouchEvent: ACTION_DOWN:"+event.getRawX());
                        Log.e(TAG, "onTouchEvent: ACTION_DOWN:"+event.getRawY());
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "onTouchEvent: ACTION_MOVE:"+event.getX());
                        Log.e(TAG, "onTouchEvent: ACTION_MOVE:"+event.getY());

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouchEvent: ACTION_UP:"+event.getX());
                        Log.e(TAG, "onTouchEvent: ACTION_UP:"+event.getY());

                        break;
                }
                return false;
            }
        });

        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                Log.e(TAG, "onTouch: tv.setOnTouchListener:");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "rl: ACTION_DOWN:"+event.getX());
                        Log.e(TAG, "rl: ACTION_DOWN:"+event.getY());
                        break;

                    case MotionEvent.ACTION_MOVE:
                        Log.e(TAG, "rl: ACTION_MOVE:"+event.getX());
                        Log.e(TAG, "rl: ACTION_MOVE:"+event.getY());

                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "rl: ACTION_UP:"+event.getX());
                        Log.e(TAG, "rl: ACTION_UP:"+event.getY());

                        break;
                }
                return false;
            }
        });
    }

    // 手指触摸屏幕的时候发生调用
    // getX与getRawX区别是getX是相对于当前View，getRawX是相当于屏幕
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.e(TAG, "activity: ACTION_DOWN:"+event.getX());
//                Log.e(TAG, "activity: ACTION_DOWN:"+event.getY());
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "activity: ACTION_MOVE:"+event.getX());
//                Log.e(TAG, "activity: ACTION_MOVE:"+event.getY());
//
//                break;
//
//            case MotionEvent.ACTION_UP:
//                Log.e(TAG, "activity: ACTION_UP:"+event.getX());
//                Log.e(TAG, "activity: ACTION_UP:"+event.getY());
//
//                break;
//        }
//        return super.onTouchEvent(event);
    }
}

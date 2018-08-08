package com.rain.androidexplore.view;

import android.content.Context;
import android.view.MotionEvent;

/**
 * Author:rain
 * Date:2018/8/3 14:42
 * Description:
 * 一个可以跟随手指滑动的view
 * todo 写了一半
 */
public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    private float lastY = 0;
    private float lastX = 0;
    public MyTextView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getRawY();
        float x = event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                float distanceX = x - lastX;
                float distanceY = y - lastY;
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

}

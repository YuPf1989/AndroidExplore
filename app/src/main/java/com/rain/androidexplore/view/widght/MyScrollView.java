package com.rain.androidexplore.view.widght;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Author:rain
 * Date:2018/8/6 11:07
 * Description:
 * 自定义scrollview
 * scrollview嵌套listview
 * 演示滑动冲突问题
 */
public class MyScrollView extends ScrollView {
//    private
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev);

    }
}

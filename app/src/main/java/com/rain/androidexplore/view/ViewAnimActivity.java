package com.rain.androidexplore.view;

import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/9 15:32
 * Description:
 * 对任意属性做动画
 * 需要满足的条件
 * 1.该属性必须要有get、set方法（如果不满足，程序会直接crash）
 * 2.该属性的set方法必须能通过方法反应出来（否则动画无效果）
 *
 * 解决方法：
 * 1.为该对象的属性加上get、set方法
 * 2.用一个类包装原始对象，间接提供get、set方法
 * 3.采用valueAnimator，监听动画过程，自己实现属性的改变
 *
 */
public class ViewAnimActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;
    private Button btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_anim);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                // 对button做属性动画
                startAnim();
                break;

            case R.id.btn2:
                startAnim2();
                break;
        }
    }

    private void startAnim2() {
        ValueAnimator animator = ValueAnimator.ofInt(1,100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            IntEvaluator evaluator = new IntEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 动画运行时，当前的值，范围1-100
                int value = (int) animation.getAnimatedValue();
                // 当前动画运行时间的比率
                float fraction = animation.getAnimatedFraction();
                btn2.getLayoutParams().width = evaluator.evaluate(fraction, 0, value * 5);
                btn2.requestLayout();
            }
        });

        animator.setDuration(5000).start();
    }

    private void startAnim() {
        // Button的setWidth、setHeight是继承与TextView的，只是对最小宽高做了设置，所以动画无效
//        ObjectAnimator.ofInt(btn,"width",500).setDuration(5000).start();
        ObjectAnimator.ofInt(new ViewWrapper(btn),"width",600).setDuration(5000).start();
    }

    class ViewWrapper {
        private View mTarget;
        public ViewWrapper(View target) {
            mTarget = target;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }
    }
}

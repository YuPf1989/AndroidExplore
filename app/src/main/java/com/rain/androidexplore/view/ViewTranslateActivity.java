package com.rain.androidexplore.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/3 11:50
 * Description:
 */
public class ViewTranslateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewTranslateActivity";
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_translate);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        findViewById(R.id.btn_top).setOnClickListener(this);
        findViewById(R.id.btn_scroll).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) btn.getLayoutParams();

        switch (v.getId()) {

            case R.id.btn:
                layoutParams.topMargin += 20;
                btn.requestLayout();

                break;

            case R.id.btn_top:
                layoutParams.topMargin -= 20;
                btn.requestLayout();
                break;

            case R.id.btn_scroll:
                // 是让view里边的内容移动
                v.scrollBy(4, 4);
                break;
        }
    }

}

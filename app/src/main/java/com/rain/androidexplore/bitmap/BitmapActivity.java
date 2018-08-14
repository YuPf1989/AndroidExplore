package com.rain.androidexplore.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/13 11:15
 * Description:
 * 使用bitmapFactory.Option来修改bitmap的采样率
 *
 * 原始imageView尺寸为150dp*150dp，即450px*450px
 * p3 1920*1080
 */
public class BitmapActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "BitmapActivity";

    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);

        findViewById(R.id.btn).setOnClickListener(this);
        iv = findViewById(R.id.iv);
        iv.setImageResource(R.drawable.p3);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                iv.setImageBitmap(decodeSampleBitmapFromRes(getResources(),R.drawable.p3,450,450));
                break;
        }
    }

    private Bitmap decodeSampleBitmapFromRes (Resources res,int resId,int reqWidth,int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 只解析图片的原始宽高信息，并不加载图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 计算所需的采样率（压缩率）
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 获取原始宽高
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int inSampleSize = 1;
        if (outHeight > reqHeight || outWidth > reqWidth) {
            int halfHeight = outHeight / 2;
            int halfWidth = outWidth / 2;
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.e(TAG, "calculateInSampleSize:inSampleSize: "+inSampleSize);
        return inSampleSize;
    }
}

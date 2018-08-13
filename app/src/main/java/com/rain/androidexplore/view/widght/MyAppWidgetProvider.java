package com.rain.androidexplore.view.widght;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/8 16:17
 * Description:
 * 本质上是一个广播
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "MyAppWidgetProvider";
    private static final String CLICK_ACTION = "com.rain.androidexplore.action.CLICK";

    public MyAppWidgetProvider() {

    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.e(TAG, "onReceive: action:" + intent.getAction());
        if (intent.getAction().equals(CLICK_ACTION)) {
            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
                    // image旋转
                    for (int i = 0; i < 37; i++) {
                        int degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.icon, rotateBitmap(context, bitmap, degree));
                        Intent clickIntent = new Intent();
                        clickIntent.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent);
                        widgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }
    }

    private Bitmap rotateBitmap(Context context, Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // 每次桌面小部件更新时都会调用一次该方法
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e(TAG, "onUpdate: ");
        Log.e(TAG, "appWidgetIds.size: "+appWidgetIds.length);
        for (int i = 0; i < appWidgetIds.length; i++) {
            onWidgetUpdate(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    private void onWidgetUpdate(Context context, AppWidgetManager widgetManager, int appWidgetId) {
        Log.e(TAG, "onWidgetUpdate:appWidgetId: " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent clickIntent = new Intent();
        clickIntent.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent);
        widgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class), remoteViews);
    }
}

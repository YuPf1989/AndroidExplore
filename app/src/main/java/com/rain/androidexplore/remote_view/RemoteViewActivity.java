package com.rain.androidexplore.remote_view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RemoteViews;

import com.rain.androidexplore.R;

/**
 * Author:rain
 * Date:2018/8/8 10:12
 * Description:
 * remoteView 是一种跨进程并且可以更新的view
 * remoteview 主要是用于通知栏和桌面小部件中
 */
public class RemoteViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "RemoteViewActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_view);

        findViewById(R.id.btn_send_notification).setOnClickListener(this);
        findViewById(R.id.btn_send_custom_notification).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_send_notification:
//                NewMessageNotification.notify(this,"这是一个通知",10);
                sendNotification();
                break;

            case R.id.btn_send_custom_notification:
                sendCustomNotification();
                break;
        }
    }

    // 发送自定义布局的notification
    private void sendCustomNotification() {
        Intent intent = new Intent(this, RemoteViewActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 创建remoteview
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_remoteview);
        remoteViews.setTextViewText(R.id.tv_content,"自定义布局的通知");
        remoteViews.setImageViewResource(R.id.icon,R.mipmap.ic_launcher);
        remoteViews.setOnClickPendingIntent(R.id.btn_click,pendingIntent);

        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.mipmap.ic_launcher)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContent(remoteViews)
                ;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(2,builder.build());
    }

    // 发送通知栏通知
    private void sendNotification() {
        Intent intent = new Intent(this, RemoteViewActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =  new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.mipmap.ic_launcher)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle("我是带Action的Notification")
                .setContentText("点我会打开Avtivity")
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,builder.build());

    }
}

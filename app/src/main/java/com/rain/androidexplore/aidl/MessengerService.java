package com.rain.androidexplore.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rain.androidexplore.bean.User;
import com.rain.androidexplore.util.Constant;

/**
 * Author:rain
 * Date:2018/7/27 18:02
 * Description:
 * 该service相当于server
 * 实现的功能：
 * client可以向server发送消息
 * server可以向client发送消息
 */
public class MessengerService extends Service {
    private static final String TAG  = "MessengerService";

    public static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MESSAGE_FROM_CLIENT:
                    Bundle data = msg.getData();
                    User user = (User) data.getSerializable("data");
                    Log.e(TAG, "MESSAGE_FROM_CLIENT: "+user.toString());

                    // 向客户端回复消息
                    Messenger client = msg.replyTo;
                    try {
                        Message replyMsg = Message.obtain(null,Constant.MESSAGE_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putString("data","你的消息我已经收到！");
                        replyMsg.setData(bundle);
                        client.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    private final Messenger messenger = new Messenger(new MessengerHandler());




}

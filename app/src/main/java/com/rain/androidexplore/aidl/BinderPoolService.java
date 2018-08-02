package com.rain.androidexplore.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Author:rain
 * Date:2018/8/2 10:15
 * Description:
 * 演示binder连接池
 * 当aidl接口比较多时，用一个service处理即可
 */
public class BinderPoolService extends Service {
    private static final String TAG  = "BinderPoolService";
    private BinderPool.BinderPoolImpl binderPool = new BinderPool.BinderPoolImpl();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binderPool;
    }
}

package com.rain.androidexplore.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.rain.androidexplore.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * Author:rain
 * Date:2018/8/2 10:21
 * Description:
 * BinderPool的具体实现
 * 1.绑定远程服务
 * 2.通过queryBinder获取对应binder
 */
public class BinderPool {
    private static final String TAG = "BinderPool";
    private static final int BINDER_NONE = -1;
    public static final int BINDER_COMPUTE = 0;
    public static final int BINDER_SECURITY = 1;
    private final Context mContext;
    private static volatile BinderPool instance;
    private CountDownLatch countDownLatch;
    private IBinderPool binderPool;

    private BinderPool(Context c) {
        mContext = c.getApplicationContext();
        connectBinderPoolService();
    }

    private synchronized void connectBinderPoolService() {
        // 用于同步的工具类，一个阻塞线程的计数器，countDownLatch.await会阻塞线程，只有调用countdown，
        // 计数器就减1，直到计数器为0时，线程才继续执行
        countDownLatch = new CountDownLatch(1);
        Intent service = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(service, binderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection binderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderPool = IBinderPool.Stub.asInterface(service);
            try {
                binderPool.asBinder().linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.e(TAG, "binderDied: ");
            binderPool.asBinder().unlinkToDeath(deathRecipient, 0);
            binderPool = null;
            connectBinderPoolService();
        }

    };

    public static BinderPool getInstance(Context c) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(c);
                }
            }
        }
        return instance;
    }

    // TODO: 2018/8/2 还不知道该方法有何用
    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        if (binderPool != null) {
            try {
                binder = binderPool.queryBinder(binderCode);
            } catch (RemoteException e) {
                e.printStackTrace();

            }
        }
        return binder;
    }

    public static class BinderPoolImpl extends IBinderPool.Stub{
        public BinderPoolImpl() {
            super();
        }

        @Override
        public IBinder queryBinder(int binder_code) throws RemoteException {
            IBinder binder = null;
            switch (binder_code) {
                case BINDER_COMPUTE:
                    binder = new ComputImpl();
                    break;

                case BINDER_SECURITY:
                    binder = new SecurityCenterImpl();
                    break;

                default:
                    break;
            }
            return binder;
        }
    }
}

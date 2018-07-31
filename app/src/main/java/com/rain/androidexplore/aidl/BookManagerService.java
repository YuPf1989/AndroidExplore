package com.rain.androidexplore.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rain.androidexplore.Book;
import com.rain.androidexplore.IBookManager;
import com.rain.androidexplore.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author:rain
 * Date:2018/7/30 10:42
 * Description:
 * 实现aidl中的接口
 */
public class BookManagerService extends Service {
    private static final String TAG = "BookManagerService";
    // 由于aidl方法是在binder的线程池中执行的，该集合支持并发读写
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> listenerList = new RemoteCallbackList<>();
    // 用于多线程并发，进行原子操作的boolean
    private AtomicBoolean isServiceDestroyed = new AtomicBoolean(false);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (checkCallingOrSelfPermission("com.rain.androidexplore.ACCESS_BOOK_SERVICE") != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return mBinder;
    }

    // aidl方法是在服务端的binder线程池中执行的
    private IBookManager.Stub mBinder = new IBookManager.Stub() {


        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.e(TAG, "getBookList: pid:"+Process.myPid());
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listenerList.register(listener);
            Log.e(TAG, "registerListener: size:" + listenerList.getRegisteredCallbackCount());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listenerList.unregister(listener);
            Log.e(TAG, "unregisterListener: size:" + listenerList.getRegisteredCallbackCount());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: pid"+ Process.myPid());
        // 每隔5s添加一本新书，并对所有注册的listener进行通知
        new Thread(new ServiceWork()).start();
    }

    private class ServiceWork implements Runnable {

        @Override
        public void run() {
            while (!isServiceDestroyed.get()) {
                SystemClock.sleep(5000);
                int newBookCount = bookList.size() + 1;
                Book book = new Book("newBook" + newBookCount, newBookCount);
                bookList.add(book);
                // note:beginBroadcast和finishBroadcast必须成对使用
                int N = listenerList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    IOnNewBookArrivedListener l = listenerList.getBroadcastItem(i);
                    try {
                        if (l != null)
                            l.onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                listenerList.finishBroadcast();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceDestroyed.set(true);
    }
}

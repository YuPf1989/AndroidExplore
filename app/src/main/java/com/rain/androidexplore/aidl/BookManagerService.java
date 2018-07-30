package com.rain.androidexplore.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.rain.androidexplore.Book;
import com.rain.androidexplore.IBookManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author:rain
 * Date:2018/7/30 10:42
 * Description:
 * 实现aidl中的接口
 */
public class BookManagerService extends Service {
    // 由于aidl方法是在binder的线程池中执行的，该集合支持并发读写
    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private IBookManager.Stub mBinder = new IBookManager.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

    };
}

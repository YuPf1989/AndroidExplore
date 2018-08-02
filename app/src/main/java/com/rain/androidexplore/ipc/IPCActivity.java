package com.rain.androidexplore.ipc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.rain.androidexplore.Book;
import com.rain.androidexplore.IBookManager;
import com.rain.androidexplore.IOnNewBookArrivedListener;
import com.rain.androidexplore.R;
import com.rain.androidexplore.aidl.BookManagerService;
import com.rain.androidexplore.aidl.MessengerService;
import com.rain.androidexplore.bean.User;
import com.rain.androidexplore.util.Constant;

/**
 * Author:rain
 * Date:2018/7/26 10:49
 * Description:
 * IPC进程间通讯
 * 该activity相当于client
 * 2.不同组件在不同进程访问内存的情形
 * 3.通过文件来进行进程间通讯，eg TestActivity1与TestActivity2进行进程间通讯
 * 4.通过Messenger（底层仍旧是aidl）对象进行进程间通讯，使用messenger进行进程间通讯
 * 主要是为了传递消息
 * 5.使用aidl主要是为了client端能都调用server端的方法
 * <p>
 * 结论
 * 安卓为每个进程都分配了一个虚拟机，不同虚拟机地址空间不同，
 * 不同的虚拟机在访问同一个对象会有多个副本，因此每个进程都有一个UserManager类，并且互不干扰
 */
public class IPCActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = "IPCActivity";
    private TextView tv_content;
    private IBookManager iBookManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onNewBookArrived: pid："+ Process.myPid());
            String targetClassName = name.getClassName();
            Log.e(TAG, "onServiceConnected: targetClassName:" + targetClassName);
            // 演示使用messenger进行进程间通讯
            if (targetClassName.equals(MessengerService.class.getCanonicalName())) {
                Messenger messenger = new Messenger(service);
                Message message = Message.obtain(null, Constant.MESSAGE_FROM_CLIENT);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", new User("chen", 3));
                message.setData(bundle);
                try {

                    // 为了能接收到服务端发送过来的消息，需要制定是哪个replyMessenger
                    message.replyTo = mGetReplyMessenger;
                    messenger.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            // 使用aidl在客户端调用服务端方法
            // note:服务端方法本身运行在服务端binder线程池中，不需要开线程，
            // 但是客户端的onServiceConnected和onServiceDisconnected都运行在UI线程中，所以不可以执行服务端耗时的方法
            if (targetClassName.equals(BookManagerService.class.getName())) {
                try {
                    iBookManager = IBookManager.Stub.asInterface(service);
                    for (int i = 0; i < 10; i++) {
                        Book book = new Book("name" + 1, i);
                        iBookManager.addBook(book);
                    }

                    tv_content.setText(iBookManager.getBookList().toString());

                    iBookManager.registerListener(mBookArrivedListener);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBookManager = null;
        }
    };

    private IOnNewBookArrivedListener mBookArrivedListener = new IOnNewBookArrivedListener.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Log.e(TAG, "onNewBookArrived: newBook:"+newBook.toString());
        }
    };

    private final Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    // 接收服务端传递过来的消息
    public static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MESSAGE_FROM_SERVER:
                    String data = msg.getData().getString("data");
                    Log.e(TAG, "MESSAGE_FROM_SERVER: " + data);
                    break;
            }
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc);


        tv_content = findViewById(R.id.tv_content);
        findViewById(R.id.btn_activity3).setOnClickListener(this);
        findViewById(R.id.btn_send_remote).setOnClickListener(this);
        findViewById(R.id.btn_invote_remote).setOnClickListener(this);
        findViewById(R.id.btn_content_provider).setOnClickListener(this);
        findViewById(R.id.btn_socket).setOnClickListener(this);
        findViewById(R.id.btn_binderpool).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 通过文件进行进程间通讯
            case R.id.btn_activity3:
                startActivity(new Intent(this, TestActivity3.class));
                break;

            case R.id.btn_send_remote:
                connectService(MessengerService.class);
                break;

            case R.id.btn_invote_remote:
                connectService(BookManagerService.class);
                break;

            case R.id.btn_content_provider:
                startActivity(new Intent(this, TestActivity4.class));
                break;

            case R.id.btn_socket:
                startActivity(new Intent(this,SocketClientActivity.class));
                break;

            case R.id.btn_binderpool:
                startActivity(new Intent(this,BinderPoolActivity.class));
                break;
        }
    }

    private void connectService(Class cls) {
        Intent intent = new Intent(this, cls);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iBookManager != null && iBookManager.asBinder().isBinderAlive()) {
            try {
                iBookManager.unregisterListener(mBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(connection);
    }
}

package com.rain.androidexplore.about_activity_part1;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rain.androidexplore.Book;
import com.rain.androidexplore.IBookManager;
import com.rain.androidexplore.IOnNewBookArrivedListener;
import com.rain.androidexplore.R;
import com.rain.androidexplore.aidl.BookManagerService;
import com.rain.androidexplore.aidl.MessengerService;
import com.rain.androidexplore.bean.User;
import com.rain.androidexplore.bean.UserManager;
import com.rain.androidexplore.util.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Author:rain
 * Date:2018/7/25 9:59
 * Description:
 * 该activity相当于client
 * 1.演示MainActivity->TestActivity生命周期的变化
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
public class TestActivity1 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestActivity1";
    private static final int REQUEST_PERMISSION_CODE = 53;
    private IBookManager iBookManager;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
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
    private TextView tv_content;

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


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        findViewById(R.id.btn_activity2).setOnClickListener(this);
        findViewById(R.id.btn_send_remote).setOnClickListener(this);
        findViewById(R.id.btn_invote_remote).setOnClickListener(this);
        tv_content = findViewById(R.id.tv_content);

        checkPermissionn();

        Log.e(TAG, "onCreate: uid:" + UserManager.uid);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activity2:
                startActivity(new Intent("com.rain.androidexplore.about_activity_part1_activity2"));
                break;

            case R.id.btn_send_remote:
                connectService(MessengerService.class);
                break;


            case R.id.btn_invote_remote:
                connectService(BookManagerService.class);
                break;
        }
    }

    private void connectService(Class cls) {
        Intent intent = new Intent(this, cls);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    // 将对象保存到文件
    private void saveToFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User("john", 20);
                File folder = new File(Constant.FOLDER_PATH);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(Constant.CACHE_FILE_PATH);
                ObjectOutputStream objectOutputStream = null;

                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(user);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (objectOutputStream != null) {
                        try {
                            objectOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    // 检查读写权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionn() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            saveToFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限请求成功
                saveToFile();
            } else {
                Toast.makeText(this, "权限拒绝！", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString("extra", "data");
        super.onSaveInstanceState(outState, outPersistentState);
        Log.e(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG, "onRestoreInstanceState: ");
        Log.e(TAG, "onRestoreInstanceState: savedInstanceState:" + savedInstanceState.getString("extra"));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "onConfigurationChanged: " + newConfig.orientation);
    }
}

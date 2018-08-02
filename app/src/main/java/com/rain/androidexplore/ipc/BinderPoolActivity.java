package com.rain.androidexplore.ipc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.rain.androidexplore.ICompute;
import com.rain.androidexplore.ISecurityCenter;
import com.rain.androidexplore.R;
import com.rain.androidexplore.aidl.BinderPool;
import com.rain.androidexplore.aidl.ComputImpl;
import com.rain.androidexplore.aidl.SecurityCenterImpl;

/**
 * Author:rain
 * Date:2018/8/2 12:08
 * Description:
 */
public class BinderPoolActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int SEND_MESSAGE = 0;
    private TextView tv;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv.setText(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_common);

        tv = findViewById(R.id.tv);
        findViewById(R.id.btn_invote).setOnClickListener(this);
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(this);
        IBinder compute_binder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        ICompute iCompute = ComputImpl.asInterface(compute_binder);
        IBinder security_binder = binderPool.queryBinder(BinderPool.BINDER_SECURITY);
        ISecurityCenter iSecurityCenter = SecurityCenterImpl.asInterface(security_binder);
        String pwd = "helloworld-安卓";
        StringBuffer bf = new StringBuffer();
        try {
            int add = iCompute.add(1, 2);
            System.out.println("add:"+add);
            String encrypt = iSecurityCenter.encrypt(pwd);
            System.out.println("encrypt:"+encrypt);
            String decrypt = iSecurityCenter.decrypt(encrypt);
            System.out.println("decrypt:"+decrypt);
            bf.append("sum:" + add)
                    .append("\n")
                    .append("pwd:" + pwd)
                    .append("\n")
                    .append("encrypt:" + encrypt)
                    .append("\n")
                    .append("decrypt:" + decrypt)
                    .append("\n");
            handler.obtainMessage(SEND_MESSAGE, bf).sendToTarget();

        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_invote:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doWork();
                    }
                }).start();
                break;
        }
    }
}

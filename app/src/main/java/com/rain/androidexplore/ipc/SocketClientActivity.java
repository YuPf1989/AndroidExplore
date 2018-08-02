package com.rain.androidexplore.ipc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rain.androidexplore.R;
import com.rain.androidexplore.socket.TCPServerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author:rain
 * Date:2018/8/1 15:32
 * Description:
 * 使用socket进行进程间通讯
 * 此为客户端
 */
public class SocketClientActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_content;
    private EditText et_input;
    private Button btn_send;
    private Socket clientSocket;
    private PrintWriter out;
    private static final int MESSAGE_RECEIVE_NEW = 0;
    private static final int MESSAGE_SOCKET_CONNECTED = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW:
                    String receivedMsg = (String) msg.obj;
                    tv_content.append(receivedMsg);
                    break;

                case MESSAGE_SOCKET_CONNECTED:
                    btn_send.setEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_client);

        tv_content = findViewById(R.id.tv_content);
        et_input = findViewById(R.id.et_input);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setEnabled(false);
        btn_send.setOnClickListener(this);

        startService(new Intent(this, TCPServerService.class));

        new Thread(new Runnable() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clientSocket != null) {
            try {
                clientSocket.shutdownInput();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8868);
                clientSocket = socket;
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                handler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("tcp server connected ");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                e.printStackTrace();
            }
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!SocketClientActivity.this.isFinishing()) {
                String s = in.readLine();
                System.out.println("server:" + s);
                if (s != null) {
                    String time = getFormatTime();
                    String receiveMsg = "server(" + time + "):" + s + "\n";
                    handler.obtainMessage(MESSAGE_RECEIVE_NEW, receiveMsg).sendToTarget();
                }
            }

            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFormatTime() {
        return new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (!TextUtils.isEmpty(et_input.getText().toString()) && out != null) {
                    String sendMsg = et_input.getText().toString();
                    out.println(sendMsg);
                    tv_content.append("self(" + getFormatTime() + "):" + sendMsg + "\n");
                    et_input.setText("");
                }
                break;
        }
    }
}

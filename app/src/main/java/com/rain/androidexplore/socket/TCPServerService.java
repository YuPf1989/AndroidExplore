package com.rain.androidexplore.socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Author:rain
 * Date:2018/8/1 15:41
 * Description:
 * 使用socket进行通讯
 * 此为server端
 */
public class TCPServerService extends Service {
    private boolean isServerDestroy = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字啊？",
            "今天天气不错啊，shy",
            "你知道吗？我可以和多个人同时聊天",
            "给你讲个笑话吧，据说爱笑的人运气不会太差"
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new TCPServer()).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServerDestroy = true;
    }

    class TCPServer implements Runnable{

        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(8868);
                while (!isServerDestroy) {
                    final Socket client = serverSocket.accept();
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 用于接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室！");
        while (!isServerDestroy) {
            String s = in.readLine();
            System.out.println("client:"+s);
            if (s == null) {
                // 客户端断开连接？
                break;
            }
            int i = new Random().nextInt(mDefinedMessages.length);
            String msg = mDefinedMessages[i];
            System.out.println("server:"+mDefinedMessages[i]);
            out.println(msg);
        }

        System.out.println("client qiet...");
        // 关闭流
        in.close();
        out.close();
        client.close();
    }
}

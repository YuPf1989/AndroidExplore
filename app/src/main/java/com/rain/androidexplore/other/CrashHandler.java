package com.rain.androidexplore.other;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author:rain
 * Date:2018/8/13 17:53
 * Description:
 * 用于捕获未捕获的异常
 * 同时将异常写入本地文件
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/crash/";
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private Thread.UncaughtExceptionHandler mDefaultExceptionHander;
    private Context mContext;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        checkWritePermission(e);
        // 如果系统提供了默认的异常处理器，就由系统处理，否则自己结束自己
        if (mDefaultExceptionHander != null) {
            mDefaultExceptionHander.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    // todo 请求权限是在activity中才有的回调，扯淡！
    @TargetApi(Build.VERSION_CODES.M)
    private void checkWritePermission(Throwable e) {
        if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // todo 存在问题
            Log.e(TAG, "checkWritePermission: no");
        } else {
            saveExceptionToSDCard(e);
        }
    }

    private void saveExceptionToSDCard(Throwable e) {
        File folder = new File(PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String curTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        File file = new File(PATH + FILE_NAME + curTime + FILE_NAME_SUFFIX);
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(curTime);
            savePhoneInfo(pw);
            pw.println();
            e.printStackTrace(pw);
            pw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    private void savePhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App version:");
        pw.print(pi.versionName);
        pw.print("_");
        pw.println(pi.versionCode);
        // Android版本号 todo 究竟是手机的还是软件的？
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        // 手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        // 手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);
        // CPU架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

    public static CrashHandler getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        private static CrashHandler instance = new CrashHandler();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDefaultExceptionHander = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
}

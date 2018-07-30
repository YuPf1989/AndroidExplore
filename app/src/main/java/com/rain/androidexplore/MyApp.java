package com.rain.androidexplore;

import android.app.Application;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Author:rain
 * Date:2018/7/25 10:04
 * Description:
 */
public class MyApp extends Application {
    private static final String TAG  = "MyApp";
    private static Application instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Logger.addLogAdapter(new AndroidLogAdapter(){

            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                // 是否打印logger
                return BuildConfig.DEBUG;
            }
        });

        Log.e(TAG, "onCreate: pid:"+ Process.myPid());
    }

    public static Application getApplication() {
        return instance;
    }
}

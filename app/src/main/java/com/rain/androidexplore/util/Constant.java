package com.rain.androidexplore.util;

import android.os.Environment;

/**
 * Author:rain
 * Date:2018/7/27 16:19
 * Description:
 */
public class Constant {
    // 创建的文件夹的地址
    public static final String  FOLDER_PATH = Environment.getExternalStorageDirectory()+"/androidexplore";

    // 缓存文件地址
    public static final String CACHE_FILE_PATH = FOLDER_PATH + "/cache.txt";

    public static final int MESSAGE_FROM_CLIENT = 0;
    public static final int MESSAGE_FROM_SERVER = 1;

}

// IBinderPoolAidl.aidl
package com.rain.androidexplore;

// Declare any non-default types here with import statements

/**
* 创建binder连接池的接口
*/
interface IBinderPool {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   IBinder queryBinder(int binder_code);
}

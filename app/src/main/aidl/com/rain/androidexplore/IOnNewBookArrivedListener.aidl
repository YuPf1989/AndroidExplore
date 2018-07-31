// IOnNewBookArrivedListener.aidl
package com.rain.androidexplore;

// Declare any non-default types here with import statements
import com.rain.androidexplore.Book;

/**
* 要实现的业务场景：
* 当服务端有新书到来时，通知每一个申请提醒通知的用户
*
*/
interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            void onNewBookArrived(in Book newBook);
}

// IBookManager.aidl
package com.rain.androidexplore;

// Declare any non-default types here with import statements
// aidl文件支持的数据类型
// list只支持arrayList，map只支持hashMap，Parcelable，AIDL
// 其中自定义的parcelable对象、AIDL对象需要显式的import进来
import com.rain.androidexplore.Book;
import com.rain.androidexplore.IOnNewBookArrivedListener;

/*
* note:
*
*
*/
interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            List<Book> getBookList();

            // 在aidl中，方法中的参数除了基本数据类型，其他需要加上in（输入型参数）、out（输出型参数）、inout（输入输出型参数）
            void addBook(in Book book);

            void registerListener(in IOnNewBookArrivedListener listener);

            void unregisterListener(in IOnNewBookArrivedListener listener);
}

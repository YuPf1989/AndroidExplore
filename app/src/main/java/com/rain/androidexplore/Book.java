package com.rain.androidexplore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author:rain
 * Date:2018/7/30 10:23
 * Description:
 * 该Book类放置的位置要和aidl文件的包名保持一致，否则会出现aidl编译错误
 */
public class Book  implements Parcelable{
    public String name;
    public int bookId;


    protected Book(Parcel in) {
        name = in.readString();
        bookId = in.readInt();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(bookId);
    }


    public Book(String name, int bookId) {
        this.name = name;
        this.bookId = bookId;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}

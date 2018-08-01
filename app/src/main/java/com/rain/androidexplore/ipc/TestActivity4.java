package com.rain.androidexplore.ipc;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rain.androidexplore.Book;
import com.rain.androidexplore.R;
import com.rain.androidexplore.bean.User;

import java.util.ArrayList;

/**
 * Author:rain
 * Date:2018/7/26 10:57
 * Description:
 * 用于演示content_provider
 */
public class TestActivity4 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestActivity4";
    private TextView tv;
    Uri bookUri = Uri.parse("content://com.rain.androidexplore.content_provider.BookProvider/book");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);

        tv = findViewById(R.id.tv);
        findViewById(R.id.btn_insert).setOnClickListener(this);
        findViewById(R.id.btn_query).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);
        findViewById(R.id.btn_update).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                insert();
                break;

            case R.id.btn_delete:
                delete();
                break;

            case R.id.btn_update:
                break;

            case R.id.btn_query:
                query();
                break;
        }
    }

    private void query() {
        ArrayList<Book> bookList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.bookId = cursor.getInt(0);
            book.name = cursor.getString(1);
            Log.e(TAG, "query: name:"+cursor.getString(1));
            bookList.add(book);
        }
        cursor.close();
        tv.setText(bookList.toString());
    }

    private void delete() {
//        getContentResolver().delete(bookUri,"_id = ?",new String[])
    }

    private int newBookId = 6;

    private void insert() {
        newBookId++;
        ContentValues values = new ContentValues();
        values.put("_id", newBookId);
        values.put("name", "js"+newBookId);
        Uri uri = getContentResolver().insert(bookUri, values);
        Toast.makeText(this, "uri:"+uri, Toast.LENGTH_SHORT).show();
    }
}

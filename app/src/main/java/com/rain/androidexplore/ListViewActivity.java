package com.rain.androidexplore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author:rain
 * Date:2018/8/13 16:25
 * Description:
 * 关于listview的复用
 */
public class ListViewActivity extends AppCompatActivity {
    private static final String TAG = "ListViewActivity";

    private ListView list;
    private ArrayList<String> data = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initData();

        list = findViewById(R.id.list);
        list.setAdapter(new MyAdapter(data,this));

    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            data.add("我是第" + i + "个条目");
        }
    }

    class MyAdapter extends BaseAdapter {
        private ArrayList<String> data;
        private Context context;

        public MyAdapter(ArrayList<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // getView方法会不断调用
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                Log.e(TAG, "getView: " + i++);
                convertView = View.inflate(context, R.layout.layout_list_item, null);
                holder = new ViewHolder();
                holder.tv = convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(data.get(position));
            return convertView;
        }
    }

    private int i;

    class ViewHolder {
        TextView tv;
    }

}

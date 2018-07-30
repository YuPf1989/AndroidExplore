package com.rain.androidexplore.bean;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Author:rain
 * Date:2018/7/26 10:58
 * Description:
 */
public class User implements Serializable{
    // 序列化一般不需要重写该值，主要是辅助序列化和反序列化
    // 类在序列化时，会在文件中生成serialVersionUID，反序列化时文件中的值会与该类进行匹配，
    // 值相等才能正常反序列化
    public static final Long serialVersionUID = 1L;
    public String name;
    public int age;


    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

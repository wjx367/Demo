package com.example.administrator.demo.activity.base;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

public abstract class MapActivity extends FragmentActivity {

    public Intent startIntent;


    public void startChange(Class name){
        startIntent = new Intent();
        startIntent.setClass(this, name);
        startActivity(startIntent);
    }

    public void startChange(Class name, boolean close){
        startChange(name);
        if(close)finish();
    }


    //初始化数据
    public abstract void initData();

    //初始化视图
    public abstract  void initView();

    //初始化标题
    public abstract void initTitle();
}

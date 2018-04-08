package com.xloger.exlink.app;

import android.app.Application;

import com.xloger.exlink.app.util.FileUtil;

/**
 * Created by xloger on 1月2日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.createInstance(this);
    }
}

package com.xloger.exlink.app.util;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class MyLog {
    private MyLog(){

    }

    public static void log(String s) {
        XposedBridge.log("[ExLink] " + s);
    }
    public static void log(Throwable t) {
        XposedBridge.log(t);
    }
}

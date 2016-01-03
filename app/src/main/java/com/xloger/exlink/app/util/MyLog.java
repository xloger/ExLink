package com.xloger.exlink.app.util;

import android.util.Log;
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
        try {
            XposedBridge.log("[ExLink] " + s);
        }catch (NoClassDefFoundError error){
            Log.d("[ExLink] ",s);
        }
    }
    public static void log(Throwable t) {
        try {
            XposedBridge.log(t);
        }catch (NoClassDefFoundError error){
            Log.e("[ExLink] ",t.toString());
        }
    }
}

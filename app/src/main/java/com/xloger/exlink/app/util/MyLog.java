package com.xloger.exlink.app.util;

import android.util.Log;
import com.xloger.exlink.app.BuildConfig;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class MyLog {
    private static final boolean LOG_ON= BuildConfig.DEBUG;
    private static boolean isShowLog=true;


    private MyLog(){

    }

    public static void log(String s) {
        if (isShowLog){
            try {
                XposedBridge.log("[ExLink] " + s);
            }catch (NoClassDefFoundError error){
                if (LOG_ON){
                    Log.d("[ExLink] ",s);
                }
            }
        }
    }
    public static void log(Throwable t) {
        if (isShowLog){
            try {
                XposedBridge.log(t);
            }catch (NoClassDefFoundError error){
                if (LOG_ON){
                    Log.e("[ExLink] ",t.toString());
                }
            }
        }
    }
}

package com.xloger.exlink.app.util;

import android.util.Log;

import com.xloger.exlink.app.BuildConfig;
import com.xloger.exlink.app.Constant;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class MyLog {
    private static final boolean LOG_ON = BuildConfig.DEBUG;
    private static boolean isShowLog = true;


    private MyLog() {

    }

    private static boolean checkIsShowLog() {
        String string = "";
        try {
            string = ExConfig.INSTANCE.loadFromXposed(Constant.IS_DEBUG_FILE_NAME);
        }catch (NoClassDefFoundError ex){
            e(ex.getMessage());
        }catch (Exception ex) {
            try {
                string = ExConfig.INSTANCE.loadFromApp(Constant.IS_DEBUG_FILE_NAME);
            } catch (Exception ex2) {
                e(ex.getMessage());
            }
        }
        if (string.equals("")) {
            return false;
        }
        isShowLog = Boolean.valueOf(string);
        return isShowLog;
    }

    public static void log(String s) {

        if (checkIsShowLog()) {
            try {
                XposedBridge.log("[ExLink] " + s);
            } catch (NoClassDefFoundError error) {
                Log.d("[ExLink] ", s);
            }
        }
    }

    public static void log(Throwable t) {

        if (checkIsShowLog()) {
            try {
                XposedBridge.log(t);
            } catch (NoClassDefFoundError error) {
                Log.e("[ExLink Error] ", t.toString());
            }
        }
    }

    public static void e(String s) {
        if (isShowLog) {
            try {
                XposedBridge.log("[ExLink Error] " + s);
            } catch (NoClassDefFoundError error) {
                Log.e("[ExLink] ", s);

            }
        }
    }

    public static void logXP(String s) {
        XposedBridge.log("[ExLink] " + s);
    }
}

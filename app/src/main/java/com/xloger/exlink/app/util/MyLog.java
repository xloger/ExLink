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
        byte[] bytes = FileUtil.load(Constant.APP_URL, Constant.IS_DEBUG_FILE_NAME);
        if (bytes != null) {
            isShowLog = Boolean.valueOf(new String(bytes));
        }
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
        if (true) {
            try {
                XposedBridge.log("[ExLink Error] " + s);
            } catch (NoClassDefFoundError error) {
                Log.e("[ExLink] ", s);

            }
        }
    }
}

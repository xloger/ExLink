package com.xloger.exlink.app;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.xloger.exlink.app.util.MyLog;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by xloger on 1月2日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        String myPackageName=this.getClass().getPackage().getName();
        MyLog.log("fuck::::" + myPackageName);

//        XSharedPreferences sharedPreferences=new XSharedPreferences(
//                myPackageName, "prefs");
//        SharedPreferences.Editor edit = sharedPreferences.edit();
//        edit.putString("123","123");
//        edit.apply();
//
//        MyLog.log(lpparam.packageName);
//        String string="123456";
//        FileUtil.getInstance().save("123",string.getBytes());

        if(!lpparam.packageName.equals("com.tencent.mobileqq"))
            return;
        findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, xc_methodHook);
    }

    XC_MethodHook xc_methodHook=new XC_MethodHook(){
        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

            String myPackageName=this.getClass().getPackage().getName();
            MyLog.log("fuck::::");

            String activityName = param.thisObject.getClass().getName();
            MyLog.log("Started activity: " + activityName);
            MyLog.log("23333333333333333333");
            if(!activityName.equals("com.123"))
                return;
            Intent intent = (Intent)param.args[0];
            MyLog.log("Intent: " + intent.toString());
            MyLog.log(" - With extras: " + intent.getExtras().toString());
            String externalUrl = intent.getStringExtra("url");
            MyLog.log("Get a internal url: " + externalUrl);
            Uri uri = Uri.parse(externalUrl);
            Intent exIntent = new Intent();
            exIntent.setAction(Intent.ACTION_VIEW);
            exIntent.setData(uri);
            ((Activity)param.thisObject).startActivity(exIntent);
            param.setResult(null); // prevent opening internal browser
        }
    };
}


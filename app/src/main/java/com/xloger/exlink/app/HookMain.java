package com.xloger.exlink.app;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.xloger.exlink.app.util.FileUtil;
import com.xloger.exlink.app.util.MyLog;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
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

        XSharedPreferences sharedPreferences=new XSharedPreferences(
                myPackageName, "123");
        String string = sharedPreferences.getString("123", "456");
        MyLog.log("快看这是个啥："+string);

        MyLog.log(lpparam.packageName);
        if(!lpparam.packageName.equals("com.tencent.mobileqq"))
            return;
        findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, xc_methodHook);
    }

    XC_MethodHook xc_methodHook=new XC_MethodHook(){
        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {


            String activityName = param.thisObject.getClass().getName();
            MyLog.log("Started activity: " + activityName);
            if(!activityName.equals("com.tencent.mobileqq.activity.QQBrowserDelegationActivity"))
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


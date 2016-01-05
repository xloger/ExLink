package com.xloger.exlink.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;
import com.xloger.exlink.app.util.MyLog;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.util.List;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by xloger on 1月2日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class HookMain implements IXposedHookLoadPackage {
    private int index;
    private static List<App> appList=null;

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (appList == null||appList.size()==0) {
//            Object appObject = FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
//            if (appObject != null) {
//                appList = (List<App>) appObject;
//                MyLog.log("天灵灵地灵灵：" + appList.toString());
//
//            }else {
//                MyLog.log("appObject is null");
//                return;
//            }
            appList=FileUtil.getAppList();
        }

        for (int i = 0; i < appList.size(); i++) {
            if(lpparam.packageName.equals(appList.get(i).getPackageName())&&appList.get(i).isUse()){
                index=i;
                findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, xc_methodHook);
                break;
            }
        }

    }

    XC_MethodHook xc_methodHook=new XC_MethodHook(){
        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {

            String activityName = param.thisObject.getClass().getName();
            MyLog.log("Started activity: " + activityName);
            if(!activityName.equals(appList.get(index).getActivityName()))
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


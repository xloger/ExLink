package com.xloger.exlink.app.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import com.xloger.exlink.app.entity.AndroidApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 16/6/1 下午5:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class AndroidAppUtil {
    public static List<AndroidApp> getAllAppInfo(Context context){
       return  getAppInfo(context,0);
    }

    public static List<AndroidApp> getUserAppInfo(Context context){
        List<AndroidApp> appList=new ArrayList<AndroidApp>();
        List<AndroidApp> tempList=getAllAppInfo(context);
        for (AndroidApp app : tempList) {
            if (!app.isSystemApp()){
                appList.add(app);
            }
        }
        return appList;
    }

    public static List<AndroidApp> getAppInfo(Context context,int mode){
        List<AndroidApp> appList=new ArrayList<AndroidApp>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(mode);
        for(PackageInfo info:packageInfoList){
            AndroidApp app=new AndroidApp();
            String packageName = info.packageName;
            ApplicationInfo appInfo = info.applicationInfo;
            Drawable icon = appInfo.loadIcon(packageManager);
            String appName = appInfo.loadLabel(packageManager).toString();
            boolean isSystemApp=isSystemApp(appInfo);

            app.setName(appName);
            app.setPackageName(packageName);
            app.setIcon(icon);
            app.setSystemApp(isSystemApp);
            MyLog.log("名字:"+appName+",是系统应用"+isSystemApp);

            appList.add(app);
        }
        return appList;
    }

    public static boolean isSystemApp(ApplicationInfo info){
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            return false;
        }else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0){//判断是不是系统应用
            return false;
        }
        return true;
    }
}

package com.xloger.exlink.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.entity.Rule;
import com.xloger.exlink.app.util.FileUtil;
import com.xloger.exlink.app.util.MyLog;
import com.xloger.exlink.app.util.StreamUtil;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.net.URLDecoder;
import java.util.List;
import java.util.Set;

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
            App app = appList.get(i);
            if(lpparam.packageName.equals(app.getPackageName())&& app.isUse()){
                index=i;
                MyLog.log("进入"+ app.getAppName()+"("+ app.getPackageName()+")");
                findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, xc_methodHook);
                break;
            }
        }

    }

    XC_MethodHook xc_methodHook=new XC_MethodHook(){
        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            App app = appList.get(index);

            //如果该应用正处于“匹配模式”，则执行sendToExLink方法
            if (app.isTest()){
                MyLog.log("进入匹配模式！");
                sendToExLink(param);
                return;
            }

            //判断该Activity是否处于规则之中
            String activityName = param.thisObject.getClass().getName();
            MyLog.log("Started activity: " + activityName);
            Set<Rule> ruleSet = app.getRules();
            Rule rule = null;
            boolean activityIsMatch=false;
            for (Rule tempRule : ruleSet) {
                if (activityName.equals(tempRule.getActivityName())){
                    activityIsMatch=true;
                    rule=tempRule;
                    break;
                }
            }
            if(!activityIsMatch)
                return;

            //分析获取的Intent
            Intent intent = (Intent)param.args[0];
            MyLog.log("Intent: " + intent.toString());
            MyLog.log(" - With extras: " + intent.getExtras().toString());
            String externalUrl = intent.getStringExtra(rule.getExtrasKey());
            MyLog.log("externalUrl:"+externalUrl);

            //匹配奇葩
//            if (externalUrl == null) {
//                MyLog.log("_(:з)∠)_知乎你为嘛这么奇葩......");
//                Bundle bundleExtra = intent.getBundleExtra(rule.getExtrasKey());
//                if (bundleExtra != null) {
//                    Set<String> keySet = bundleExtra.keySet();
//                    for (String key : keySet) {
//                        Object o = bundleExtra.get(key);
//                        String value=o.toString();
//                        if (value.startsWith("http")){
//                            externalUrl=value;
//                            MyLog.log("匹配奇葩规则成功");
//                        }
//                    }
//                }
//            }

            if (externalUrl == null) {
                Bundle bundleExtra = intent.getBundleExtra(rule.getExtrasKey());
                if (bundleExtra != null) {
                    externalUrl= bundleExtra.toString();
                }
                if (externalUrl==null||!externalUrl.contains("http")){
                    externalUrl=intent.toUri(0);
                    return;
                }
            }

            if (externalUrl == null||"".equals(externalUrl)) {
                throw new Exception("无法获取url");
            }

            //Url规范化
            if (!externalUrl.startsWith("http")){
                MyLog.log("Url不符合规范，正在二次分析");
                MyLog.log("当前externalUrl:"+externalUrl);
                externalUrl= URLDecoder.decode(externalUrl);
                externalUrl= StreamUtil.parseUrl(externalUrl);
                MyLog.log("处理后externalUrl:"+externalUrl);
            }

            Uri uri = Uri.parse(externalUrl);
            MyLog.log("uri:"+uri);

            //检查白名单
            Set<String> whiteUrl = app.getWhiteUrl();
            if (whiteUrl != null) {
                for (String s : whiteUrl) {
                    if (uri.getHost().equals(s)){
                        MyLog.log("处于白名单之中，跳过");
                        return;
                    }
                }
            }

            if (externalUrl != null&&externalUrl.startsWith("http")) {

                //发送干净的Intent
                Intent exIntent = new Intent();
                exIntent.setAction(Intent.ACTION_VIEW);
                exIntent.setData(uri);
                ((Activity)param.thisObject).startActivity(exIntent);
                param.setResult(null); // prevent opening internal browser

            }
        }

        private void sendToExLink(XC_MethodHook.MethodHookParam param) {
            Intent intent = (Intent)param.args[0];
            MyLog.log("Intent: " + intent.toString());
            MyLog.log(" - With extras: " + intent.getExtras().toString());
            Bundle extras = intent.getExtras();
            Set<String> keySet = extras.keySet();
            for (String key : keySet) {
                Object o = extras.get(key);
                String value=o.toString();
//                if (o instanceof String){
//                    value= (String) o;
//                }else {
//                    continue;
//                }

                //微博特殊匹配
                boolean weiboRule=false;
                if (appList.get(index).getPackageName().equals("com.sina.weibo")){
                    if (value.contains("http://t.cn")){
                        weiboRule=true;
                    }
                }
                if ((value.contains("www.example.org")&&value.contains("ex-link-test"))||weiboRule){
                    MyLog.log("成功匹配！");
                    Uri uri = Uri.parse("exlink://test");
                    String activityName = param.thisObject.getClass().getName();

                    Intent sendToExLinkIntent=new Intent();
                    sendToExLinkIntent.setAction(Intent.ACTION_VIEW);
                    sendToExLinkIntent.setData(uri);
                    sendToExLinkIntent.putExtra("activityName",activityName);
                    sendToExLinkIntent.putExtra("extrasKey",key);
                    sendToExLinkIntent.putExtra("position",index);
                    ((Activity)param.thisObject).startActivity(sendToExLinkIntent);
                    param.setResult(null);
                }
            }

        }
    };
}


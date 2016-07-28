package com.xloger.exlink.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.entity.Rule;
import com.xloger.exlink.app.util.AppUtil;
import com.xloger.exlink.app.util.MyLog;
import com.xloger.exlink.app.util.StreamUtil;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * Created by xloger on 1月2日.
 * Author:xloger
 * Email:phoenix@xloger.com
 * 卧槽这代码复杂度简直了......你们想看的话自求多福吧Orz，当初我写的什么鬼啊......
 */
public class HookMain implements IXposedHookLoadPackage {
    private int index;
    private static List<App> appList=null;
    private final static String EX_DAT ="ExDat";

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (appList == null||appList.size()==0) {
            appList= AppUtil.getAppList();
        }
        if (appList == null) {
            return;
        }

        for (int i = 0; i < appList.size(); i++) {
            App app = appList.get(i);
            if(lpparam.packageName.equals(app.getPackageName())&& app.isUse()){
                index=i;
                MyLog.log("进入"+ app.getAppName()+"("+ app.getPackageName()+")");
//                findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, xc_methodHook);
                findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class,xc_methodHook);
                break;
            }
        }

    }

    private XC_MethodHook xc_methodHook=new XC_MethodHook(){

        @Override
        protected void beforeHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
            boolean booleanExtra = ((Intent) param.args[0]).getBooleanExtra("exlink", false);
            if (booleanExtra){
                MyLog.log("递归，不处理");
                return;
            }

            App app = appList.get(index);
            String exUrl =null;

            //如果该应用正处于“匹配模式”，则执行sendToExLink方法
            if (app.isTest()){
                MyLog.log("进入匹配模式！");
                sendToExLink(param);
                return;
            }

            //判断该Activity是否已经处于规则之中
            String activityName = param.thisObject.getClass().getName();
            Set<Rule> ruleSet = app.getRules();
            List<Rule> ruleList=new ArrayList<Rule>();
            for (Rule tempRule : ruleSet) {
                if (activityName.equals(tempRule.getActivityName())){
                    ruleList.add(tempRule);
                }
            }
            if(ruleList.size()==0) {
                return;
            }
            MyLog.log("Started activity: " + activityName);

            //分析获取的Intent
            Intent intent = (Intent)param.args[0];
            MyLog.log("Intent: " + intent.toString());
            boolean isDatNull=false;
            do {
                if (StreamUtil.isContain(ruleList, EX_DAT)&&!isDatNull){
                    if (intent.getData() != null) {
                        exUrl=intent.getData().toString();
                        isDatNull=false;
                    }else {
                        isDatNull=true;
                    }
                }else {
                    Bundle extras=intent.getExtras();
                    if (!StreamUtil.isContainUrl(extras.toString())){
                        MyLog.log("Extras不对，换一个");
                        if (param.args.length>2&&param.args[2]!=null){
                            extras=(Bundle)param.args[2];
                            if (!StreamUtil.isContainUrl(extras.toString())){
                                MyLog.log("还是不对，不处理了...");
                                return;
                            }
                        }else {
                            MyLog.log("没Bundle...");
                            return;
                        }
                    }
                    MyLog.log(" - With extras: " + extras.toString());
                    int trueUrl=0;
                    for (Rule rule : ruleList) {
                        String tempUrl=extras.getString(rule.getExtrasKey());
                        if (tempUrl!=null&&!"".equals(tempUrl)){
                            MyLog.log("tempUrl:"+tempUrl);
                            exUrl=tempUrl;
                            trueUrl++;
                        }
                    }
                    if (trueUrl>1){
                        MyLog.log("Error：好像遇到多个链接的我无法处理的情况了_(:з)∠)_");
                    }
                    isDatNull=false;
                }
            }while (isDatNull);

            //intent.toUri(0);这个还没测试有没有效果

            if (exUrl == null||"".equals(exUrl)) {
                MyLog.log("Error：无法获取url");
                return;
            }

            //Url规范化
            if (!exUrl.startsWith("http")){
                MyLog.log("Url不符合规范，正在二次分析");
                MyLog.log("当前externalUrl:"+exUrl);
                exUrl=StreamUtil.clearUrl(exUrl);
                MyLog.log("处理后externalUrl:"+exUrl);

                if (exUrl == null) {
                    MyLog.log("Error：无法解析url");
                    return;
                }
            }

            Uri uri = Uri.parse(exUrl);
            MyLog.log("uri:"+uri);


            //检查白名单
            Set<String> whiteUrl = app.getWhiteUrl();
            if (whiteUrl != null) {
                for (String s : whiteUrl) {
                    if (s.equals(uri.getHost())){
                        MyLog.log("处于白名单之中，跳过");
                        return;
                    }else if (StreamUtil.isSecondLevelDomain(s,uri.getHost())){
                        MyLog.log("为"+s+"的二级域名，跳过");
                        return;
                    }
                }
            }

            if (appList.get(index).getPackageName().equals("com.tencent.mm")){
                MyLog.log("进入匹配微信模式");
                compatibleWeChat(param,uri);
                return;
            }


            //发送干净的Intent
            Intent exIntent = new Intent();
            exIntent.setAction(Intent.ACTION_VIEW);
            exIntent.setData(uri);
            exIntent.putExtra("exlink",true);
            ((Activity)param.thisObject).startActivity(exIntent);
            param.setResult(null);

        }

        /**
         * 匹配模式 <br/>
         * 判断哪个值包含了Url，并发送到Exlink
         * @param param 传入的参数
         */
        private void sendToExLink(XC_MethodHook.MethodHookParam param) {

            Intent intent = (Intent)param.args[0];
            MyLog.log("Intent: " + intent.toString());

            Uri data = intent.getData();
            if (data!=null){
                if (StreamUtil.isContain(data.toString())){
                    openExlink(param, EX_DAT);
                    return;
                }
            }

            Bundle extras = intent.getExtras();
            if (extras==null){
                MyLog.log("没有Extras，采用第二套方案");
                    if (param.args.length>2){
                        extras= (Bundle) param.args[2];
                        if (extras != null) {
                            MyLog.log("第二套:"+extras.toString());
                        }else {
                            MyLog.log("第三套方案失败，跳过");
                            return;
                        }
                    }else {
                        MyLog.log("第三套方案失败，跳过");
                        return;
                    }

            }else {
                MyLog.log(" - With extras: " + extras.toString());
            }
            Set<String> keySet = extras.keySet();
            for (String key : keySet) {
                Object o = extras.get(key);
                if (o == null) {
                    continue;
                }
                String value=o.toString();

                if (StreamUtil.isContain(value)){
                    openExlink(param,key);
                }
            }

        }

        private void openExlink(XC_MethodHook.MethodHookParam param,String key){
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

        /**
         * 兼容微信 <br/>
         * 对微信进行特殊处理
         * @param param 传入的参数
         * @param uri 需要被打开的链接
         */
        private void compatibleWeChat(XC_MethodHook.MethodHookParam param,Uri uri){
            boolean isOpenWithOut1=false;
            boolean isOpenWithOut2=false;
            boolean isOpenWithOut3=false;

            Intent intent = (Intent)param.args[0];
            String judge1 = intent.getStringExtra("preChatName");
            String judge2 = intent.getStringExtra("prePublishId");
            String judge3 = intent.getStringExtra("preUsername");
            MyLog.log("judge1:"+judge1+"judge2:"+judge2+"judge3:"+judge3);

            if (judge1==null&&judge2==null&&judge3==null){
                isOpenWithOut1=true;
            }

            String judge4 = intent.getStringExtra("version_name");
            String judge5 = intent.getStringExtra("KAppId");
            String judge6 = intent.getStringExtra("srcDisplayname");
            MyLog.log("judge4:"+judge4+"judge5:"+judge5+"judge6:"+judge6);

            if (judge4!=null&&judge5!=null&&judge6==null){
                isOpenWithOut2=true;
            }

            String judge7 = intent.getStringExtra("srcUsername");
            String judge8 = intent.getStringExtra("message_index");
            String judge9 = intent.getStringExtra("message_id");
            MyLog.log("judge7:"+judge7+"judge8:"+judge8+"judge9:"+judge9);

            if (judge7==null&&judge8==null&&judge9==null){
                isOpenWithOut3=true;
            }


            if (isOpenWithOut1||isOpenWithOut2||isOpenWithOut3){
                MyLog.log("判断可以用外置浏览器打开");
                Intent exIntent = new Intent();
                exIntent.setAction(Intent.ACTION_VIEW);
                exIntent.setData(uri);
                ((Activity)param.thisObject).startActivity(exIntent);
                param.setResult(null);
            }else {
                MyLog.log("判断需要用内置浏览器打开");
            }
        }
    };
}


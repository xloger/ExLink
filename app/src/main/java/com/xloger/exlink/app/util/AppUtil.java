package com.xloger.exlink.app.util;

import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.entity.Rule;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Created by xloger on 7月13日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class AppUtil {
    private static List<App> appList;

    public static List<App> getAppList(){
        if (appList == null) {
            appList= (List<App>) FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
//            if (appList == null) {
//                appList=initAppData();
//                save(appList);
//            }
        }
        return appList;
    }

    private static void save(){
        save(appList);
    }

    public static void save(List<App> appList){
        FileUtil.saveObject(Constant.APP_URL,Constant.APP_FILE_NAME, appList);
        AppUtil.appList=appList;
    }

    private static List<App> initAppData(){
        MyLog.log("进行了初始化规则");
        List<App> apps=new LinkedList<App>();
        App qq=new App();
        qq.setAppName("QQ");
        qq.setPackageName("com.tencent.mobileqq");
        Set<Rule> qqRule=new HashSet<Rule>();
        qqRule.add(new Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity","url"));
        qq.setRules(qqRule);
        Set<String> qqWhiteUrl=new HashSet<String>();
//        qqWhiteUrl.add("h5.qzone.qq.com");
//        qqWhiteUrl.add("qzs.qzone.qq.com");
        qqWhiteUrl.add("qzone.qq.com");
        qqWhiteUrl.add("qun.qq.com");
        qqWhiteUrl.add("jq.qq.com");
        qqWhiteUrl.add("mqq.tenpay.com");
        qqWhiteUrl.add("mp.qq.com");
        qqWhiteUrl.add("yundong.qq.com");
        qq.setWhiteUrl(qqWhiteUrl);
        qq.setIsUse(true);
        qq.setIsUserBuild(false);
        apps.add(qq);

        App qqLite=new App();
        qqLite.setAppName("QQ轻聊版");
        qqLite.setPackageName("com.tencent.qqlite");
        Set<Rule> qqLiteRule=new HashSet<Rule>();
        qqLiteRule.add(new Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity","url"));
        qqLite.setRules(qqLiteRule);
        Set<String> qqLiteWhiteUrl=new HashSet<String>();
        qqLiteWhiteUrl.add("qun.qq.com");
        qqLiteWhiteUrl.add("jq.qq.com");
        qqLiteWhiteUrl.add("mqq.tenpay.com");
        qqLiteWhiteUrl.add("mp.qq.com");
        qqLite.setWhiteUrl(qqLiteWhiteUrl);
        qqLite.setIsUse(true);
        qqLite.setIsUserBuild(false);
        apps.add(qqLite);

        App qqi=new App();
        qqi.setAppName("QQ国际版");
        qqi.setPackageName("com.tencent.mobileqqi");
        Set<Rule> qqiRule=new HashSet<Rule>();
        qqiRule.add(new Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity", "url"));
        qqi.setRules(qqiRule);
        qqi.setIsUse(true);
        qqi.setIsUserBuild(false);
        apps.add(qqi);

        App tieba=new App();
        tieba.setAppName("百度贴吧");
        tieba.setPackageName("com.baidu.tieba");
        Set<Rule> tiebaRule=new HashSet<Rule>();
        tiebaRule.add(new Rule("com.baidu.tieba.imMessageCenter.im.chat.PersonalChatActivity","tag_url"));
        tiebaRule.add(new Rule("com.baidu.tieba.pb.pb.main.PbActivity","tag_url"));
        tieba.setRules(tiebaRule);
        tieba.setIsUse(true);
        tieba.setIsUserBuild(false);
        apps.add(tieba);

        App weibo=new App();
        weibo.setAppName("微博");
        weibo.setPackageName("com.sina.weibo");
        Set<Rule> weiboRule=new HashSet<Rule>();
        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.weiyou.DMSingleChatActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.page.NewCardListActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.feed.DetailWeiboActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","ExDat"));
        weiboRule.add(new Rule("com.sina.weibo.feed.DetailWeiboActivity","ExDat"));
        weibo.setRules(weiboRule);
        Set<String> weiboWhiteUrl=new HashSet<String>();
        weiboWhiteUrl.add("card.weibo.com");
        weibo.setWhiteUrl(weiboWhiteUrl);
        weibo.setIsUse(true);
        weibo.setIsUserBuild(false);
        apps.add(weibo);

        App weChat=new App();
        weChat.setAppName("微信");
        weChat.setPackageName("com.tencent.mm");
        Set<Rule> weChatRule=new HashSet<Rule>();
        weChatRule.add(new Rule("com.tencent.mm.ui.LauncherUI","rawUrl"));
        weChat.setRules(weChatRule);
        Set<String> weChatWhiteUrl=new HashSet<String>();
        weChatWhiteUrl.add("open.weixin.qq.com");
        weChatWhiteUrl.add("weixin.qq.com");
        weChat.setWhiteUrl(weChatWhiteUrl);
        weChat.setIsUse(true);
        weChat.setIsUserBuild(false);
        apps.add(weChat);
        return apps;
    }

    /**
     * 初始化与更新规则，并且保存
     */
    public static void updateAppData(){
        appList=getAppList();
        if (appList == null) {
            appList=initAppData();
            save();
            return;
        }
        List<App> newAppList=initAppData();

        for (int i = 0; i < 6; i++) {
            Set<Rule> ruleSet = appList.get(i).getRules();
            Set<Rule> newRuleSet = newAppList.get(i).getRules();
            if (ruleSet==null){
                ruleSet=new HashSet<Rule>();
                appList.get(i).setRules(ruleSet);
            }
            if (newRuleSet != null) {
                ruleSet.addAll(newRuleSet);
            }

            Set<String> whiteUrl = appList.get(i).getWhiteUrl();
            Set<String> newWhiteUrl = newAppList.get(i).getWhiteUrl();
            if (whiteUrl==null){
                whiteUrl=new HashSet<String>();
                appList.get(i).setWhiteUrl(whiteUrl);
            }
            if (newWhiteUrl != null) {
                whiteUrl.addAll(newWhiteUrl);
            }
        }

        save();
    }
}

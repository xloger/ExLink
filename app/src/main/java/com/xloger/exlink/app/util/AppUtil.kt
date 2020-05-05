package com.xloger.exlink.app.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.entity.AppList
import com.xloger.exlink.app.entity.Rule
import java.util.*


/**
 * Created by xloger on 7月13日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
class AppUtil {
//    var appList: List<App> = Delegates.notNull<>()
//    private var size: Int = 0

//    fun getAppList(): MutableList<App> {
//            var appList = FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME) as List<App>
//            //            if (appList == null) {
//            //                appList=initAppData();
//            //                save(appList);
//            //            }
//        return appList
//    }
//
//    private fun save() {
//        save(appList)
//    }
//
//    fun save(appList: List<App>) {
//        FileUtil.saveObject(Constant.APP_URL, Constant.APP_FILE_NAME, appList)
//        AppUtil.appList = appList
//    }

    fun initAppData(): MutableList<App> {
        MyLog.log("进行了初始化规则")
        val apps = LinkedList<App>()
        val qq = App(
                appName = "QQ",
                packageName = "com.tencent.mobileqq",
                rules = setOf(Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity", "url")),
                whiteUrl = setOf("qzone.qq.com", "qun.qq.com", "jq.qq.com", "mqq.tenpay.com", "mp.qq.com", "yundong.qq.com"),
                isUse = true,
                isUserBuild = false,
                isTest = false
        )
        apps.add(qq)
        val qqLite = qq.copy(
                appName = "QQ轻聊版",
                packageName = "com.tencent.qqlite"
        )
        apps.add(qqLite)

        val qqi = qq.copy(
                appName = "QQ国际版",
                packageName = "com.tencent.mobileqqi"
        )
        apps.add(qqi)

        val tim = qq.copy(
                appName = "TIM",
                packageName = "com.tencent.tim"
        )
        apps.add(tim)

        val tieba = App(
                appName = "百度贴吧",
                packageName = "com.baidu.tieba",
                rules = setOf(Rule("com.baidu.tieba.imMessageCenter.im.chat.PersonalChatActivity", "tag_url"),
                        Rule("com.baidu.tieba.pb.pb.main.PbActivity", "tag_url")),
                whiteUrl = HashSet<String>(),
                isUserBuild = false
        )
        apps.add(tieba)

        val weibo = App(appName = "微博",
                packageName = "com.sina.weibo",
                rules = setOf(Rule("com.sina.weibo.feed.HomeListActivity", "com_sina_weibo_weibobrowser_url"),
                        Rule("com.sina.weibo.feed.HomeListActivity", "com_sina_weibo_weibobrowser_url"),
                        Rule("com.sina.weibo.weiyou.DMSingleChatActivity", "com_sina_weibo_weibobrowser_url"),
                        Rule("com.sina.weibo.page.NewCardListActivity", "com_sina_weibo_weibobrowser_url"),
                        Rule("com.sina.weibo.feed.DetailWeiboActivity", "com_sina_weibo_weibobrowser_url"),
                        Rule("com.sina.weibo.feed.HomeListActivity", "com_sina_weibo_weibobrowser_url"),
                        Rule("com.sina.weibo.feed.HomeListActivity", "ExDat"),
                        Rule("com.sina.weibo.feed.DetailWeiboActivity", "ExDat")
                ),
                whiteUrl = setOf("card.weibo.com"),
                isUserBuild = false
        )
        apps.add(weibo)

        val weChat = App(
                appName = "微信",
                packageName = "com.tencent.mm",
                rules = setOf(Rule("com.tencent.mm.ui.LauncherUI", "rawUrl"),
                        Rule("com.tencent.mm.plugin.sns.ui.SnsTimeLineUI", "rawUrl")),
//                whiteUrl = setOf("open.weixin.qq.com", "weixin.qq.com"),
                isUserBuild = false
        )
        apps.add(weChat)

        return apps
    }

    fun addJson(localAppList: MutableList<App>, string: String): Boolean {
        if (string.isNullOrEmpty()) {
            return false
        }
        val importAppList: AppList = try {
            Gson().fromJson(string, AppList::class.java)
        } catch (e: JsonSyntaxException) {
            MyLog.e("传入的 json 异常：${string}")
            MyLog.e("传入的 json 异常：${e.message}")
            AppList(mutableListOf())
            return false
        }

//        val localAppList = JSONFile().getJson()
//        val mutableList = localAppList.toMutableList()

        if (importAppList == null || importAppList.list == null) {
            MyLog.e("list 为 null：${importAppList.toString()}")
            return false
        }

        for (newApp in importAppList.list) {
            var isAdd = false
            for (localApp in localAppList) {
                if (localApp.packageName == newApp.packageName) {
                    if (localApp.rules == null) {
                        localApp.rules = newApp.rules
                    } else {
                        val mutableSet = localApp.rules.toMutableSet()
                        mutableSet.addAll(newApp.rules)
                        localApp.rules = mutableSet.toSet()
                    }
                    if (localApp.whiteUrl == null) {
                        localApp.whiteUrl = newApp.whiteUrl
                    } else {
                        val mutableSet = localApp.whiteUrl.toMutableSet()
                        mutableSet.addAll(newApp.whiteUrl)
                        localApp.whiteUrl = mutableSet
                    }
                    isAdd = true
                }
            }
            if (!isAdd) {
                localAppList.add(newApp)
            }

        }

//        MyLog.log("保存${mutableList.toSimpleString()}")
//        localAppList.clear()
//        localAppList.addAll(mutableList)
//        JSONFile().saveJson(mutableList.toList())
        return true

    }

    fun exportJson(appList: MutableList<App> = JSONFile.getJson()): String {
        return Gson().toJson(AppList(appList))
    }

    companion object {
        val INSTANCE = AppUtil()
    }

    /**
     * 初始化与更新规则，并且保存
     */
//    fun updateAppData() {
//        appList = getAppList()
//        if (appList == null) {
//            appList = initAppData()
//            save()
//            return
//        }
//        val newAppList = initAppData()
//        //什么智障代码
//        //        for (int i = 0; i < size; i++) {
//        //            Set<Rule> ruleSet = appList.get(i).getRules();
//        //            Set<Rule> newRuleSet = newAppList.get(i).getRules();
//        //            if (ruleSet == null) {
//        //                ruleSet = new HashSet<Rule>();
//        //                appList.get(i).setRules(ruleSet);
//        //            }
//        //            if (newRuleSet != null) {
//        //                ruleSet.addAll(newRuleSet);
//        //            }
//        //
//        //            Set<String> whiteUrl = appList.get(i).getWhiteUrl();
//        //            Set<String> newWhiteUrl = newAppList.get(i).getWhiteUrl();
//        //            if (whiteUrl == null) {
//        //                whiteUrl = new HashSet<String>();
//        //                appList.get(i).setWhiteUrl(whiteUrl);
//        //            }
//        //            if (newWhiteUrl != null) {
//        //                whiteUrl.addAll(newWhiteUrl);
//        //            }
//        //        }
//
//        for (newApp in newAppList) {
//            var isAdd = false
//            for (userApp in appList!!) {
//                if (userApp.packageName == newApp.packageName) {
//                    if (userApp.rules == null) {
//                        userApp.rules = newApp.rules
//                    } else {
//                        userApp.rules!!.addAll(newApp.rules!!)
//                    }
//                    if (userApp.whiteUrl == null) {
//                        userApp.whiteUrl = newApp.whiteUrl
//                    } else {
//                        userApp.whiteUrl!!.addAll(newApp.whiteUrl!!)
//                    }
//                    isAdd = true
//                }
//            }
//            if (!isAdd) {
//                appList!!.add(newApp)
//            }
//
//        }
//
//        save()
//    }
}

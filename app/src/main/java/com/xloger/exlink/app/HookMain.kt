package com.xloger.exlink.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.entity.Rule
import com.xloger.exlink.app.util.*
import com.xloger.xlib.tool.Xlog

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

import java.util.ArrayList

import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.XposedHelpers.getObjectField
import kotlin.properties.Delegates

/**
 * Created by xloger on 1月2日.
 * Author:xloger
 * Email:phoenix@xloger.com
 * 卧槽这代码复杂度简直了......你们想看的话自求多福吧Orz，当初我写的什么鬼啊......
 */
class HookMain : IXposedHookLoadPackage {
    private var index: Int = 0

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: LoadPackageParam) {

//        if (appList == null || appList.isEmpty()) {
            appList = JSONFile().getJson()
        MyLog.log("初始化")
//        }

        findAndHookMethod("com.xloger.exlink.app.util.Tool", lpparam.classLoader, "isHook", object : XC_MethodHook() {
            override fun beforeHookedMethod (param: MethodHookParam?) {
                MyLog.log("hook 测试")
                param?.result = true
            }

            override fun afterHookedMethod(param: MethodHookParam?) {
                MyLog.log("hook 测试")
                param?.result = true
            }

        })

        for (i in appList.indices) {
            val (appName, packageName, _, _, isUse) = appList[i]
            if (lpparam.packageName == packageName && isUse) {
                index = i
                MyLog.log("进入$appName($packageName)")
                //                findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, xc_methodHook);
                findAndHookMethod(Activity::class.java, "startActivityForResult", Intent::class.java, Int::class.javaPrimitiveType, Bundle::class.java, xc_methodHook)
                break
            }
        }

    }

    private val xc_methodHook = object : XC_MethodHook() {

        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam?) {
            val booleanExtra = (param!!.args[0] as Intent).getBooleanExtra("exlink", false)
            if (booleanExtra) {
                MyLog.log("递归，不处理")
                return
            }

            val (_, packageName, ruleSet, whiteUrl, _, _, isTest) = appList[index]

            //如果该应用正处于“匹配模式”，则执行sendToExLink方法
            if (isTest) {
                MyLog.log("进入匹配模式！")
                sendToExLink(param)
                return
            }

            //判断该Activity是否已经处于规则之中
            val activityName = param.thisObject.javaClass.name
            val ruleList = ArrayList<Rule>()
            for (tempRule in ruleSet) {
                if (activityName == tempRule.activityName) {
                    ruleList.add(tempRule)
                }
            }
            if (ruleList.size == 0) {
                return
            }
            MyLog.log("Started activity: " + activityName)

            //分析获取的Intent
            val intent = param.args[0] as Intent
            MyLog.log("Intent: " + intent.toString())
//            var isDatNull = false
//            do {
//                if (StreamUtil.isContain(ruleList, EX_DAT) && !isDatNull) {
//                    if (intent.data != null) {
//                        exUrl = intent.data!!.toString()
//                        isDatNull = false
//                    } else {
//                        isDatNull = true
//                    }
//                } else {
//                    var extras = intent.extras
//                    if (extras == null || !StreamUtil.isContainUrl(extras.toString())) {
//                        MyLog.log("Extras不对，换一个")
//                        if (param.args.size > 2 && param.args[2] != null) {
//                            extras = param.args[2] as Bundle
//                            if (!StreamUtil.isContainUrl(extras.toString())) {
//                                MyLog.log("还是不对，不处理了...")
//                                return
//                            }
//                        } else {
//                            MyLog.log("没Bundle...")
//                            return
//                        }
//                    }
//                    MyLog.log(" - With extras: " + extras.toString())
//                    var trueUrl = 0
//                    for (rule in ruleList) {
//                        val tempUrl = extras.getString(rule.extrasKey)
//                        if (tempUrl != null && "" != tempUrl) {
//                            MyLog.log("tempUrl:" + tempUrl)
//                            exUrl = tempUrl
//                            trueUrl++
//                        }
//                    }
//                    if (trueUrl > 1) {
//                        MyLog.log("Error：好像遇到多个链接的我无法处理的情况了_(:з)∠)_")
//                    }
//                    isDatNull = false
//                }
//            } while (isDatNull)

            //intent.toUri(0);这个还没测试有没有效果

            val exUrlList = ruleList
                    .map { parseUrl(it, param) }
                    .filter { it.isNotBlank() }

            if (exUrlList.isEmpty()) {
                MyLog.e("Error：无法获取url")
                return
            } else if (exUrlList.size > 1) {
                MyLog.e("遇到多个 url 的特殊情况，暂不处理。$exUrlList")
                return
            }
            var exUrl: String = exUrlList[0]

            //Url规范化
            if (!exUrl.startsWith("http")) {
                MyLog.log("Url不符合规范，正在二次分析")
                MyLog.log("当前externalUrl:" + exUrl)
                exUrl = StreamUtil.clearUrl(exUrl)
                MyLog.log("处理后externalUrl:" + exUrl)

                if (exUrl.isBlank()) {
                    MyLog.log("Error：无法解析url")
                    return
                }
            }

            val uri = Uri.parse(exUrl)

            //检查白名单
            for (s in whiteUrl) {
                if (s == uri.host) {
                    MyLog.log("处于白名单之中，跳过")
                    return
                } else if (StreamUtil.isSecondLevelDomain(s, uri.host)) {
                    MyLog.log("为" + s + "的二级域名，跳过")
                    return
                }
            }


            if (packageName == "com.tencent.mm") {
                MyLog.log("进入匹配微信模式")
                compatibleWeChat(param, uri)
                return
            }

            openUrl(param, uri)
        }

        private fun parseUrl(rule: Rule, param: XC_MethodHook.MethodHookParam): String {
            val intent = param.args[0] as Intent
            if (rule.extrasKey == EX_DAT) {
                return intent.dataString
            } else {
                val bundle = getBundle(param)
                if (bundle != null) {
                    return bundle.getString(rule.extrasKey)
                }
            }
            return ""
        }

        private fun getBundle(param: XC_MethodHook.MethodHookParam): Bundle? {
            val intent = param.args[0] as Intent
            if (intent.extras != null) {
                return intent.extras
            } else if (param.args.size > 2 && param.args[2] != null) {
                return param.args[2] as Bundle
            } else {
                MyLog.e("获取 Bundle 异常")
                return null
            }
        }

        private fun openUrl(param: XC_MethodHook.MethodHookParam, uri: Uri) {
            //发送干净的Intent
            val exIntent = Intent()
            exIntent.action = Intent.ACTION_VIEW
            exIntent.data = uri
            exIntent.putExtra("exlink", true)
            exIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            (param.thisObject as Activity).startActivity(exIntent)
            param.result = null
        }

        /**
         * 匹配模式 <br></br>
         * 判断哪个值包含了Url，并发送到Exlink
         * @param param 传入的参数
         */
        private fun sendToExLink(param: XC_MethodHook.MethodHookParam) {
            val intent = param.args[0] as Intent
            MyLog.log("Intent: " + intent.toString())

            val data = intent.data
            if (data != null) {
                if (StreamUtil.isContain(data.toString())) {
                    openExlink(param, EX_DAT)
                    return
                }
            }

            var extras = intent.extras
            if (extras != null && !StreamUtil.isContain(extras!!.toString())) {
                MyLog.log("Extras不对，换一个")
                if (param.args.size > 2 && param.args[2] != null) {
                    extras = param.args[2] as Bundle
                    if (!StreamUtil.isContain(extras!!.toString())) {
                        MyLog.log("还是不对，不处理了...")
                        return
                    }
                } else {
                    MyLog.log("没Bundle...")
                    return
                }
            }
            MyLog.log(" - With extras: " + extras!!)

            val keySet = extras!!.keySet()
            for (key in keySet) {
                val o = extras!!.get(key) ?: continue
                val value = o.toString()

                if (StreamUtil.isContain(value)) {
                    openExlink(param, key)
                }
            }

        }

        private fun openExlink(param: XC_MethodHook.MethodHookParam, key: String) {
            MyLog.log("成功匹配！")
            val uri = Uri.parse("exlink://test")
            val activityName = param.thisObject.javaClass.name

            val sendToExLinkIntent = Intent()
            sendToExLinkIntent.action = Intent.ACTION_VIEW
            sendToExLinkIntent.data = uri
            sendToExLinkIntent.putExtra("activityName", activityName)
            sendToExLinkIntent.putExtra("extrasKey", key)
            sendToExLinkIntent.putExtra("position", index)
            (param.thisObject as Activity).startActivity(sendToExLinkIntent)
            param.result = null
        }

        /**
         * 兼容微信 <br></br>
         * 对微信进行特殊处理
         * @param param 传入的参数
         * @param uri 需要被打开的链接
         */
        private fun compatibleWeChat(param: XC_MethodHook.MethodHookParam?, uri: Uri) {
            var isOpenWithOut1 = false
            var isOpenWithOut2 = false
            var isOpenWithOut3 = false

            val intent = param!!.args[0] as Intent
            val judge1 = intent.getStringExtra("preChatName")
            val judge2 = intent.getStringExtra("prePublishId")
            val judge3 = intent.getStringExtra("preUsername")
            MyLog.log("judge1:" + judge1 + "judge2:" + judge2 + "judge3:" + judge3)

            if (judge1 == null && judge2 == null && judge3 == null) {
                isOpenWithOut1 = true
            }

            val judge4 = intent.getStringExtra("version_name")
            val judge5 = intent.getStringExtra("KAppId")
            val judge6 = intent.getStringExtra("srcDisplayname")
            MyLog.log("judge4:" + judge4 + "judge5:" + judge5 + "judge6:" + judge6)

            if (judge4 != null && judge5 != null && judge6 == null) {
                isOpenWithOut2 = true
            }

            val judge7 = intent.getStringExtra("srcUsername")
            val judge8 = intent.getStringExtra("message_index")
            val judge9 = intent.getStringExtra("message_id")
            MyLog.log("judge7:" + judge7 + "judge8:" + judge8 + "judge9:" + judge9)

            if (judge7 == null && judge8 == null && judge9 == null) {
                isOpenWithOut3 = true
            }

            if (isOpenWithOut1 || isOpenWithOut2 || isOpenWithOut3) {
                MyLog.log("判断可以用外置浏览器打开")
                openUrl(param, uri)
            } else {
                MyLog.log("判断需要用内置浏览器打开")
            }
        }

    }


    companion object {
        private var appList: List<App> by Delegates.notNull()
        private val EX_DAT = "ExDat"
    }
}


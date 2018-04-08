package com.xloger.exlink.app

import com.xloger.exlink.app.util.MyLog
import com.xloger.exlink.app.util.Tool
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * Created by xloger on 2018/4/8.
 */
class HookSelf : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName.equals("com.xloger.exlink.app")) {
            MyLog.log("开始hook 测试方法")
            try {
//            val clazz = XposedHelpers.findClass("com.xloger.exlink.app.util.KotlinTool", lpparam.classLoader)
//            val value = clazz.getDeclaredField("test")
//            MyLog.e("value:" + value)


                //"com.xloger.exlink.app.util.Tool"
                XposedHelpers.findAndHookMethod(Tool::class.java, "isHook", XC_MethodReplacement.returnConstant(true))
                MyLog.log("isHook:" + Tool.isHook())
            } catch (ex: XposedHelpers.ClassNotFoundError) {
                MyLog.e("没找到你妹啊！")
                MyLog.e(ex.message)
            }
        }
    }

}
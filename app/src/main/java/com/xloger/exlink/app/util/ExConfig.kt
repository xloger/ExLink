package com.xloger.exlink.app.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.xloger.exlink.app.Constant
import de.robv.android.xposed.XSharedPreferences
import java.io.File

/**
 * Created on 2019/1/28 3:09.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 存储配置信息的类
 */
object ExConfig {
    lateinit var sp: SharedPreferences

    val fileName = "exconfig"

    fun init(context: Context) {
        sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
//        val prefsFile = File(context.getFilesDir().toString() + "/../shared_prefs/" + fileName + ".xml")
//        prefsFile.setReadable(true, false)
//
//        context.filesDir.setReadable(true, false)
//        val path = "/data/data/${Constant.PACKAGE_NAME}"
//        File(path).setReadable(true, false)
//
//        val path2 = "/data/data/${Constant.PACKAGE_NAME}/shared_prefs"
//        File(path2).setReadable(true, false)
    }

    fun saveFromApp(key: String, value: String) {
        if (!ExConfig::sp.isInitialized) {
            return
        }
        val edit = sp.edit()
        edit.putString(key, value)
        edit.apply()
        MyLog.isChecked = false
    }

    fun loadFromApp(key: String): String {
        return sp.getString(key, "")
    }

    @Deprecated("高版本不支持，不推荐使用")
    fun loadFromXposed(key: String): String {
        try {

            val xsp = XSharedPreferences(Constant.PACKAGE_NAME, fileName)
//            MyLog.e("xsp路径：${xsp.file.toString()}")
            xsp.makeWorldReadable()
            xsp.reload()
            val string = xsp.getString(key, "")
//            MyLog.e("string:${string}")
            return string
        } catch (ex: NoClassDefFoundError) {
            MyLog.e("找不到 XSharedPreferences")
            return ""
        }

    }
}
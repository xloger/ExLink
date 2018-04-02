package com.xloger.exlink.app.util

import android.annotation.SuppressLint
import android.os.Environment
import com.google.gson.Gson
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.entity.AppList
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import android.os.Build


/**
 * Created on 2017/11/14 20:12.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
class JSONFile {
    val file: File
        get() {
//            val SDPath = Environment.getExternalStorageDirectory().path
//            val SDPath = System.getenv("EXTERNAL_STORAGE")
            val OldSDPath = "/data/data/com.xloger.exlink.app/"
            val SDPath = if (Build.VERSION.SDK_INT >= 24)
                "/data/user_de/0/com.xloger.exlink.app/"
            else
                OldSDPath

            var jsonFile = File(SDPath)
            try {
                jsonFile = File(SDPath, "exlink.json")
            } catch (ex: FileNotFoundException) {
                MyLog.e("文件又没有找到")
                val appList = AppUtil().initAppData()
                saveJson(appList)
                jsonFile = File(SDPath, "exlink.json")
            }
            return jsonFile
        }

    //兼容 Java 代码
    fun getJson() : List<App> = getJson("default")

    fun getJson(from: String = "default"): List<App> {
        try {
            return if (file.exists() && file.readText().isNotEmpty()) {
                Gson().fromJson(file.readText(), AppList::class.java).list
            } else {
                val appList = AppUtil().initAppData()
                saveJson(appList)
                appList
            }
        } catch (ex: FileNotFoundException) {
            var appList = AppUtil().initAppData()
            if (from == "xposed") {
                MyLog.e("${file.path}没有找到，如果是开机阶段可无视该报错。")
            } else {
                MyLog.e("${file.path}没找到，将初始化")
                val isCrate = saveJson(appList)
                if (!isCrate) {
                    appList = emptyList()
                }
            }

            return appList
        }

    }

    @SuppressLint("SetWorldReadable")
    fun saveJson(list: List<App>): Boolean {
        if (!file.exists()) {
            val isSuccess = try {
                file.createNewFile()
            } catch (ex: IOException) {
                MyLog.e(ex.message)
                false
            }
            if (!isSuccess) {
                MyLog.e("创建 json 文件异常${file.path}")
                return false
            } else {
                MyLog.e("成功创建 json 文件")
                val b = file.setReadable(true, false)
                if (b) {
                    MyLog.log("修改权限为公开")
                }
                file.writeText(Gson().toJson(AppList(list)))
            }
        }
        return true
    }
}
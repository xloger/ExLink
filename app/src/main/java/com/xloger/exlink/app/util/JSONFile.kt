package com.xloger.exlink.app.util

import android.os.Environment
import com.google.gson.Gson
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.entity.AppList
import java.io.File

/**
 * Created on 2017/11/14 20:12.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
class JSONFile {
    val file: File
        get() = File(Environment.getExternalStorageDirectory().path, "exlink.json")

    fun getJson(): List<App> {
        if (file.exists() && file.readText().isNotEmpty()) {
            return Gson().fromJson(file.readText(), AppList::class.java).list
        } else {
            val appList = AppUtil().initAppData()
            saveJson(appList)
            return appList
        }
    }

    fun saveJson(list: List<App>) {
        if (!file.exists()) {
            val isSuccess = file.createNewFile()
            if (!isSuccess) {
                MyLog.e("创建 json 文件异常")
                return
            }
        }
        file.writeText(Gson().toJson(AppList(list)))
    }
}
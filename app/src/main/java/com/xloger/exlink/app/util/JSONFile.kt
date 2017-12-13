package com.xloger.exlink.app.util

import android.os.Environment
import com.google.gson.Gson
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.entity.AppList
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created on 2017/11/14 20:12.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
class JSONFile {
    val file: File
        get() {
//            val SDPath = Environment.getExternalStorageDirectory().path
            val SDPath = System.getenv("EXTERNAL_STORAGE")
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

    fun getJson(): List<App> {
        try {
            if (file.exists() && file.readText().isNotEmpty()) {
                return Gson().fromJson(file.readText(), AppList::class.java).list
            } else {
                val appList = AppUtil().initAppData()
                saveJson(appList)
                return appList
            }
        }catch (ex: FileNotFoundException){
            MyLog.e("文件没找到，将初始化")
            val appList = AppUtil().initAppData()
            saveJson(appList)
            return appList
        }

    }

    fun saveJson(list: List<App>) {
        if (!file.exists()) {
            var isSuccess = false
            try {
                isSuccess = file.createNewFile()
            }catch (ex: IOException){
                isSuccess = false
            }
            if (!isSuccess) {
                MyLog.e("创建 json 文件异常")
                return
            } else {
                MyLog.e("成功创建 json 文件")
                file.writeText(Gson().toJson(AppList(list)))
            }
        }
    }
}
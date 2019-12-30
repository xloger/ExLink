package com.xloger.exlink.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.entity.AppList
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


/**
 * Created on 2017/11/14 20:12.
 * Editor:xloger
 * Email:phoenix@xloger.com
 * TODO 做成缓存
 */
object JSONFile {

    fun getJson(): MutableList<App> = getJson("")

    fun getJson(from: String = "default"): MutableList<App> {
        val string = ExConfig.loadFromApp("exlink.json")
        if (!string.isBlank()) {
            val list = Gson().fromJson<AppList>(string, AppList::class.java).list
            return list
        }

        val appList = AppUtil().initAppData()

        MyLog.e("没找到规则文件，返回默认值。")
        saveJson(appList)

        return appList

    }

    fun readJsonFromXposed(context: Context): List<App> {
        MyLog.e("context:" + context)
        context?.let {
            val cursor = it.contentResolver?.query(Uri.parse("content://com.xloger.exlink.app.rule/rule"), null, null, null, null)
            cursor?.let {
                cursor.moveToNext()
                val json = cursor.getString(0)
                if (!json.isBlank()) {
//                    val list = Gson().fromJson<AppList>(json, AppList::class.java).list
                    val list = Gson().fromJson<AppList>(json, AppList::class.java).list

                    return list
                }
            }

        }
        return emptyList()

    }

    fun saveJson(list: MutableList<App>): Boolean {
        ExConfig.saveFromApp("exlink.json", Gson().toJson(AppList(list)))
        return true

    }

}
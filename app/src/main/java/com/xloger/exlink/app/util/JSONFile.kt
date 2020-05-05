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
 * 规则的操作类，在此进行获取与存储操作
 */
object JSONFile {

    /**
     * 获得规则数据
     */
    @JvmStatic
    fun getJson(): MutableList<App> {
        val string = FileHelper.readFile()
        if (!string.isBlank()) {
            val list = Gson().fromJson<AppList>(string, AppList::class.java).list
            return list
        } else {
            throw GetRuleError()
        }



    }

    /**
     * 在 hook 方法中得到规则数据
     */
    fun getJsonFromXposed(): MutableList<App> {
        return getJson()
    }

    fun getDefaultJson(): List<App> {
        val appList = AppUtil().initAppData()
        saveJson(appList)
        return appList
    }

    /**
     * 保存数据
     * @return 是否成功存储
     */
    fun saveJson(list: MutableList<App>): Boolean {
        return FileHelper.saveFile(FileHelper.getJsonPath(), Gson().toJson(AppList(list)))
    }

}

class GetRuleError() : Exception() {

}
package com.xloger.exlink.app.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.xloger.exlink.app.Constant
import de.robv.android.xposed.XSharedPreferences

/**
 * Created on 2019/1/28 3:09.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object ExConfig {
    lateinit var sp: SharedPreferences

    val fileName = "exconfig"

    fun init(context: Context) {
        sp = context.getSharedPreferences(fileName, Activity.MODE_WORLD_READABLE)
    }

    fun saveFromApp(key: String, value: String) {
        val edit = sp.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun loadFromApp(key: String) : String {
        return sp.getString(key, "")
    }

    fun loadFromXposed(key: String) : String {
        val xsp = XSharedPreferences(Constant.PACKAGE_NAME, fileName)
        xsp.makeWorldReadable()
        return xsp.getString(key, "")
    }
}
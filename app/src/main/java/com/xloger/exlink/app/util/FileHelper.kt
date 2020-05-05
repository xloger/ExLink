package com.xloger.exlink.app.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.lang.Exception
import java.nio.charset.Charset

/**
 * Created on 2020/1/29 20:13.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object FileHelper {

    fun getJsonPath() : String {
        val sdPath = "/sdcard"
        val dir = File(sdPath, "ExLink")
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir.path + File.separator + "exlink.json"
    }

    fun readFile(path: String = getJsonPath()): String {
        val file = File(path)
        if (!file.exists()) {
            return ""
        }
        try {
            return file.readText(Charsets.UTF_8)
        } catch (ex: Exception) {
            return ""
        }
    }

    fun saveFile(path: String = getJsonPath(), content: String) : Boolean {
        val file = File(path)
        try {
            file.writeText(content, Charsets.UTF_8)
            return true
        } catch (ex: Exception) {
            MyLog.e(ex.message)
            return false
        }
    }
}
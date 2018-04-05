package com.xloger.exlink.app.util

import com.xloger.exlink.app.entity.App

/**
 * Created on 2017/12/17 18:07.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
class KotlinTool {
    var test = "内容测试"

    fun testHook(): Boolean {
        return false
    }

    fun listAppToSimpleString(list: List<App>) = list.toSimpleString()
}
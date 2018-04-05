package com.xloger.exlink.app.util

import com.xloger.exlink.app.entity.App

/**
 * Created by xloger on 2018/4/5.
 */
fun List<App>.toSimpleString(): String {
    val ret = StringBuffer()
    this.forEach {
        ret.append("${it.appName},\n")
    }
    return ret.toString()
}
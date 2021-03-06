package com.xloger.exlink.app.entity

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
data class App(var appName: String,
               var packageName: String,
               var rules: Set<Rule> = emptySet(),
               var whiteUrl: Set<String> = emptySet(),
               var isUse: Boolean = true,
               var isUserBuild: Boolean = true,
               var isTest: Boolean = false)

data class AppList(val list: MutableList<App>)
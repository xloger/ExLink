package com.xloger.exlink.app.entity

import java.io.Serializable

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
data class App(var appName: String,
               var packageName: String,
               var rules: Set<Rule>,
               var whiteUrl: Set<String>,
               var isUse: Boolean = true,
               var isUserBuild: Boolean = true,
               var isTest: Boolean = false)

data class AppList(val list: List<App>)
package com.xloger.exlink.app.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * Created on 2019/8/25 18:05.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class RuleDatabaseOpenHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "rule", null, 1) {

    init {
        instance = this
    }

    companion object {
        private var instance: RuleDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: RuleDatabaseOpenHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable("Rule", true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE,
                "rule" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {

    }
}
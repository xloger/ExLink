package com.xloger.exlink.app

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.google.gson.Gson
import com.xloger.exlink.app.entity.AppList
import com.xloger.exlink.app.util.AppUtil
import com.xloger.exlink.app.util.ExConfig
import com.xloger.exlink.app.util.JSONFile
import com.xloger.exlink.app.util.MyLog

class RuleContentProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor = MatrixCursor(arrayOf("rule"), 1)
        val json = Gson().toJson(AppList(AppUtil().initAppData()))
        cursor.addRow(arrayOf(json))
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return 0
    }
}

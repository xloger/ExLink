package com.xloger.exlink.app.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import com.xloger.exlink.app.R
import com.xloger.exlink.app.util.AppUtil

/**
 * Created by xloger on 2018/4/6.
 */
class ExportJsonDialog(context: Context) : AlertDialog(context) {

    fun showDialog() {
        val builder = Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_export_json, null)
        val editText = view.findViewById<EditText>(R.id.export_edit)

        editText.setText(AppUtil().exportJson())


        builder.setView(view)
        builder.create().show()
    }
}
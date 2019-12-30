package com.xloger.exlink.app.view

import android.content.ClipboardManager
import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import com.xloger.exlink.app.R
import com.xloger.exlink.app.activity.MainActivity
import com.xloger.exlink.app.adapter.AppAdapter
import com.xloger.exlink.app.adapter.ExportAdapter
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.util.AppUtil
import com.xloger.exlink.app.util.JSONFile

/**
 * Created by xloger on 2018/4/6.
 */
class ExportJsonDialog(context: Context) : AlertDialog(context) {
    val chooseSet = mutableSetOf<Int>()
    val chooseAppList = mutableListOf<App>()

    fun showDialog() {
        val builder = Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_export_json, null)
        val editText = view.findViewById<EditText>(R.id.export_edit)
        val listview = view.findViewById<ListView>(R.id.export_list)

        val appList = JSONFile.getJson()
        val adapter = ExportAdapter(context, appList) { position, isCheck ->
            if (isCheck) {
                chooseSet.add(position)
            } else {
                chooseSet.remove(position)
            }

            chooseAppList.clear()
            chooseSet.forEach {
                chooseAppList.add(appList[it])
            }
            editText.setText(AppUtil().exportJson(chooseAppList))

        }
        listview.adapter = adapter

        builder.setPositiveButton("复制到剪切板"){ dialogInterface, i ->
            val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmb.text = AppUtil().exportJson(chooseAppList)
        }


        builder.setView(view)
        builder.create().show()
    }
}
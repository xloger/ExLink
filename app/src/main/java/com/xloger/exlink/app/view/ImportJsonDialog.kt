package com.xloger.exlink.app.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.xloger.exlink.app.R
import com.xloger.exlink.app.activity.MainActivity
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.util.AppUtil
import com.xloger.exlink.app.util.MyLog

/**
 * Created by xloger on 2018/4/6.
 */
class ImportJsonDialog(val activity: MainActivity, val appList: MutableList<App>) : AlertDialog(activity) {

    fun showDialog() {
        val builder = Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_export_json, null)
        val editText = view.findViewById<EditText>(R.id.export_edit)

        builder.setPositiveButton("导入") { dialog, which ->
            val isSuccess = AppUtil().addJson(appList, editText.text.toString())
            if (isSuccess) {
                activity.updateList()
            } else {
                Toast.makeText(activity, "格式不正确", Toast.LENGTH_SHORT).show()
            }

        }


        builder.setView(view)
        builder.create().show()
    }
}
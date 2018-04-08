package com.xloger.exlink.app.view

import android.app.Activity
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
class ImportJsonDialog(val activity: Activity, val appList: MutableList<App>) : AlertDialog(activity) {

    fun showDialog() {
        val builder = Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_import_json, null)
        val editText = view.findViewById<EditText>(R.id.import_edit)

        builder.setPositiveButton("导入") { dialog, which ->
            val isSuccess = AppUtil().addJson(appList, editText.text.toString())
            if (isSuccess) {
                if (activity is MainActivity) {
                    activity.updateList()
                }
            } else {
                Toast.makeText(activity, "格式不正确", Toast.LENGTH_SHORT).show()
            }

        }


        builder.setView(view)
        builder.create().show()
    }
}
package com.xloger.exlink.app.view

import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.xloger.exlink.app.R
import com.xloger.exlink.app.adapter.ChooseAppAdapter
import com.xloger.exlink.app.entity.AndroidApp
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.util.AndroidAppUtil

/**
 * Created on 16/6/1 下午3:57.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class StepOneDialog(context: Context) : AlertDialog(context), View.OnClickListener {
    private var callBack: StepOneCallBack? = null
    private var appEditText: EditText? = null
    private var packageEditText: EditText? = null
    private var chooseBtn: Button? = null

    //                    if ("".equals(newRuleAppName)) {
    //                        testApp.setAppName(getString(R.string.demo));
    //                    }else {
    //                        testApp.setAppName(newRuleAppName);
    //                    }
    //                    testApp.setPackageName(newRulePackageName);
    val builder: AlertDialog.Builder
        get() {
            val builder = AlertDialog.Builder(context)
            val oneStepView = LayoutInflater.from(context).inflate(R.layout.step_one, null)
            chooseBtn = oneStepView.findViewById<View>(R.id.step_one_choose) as Button
            appEditText = oneStepView.findViewById<View>(R.id.new_rule_app_name) as EditText
            packageEditText = oneStepView.findViewById<View>(R.id.new_rule_package_name) as EditText
            chooseBtn!!.setOnClickListener(this)
            builder.setTitle(getString(R.string.step_one))
            builder.setView(oneStepView)
            builder.setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener { dialog, which ->
                val newRuleAppName = appEditText!!.text.toString()
                val newRulePackageName = packageEditText!!.text.toString()
                if ("" != newRulePackageName && newRulePackageName.length != 0) {
                    if (newRulePackageName == "com.coolapk.market") {
                        Toast.makeText(context, "酷安可直接在其设置里修改，无须添加。", Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    val testApp = App(newRuleAppName, newRulePackageName)
                    testApp.isUse = true
                    testApp.isUserBuild = true
                    testApp.isTest = true

                    if (callBack != null) {
                        callBack!!.onPositiveClick(testApp)
                    }
                    Toast.makeText(context, getString(R.string.step_one_text4), Toast.LENGTH_SHORT).show()
                }
            })
            builder.setNegativeButton(getString(R.string.no)) { dialog, which -> dialog.dismiss() }
            return builder
        }

    private fun getString(id: Int): String {
        return context.getString(id)
    }

    fun setPositiveListener(callBack: StepOneCallBack) {
        this.callBack = callBack
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.step_one_choose -> openChooseDialog()
        }
    }

    private fun openChooseDialog() {
        ChooseAppTask().execute()
    }

    internal inner class ChooseAppTask : AsyncTask<Void, Void, List<AndroidApp>>() {

        private var loading: Loading? = null

        override fun onPreExecute() {
            loading = Loading(context)
            loading!!.show()
        }

        override fun onPostExecute(appList: List<AndroidApp>) {
            loading!!.dismiss()
            val builder = AlertDialog.Builder(context)
            val adapter = ChooseAppAdapter(context, appList)
            builder.setAdapter(adapter) { dialog, which ->
                appEditText!!.setText(appList[which].name)
                packageEditText!!.setText(appList[which].packageName)
            }
            builder.create().show()
        }

        override fun doInBackground(vararg params: Void): List<AndroidApp> {
            val appList = AndroidAppUtil.getUserAppInfo(context)
            return AndroidAppUtil.getUserAppInfo(context)
        }
    }


    interface StepOneCallBack {
        fun onPositiveClick(app: App)
    }

}



package com.xloger.exlink.app.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewConfiguration
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.xloger.exlink.app.R
import com.xloger.exlink.app.adapter.AppAdapter
import com.xloger.exlink.app.entity.App
import com.xloger.exlink.app.util.*
import com.xloger.exlink.app.util.AppUtil.Companion.INSTANCE
import com.xloger.exlink.app.util.JSONFile.getJson
import com.xloger.exlink.app.util.JSONFile.saveJson
import com.xloger.exlink.app.view.AddWhiteDialog
import com.xloger.exlink.app.view.AddWhiteDialog.AddWhiteCallBack
import com.xloger.exlink.app.view.ExportJsonDialog
import com.xloger.exlink.app.view.ImportJsonDialog
import com.xloger.exlink.app.view.StepOneDialog
import com.xloger.exlink.app.view.StepOneDialog.StepOneCallBack
import com.xloger.xlib.tool.XPermission.Write_SD
import com.xloger.xlib.tool.XPermission.XPermissionCallback
import com.xloger.xlib.tool.XPermission.onRequestPermissionsResult
import com.xloger.xlib.tool.XPermission.requestPermission
import com.xloger.xlib.tool.Xlog

class MainActivity : BaseActivity(), View.OnClickListener {
    private var appList: MutableList<App>? = null
    private var listView: ListView? = null
    private var appAdapter: AppAdapter? = null
    private var addApp: FloatingActionButton? = null
    private val isShowingDebug = false
    private var context: Context? = null
    private var readme: TextView? = null
    private var jsonFile: JSONFile? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this@MainActivity
        jsonFile = JSONFile
        initView()
        requestPermission(this, object : XPermissionCallback {
            override fun onSuccess() {
                onGetPermission()
            }

            override fun onRefuse(list: List<String>) {
                Toast.makeText(context, "该 App 需要将规则存入SD卡供hook方法读取", Toast.LENGTH_SHORT).show()
            }
        }, Write_SD)

//        onGetPermission();
//
//        Cursor cursor = getContentResolver().query(Uri.parse("content://com.xloger.exlink.app.rule/*"), null, null, null, null);
//        cursor.moveToNext();
//        String json = cursor.getString(0);
//        MyLog.log("得到了：" + json);
//        Log.d("xloger", "得到：" + json);

//        setOverflowShowingAlways();
    }

    private fun onGetPermission() {
        try {
            appList = getJson()
        } catch (error: GetRuleError) {
            saveJson(INSTANCE.initAppData())
            appList = getJson()
        }
        openStepTwo()
        debugMode()
        appAdapter = AppAdapter(this, appList, itemCallBack)
        listView!!.adapter = appAdapter
        ViewTool.setListViewHeightBasedOnChildren(listView)
        addApp!!.setOnClickListener(this)
        readme!!.setOnClickListener(this)
        readme!!.paint.isAntiAlias = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        appList!!.clear()
        appList!!.addAll(jsonFile!!.getJson())
        if (appAdapter != null) {
            appAdapter!!.notifyDataSetChanged()
        }
        debugMode()
    }

    override fun onPause() {
        super.onPause()
        updateList()
    }

    fun updateList() {
        jsonFile!!.saveJson(appList!!)
        //        MyLog.log("更新 List:" + new KotlinTool().listAppToSimpleString(appList));
        appAdapter!!.notifyDataSetChanged()
        ViewTool.setListViewHeightBasedOnChildren(listView)
    }

    private fun initView() {
        listView = findViewById(R.id.app_list)
        addApp = findViewById(R.id.add_app)
        readme = findViewById(R.id.main_read_me)
        MyLog.log("View isHook:" + Tool.isHook())
        if (!Tool.isHook()) {
            findViewById<View>(R.id.hook_text).visibility = View.VISIBLE
        }
    }

    private fun openStepTwo() {
        for (i in appList!!.indices) {
            if (appList!![i].isUse && appList!![i].isTest) {
                val intent = Intent(context, StepTwoActivity::class.java)
                startActivity(intent)
                break
            }
        }
    }

    private fun debugMode() {
        val sp = getSharedPreferences("config", 0)
        val isDebugMode = sp.getBoolean("isDebugMode", false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.add_app -> {
                val dialog = StepOneDialog(context!!)
                dialog.setPositiveListener(object : StepOneCallBack {
                    override fun onPositiveClick(app: App) {
                        appList!!.add(app)
                        updateList()
                        ViewTool.setListViewHeightBasedOnChildren(listView)
                    }
                })
                val builder = dialog.builder
                builder.create().show()
            }
            R.id.main_read_me -> {
                val intent = Intent(context, ReadMeActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //    private String[] longClickList=new String[]{getString(R.string.item_click_text1),getString(R.string.item_click_text2),getString(R.string.item_click_text3)};
    private val itemCallBack: ItemCallBack = object : ItemCallBack {
        override fun onClick(position: Int, view: View?) {
            if (view is CheckBox) {
                appList!![position].isUse = view.isChecked
                updateList()
                Toast.makeText(context, getString(R.string.item_check_text), Toast.LENGTH_SHORT).show()
            } else {
                onLongClick(position, view)
            }
        }

        override fun onLongClick(position: Int, view: View?) {
            var longClickList = arrayOf(getString(R.string.item_click_text1), getString(R.string.item_click_text2), getString(R.string.item_click_text3))
            if (appList!![position].isTest) {
                longClickList = arrayOf(getString(R.string.item_click_text1), getString(R.string.item_click_text2), getString(R.string.item_click_text3), getString(R.string.reset_rule))
            }
            val builder = AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.item_click_title))
            builder.setItems(longClickList, DialogInterface.OnClickListener { dialog, which ->
                if (which == 0) {
                    appList!![position].isTest = true
                    updateList()
                    //                        openStepTwo();
                    val intent = Intent(context, EditRuleActivity::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                } else if (which == 1) {
                    val addWhiteDialog = AddWhiteDialog(context, appList!![position])
                    addWhiteDialog.setCallBack(object : AddWhiteCallBack {
                        override fun onPositiveClick() {}
                        override fun saveWhiteList() {
                            updateList()
                        }
                    })
                    val builder = addWhiteDialog.builder
                    builder.create().show()
                } else if (which == 2) {
                    if (!appList!![position].isUserBuild) {
                        Toast.makeText(context, getString(R.string.del_system_rule_toask), Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }
                    appList!!.removeAt(position)
                    updateList()
                    ViewTool.setListViewHeightBasedOnChildren(listView)
                    appAdapter!!.notifyDataSetChanged()
                } else if (which == 3) {
                    appList!![position].isTest = false
                    updateList()
                }
            })
            builder.create().show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent: Intent
        when (item.itemId) {
            R.id.action_instruction -> {
                intent = Intent(context, ReadMeActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                intent = Intent(context, SettingActivity::class.java)
                startActivity(intent)
            }
            R.id.action_about -> {
                intent = Intent(context, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.action_export_json -> ExportJsonDialog(context!!).showDialog()
            R.id.action_import_json -> ImportJsonDialog(this, appList!!).showDialog()
            R.id.action_import_old_json -> {
                val bytes = FileUtil.loadOld("/data/data/com.xloger.exlink.app/", "exlink.json")
                if (bytes != null && bytes.size != 0) {
                    val isSuccess = AppUtil().addJson(appList!!, String(bytes))
                    if (isSuccess) {
                        updateList()
                        Xlog.toast(context, "导入成功")
                    } else {
                        Xlog.toast(context, "导入失败")
                    }
                } else {
                    Xlog.toast(context, "没有旧的规则存在")
                }
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 通过反射，强制显示Overflow（即右上角的菜单按钮）
     */
    private fun setOverflowShowingAlways() {
        try {
            val config = ViewConfiguration.get(this)
            val menuKeyField = ViewConfiguration::class.java.getDeclaredField("sHasPermanentMenuKey")
            menuKeyField.isAccessible = true
            menuKeyField.setBoolean(config, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface ItemCallBack {
        fun onClick(position: Int, view: View?)
        fun onLongClick(position: Int, view: View?)
    }
}
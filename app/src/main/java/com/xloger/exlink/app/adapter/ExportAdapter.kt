package com.xloger.exlink.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

import com.xloger.exlink.app.R
import com.xloger.exlink.app.activity.MainActivity
import com.xloger.exlink.app.entity.App

/**
 * Created by xloger on 1月3日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
class ExportAdapter(private val context: Context, private val appList: List<App>?, private val itemCallBack: (position: Int, isCheck: Boolean) -> Unit) : BaseAdapter() {

    override fun getCount(): Int {
        return appList?.size ?: 0
    }

    override fun getItem(position: Int): Any {
        return appList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var ret: View = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.item_app, parent, false)

        var viewHolder: ViewHolder? = null
        if (ret.getTag(R.id.app_adapter_view_holder) == null) {
            viewHolder = ViewHolder()
            viewHolder.layout = ret.findViewById(R.id.app_layout)
            viewHolder.use = ret.findViewById(R.id.app_use)
            viewHolder.name = ret.findViewById(R.id.app_name)
            viewHolder.packageName = ret.findViewById(R.id.app_package_name)
            viewHolder.system = ret.findViewById(R.id.app_system)
            viewHolder.test = ret.findViewById(R.id.app_test)
            ret.setTag(R.id.app_adapter_view_holder, viewHolder)
        } else {
            viewHolder = ret.getTag(R.id.app_adapter_view_holder) as ViewHolder
        }

        viewHolder.name!!.text = appList!![position].appName
        viewHolder.packageName!!.visibility = View.GONE
        viewHolder.packageName!!.text = appList[position].packageName

        viewHolder.use!!.tag = position.toString() + ""
        viewHolder.use!!.setOnClickListener(View.OnClickListener {
            onClick(it, viewHolder.use!!.isChecked)
        })

        viewHolder.layout!!.tag = position.toString() + ""
        viewHolder.layout!!.setTag(R.id.app_adapter_position, position.toString() + "")
        viewHolder.layout!!.setOnClickListener(View.OnClickListener {
            viewHolder.use!!.isChecked = !viewHolder.use!!.isChecked
            onClick(it, viewHolder.use!!.isChecked)
        })


        return ret
    }


    fun onClick(v: View, isCheck: Boolean) {
        val positionString = v.tag as String
        val position = Integer.parseInt(positionString)
        itemCallBack(position, isCheck)
    }


    private class ViewHolder {
        var layout: LinearLayout? = null
        var use: CheckBox? = null
        var name: TextView? = null
        var packageName: TextView? = null
        var system: TextView? = null
        var test: TextView? = null
    }
}

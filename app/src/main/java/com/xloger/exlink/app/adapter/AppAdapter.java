package com.xloger.exlink.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.browse.MediaBrowser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.activity.MainActivity;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xloger on 1月3日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class AppAdapter extends BaseAdapter implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private List<App> appList;
    private MainActivity.ItemCallBack itemCallBack;

    public AppAdapter(Context context, List<App> appList,MainActivity.ItemCallBack itemCallback) {
        this.context = context;
        this.appList = appList;
        this.itemCallBack=itemCallback;
    }

    @Override
    public int getCount() {
        if (appList != null) {
            return appList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        if (convertView == null) {
            ret = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        }else {
            ret=convertView;
        }

        ViewHolder viewHolder= (ViewHolder) ret.getTag(R.id.app_adapter_view_holder);
        if (viewHolder == null) {
            viewHolder=new ViewHolder();
            viewHolder.layout= (LinearLayout) ret.findViewById(R.id.app_layout);
            viewHolder.use= (CheckBox) ret.findViewById(R.id.app_use);
            viewHolder.name= (TextView) ret.findViewById(R.id.app_name);
            viewHolder.packageName= (TextView) ret.findViewById(R.id.app_package_name);
            ret.setTag(R.id.app_adapter_view_holder,viewHolder);
        }

        viewHolder.use.setChecked(appList.get(position).isUse());
        viewHolder.name.setText(appList.get(position).getAppName());
        viewHolder.packageName.setText(appList.get(position).getPackageName());

        viewHolder.use.setTag(position+"");
        viewHolder.use.setOnClickListener(this);

        viewHolder.layout.setTag(position+"");
        viewHolder.layout.setTag(R.id.app_adapter_position,position+"");
        viewHolder.layout.setOnLongClickListener(this);

        return ret;
    }


    @Override
    public void onClick(View v) {
        String positionString=(String)v.getTag();
        int position=Integer.parseInt(positionString);
        itemCallBack.onClick(position,v);
    }


    @Override
    public boolean onLongClick(View v) {
        String positionString=(String)v.getTag(R.id.app_adapter_position);
        int position=Integer.parseInt(positionString);
        itemCallBack.onLongClick(position,v);
        return true;
    }


    private static class ViewHolder{
        public LinearLayout layout;
        public CheckBox use;
        public TextView name;
        public TextView packageName;
    }
}

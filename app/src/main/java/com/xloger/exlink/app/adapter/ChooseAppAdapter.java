package com.xloger.exlink.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xloger.exlink.app.R;
import com.xloger.exlink.app.entity.AndroidApp;

import java.util.List;

/**
 * Created on 16/6/1 下午7:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class ChooseAppAdapter extends ArrayAdapter<AndroidApp> {
    private Context context;
    private List<AndroidApp> appList;
    public ChooseAppAdapter(Context context,List<AndroidApp> appList) {
        super(context, R.layout.item_choose_app,appList);
        this.context=context;
        this.appList=appList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view=LayoutInflater.from(context).inflate(R.layout.item_choose_app,parent,false);
        }else {
            view=convertView;
        }

        AndroidApp app=getItem(position);

        TextView nameText = view.findViewById(R.id.choose_name);
        nameText.setText(app.getName());
        TextView packageText = view.findViewById(R.id.choose_package_name);
        packageText.setText(app.getPackageName());
        ImageView icon = view.findViewById(R.id.choose_icon);
        icon.setImageDrawable(app.getIcon());


        return view;
    }
}

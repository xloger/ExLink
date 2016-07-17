package com.xloger.exlink.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.xloger.exlink.app.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created on 16/6/2 上午11:19.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class WhiteRuleAdapter extends BaseAdapter {
    private Context context;
    private Set whiteSet;
    private final List whiteList;
    private WhiteRuleAdapterCallBack callBack;

    public WhiteRuleAdapter(Context context,Set whiteSet,WhiteRuleAdapterCallBack callBack){
        this.context=context;
        this.whiteSet=whiteSet;
        this.callBack=callBack;
        Iterator iterator;
        if (whiteSet != null) {
            iterator=whiteSet.iterator();
            whiteList = new ArrayList();
            while (iterator.hasNext()){
                whiteList.add(iterator.next());
            }
        }else {
            whiteList=null;
        }

    }

    @Override
    public int getCount() {
        return whiteList==null?0:whiteList.size();
    }

    @Override
    public Object getItem(int position) {
        return whiteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view= LayoutInflater.from(context).inflate(R.layout.item_white_rule,parent,false);
        }else {
            view=convertView;
        }
        TextView urlText= (TextView) view.findViewById(R.id.item_white_url);
        ImageView delImg= (ImageView) view.findViewById(R.id.item_white_del);
        urlText.setText(whiteList.get(position).toString());
        delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteSet.remove(whiteList.get(position));
                whiteList.remove(position);
                callBack.onDelClick();
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public interface WhiteRuleAdapterCallBack{
        void onDelClick();
    }
}

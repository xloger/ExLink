package com.xloger.exlink.app.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.WhiteRuleAdapter;
import com.xloger.exlink.app.entity.App;

import java.util.HashSet;
import java.util.Set;

/**
 * Created on 16/6/2 上午10:29.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class AddWhiteDialog extends AlertDialog {
    protected Context context;
    private App app;
    private EditText whiteNameEditText;
    private AddWhiteCallBack callBack;
    private WhiteRuleAdapter adapter;

    public AddWhiteDialog(Context context,App app) {
        super(context);
        this.context=context;
        this.app=app;
    }

    public Builder getBuilder(){
        Builder builder=new Builder(context);
        final View addWhiteView = LayoutInflater.from(context).inflate(R.layout.add_white, null);
        whiteNameEditText = (EditText) addWhiteView.findViewById(R.id.add_white_name);
        ListView listView= (ListView) addWhiteView.findViewById(R.id.add_white_list);

        adapter = new WhiteRuleAdapter(context, app.getWhiteUrl(), new WhiteRuleAdapter.WhiteRuleAdapterCallBack() {
            @Override
            public void onDelClick() {
                if (callBack != null) {
                    callBack.saveWhiteList();
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }
            }
        });
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        builder.setTitle(getString(R.string.add_white_title)).setView(addWhiteView);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String whiteName= whiteNameEditText.getText().toString();
                if (!"".equals(whiteName)&&whiteName.length()!=0) {
                    Set<String> whiteUrl = app.getWhiteUrl();
                    if (whiteUrl == null) {
                        whiteUrl=new HashSet<String>();
                        app.setWhiteUrl(whiteUrl);
                    }
                    if (!"".equals(whiteName)) {
                        whiteUrl.add(whiteName);
                        if (callBack != null) {
                            callBack.saveWhiteList();
                            Toast.makeText(context,getString(R.string.add_white_succeed),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }

    private String getString(int id){
        return context.getString(id);
    }

    public void setCallBack(AddWhiteCallBack callBack){
        this.callBack=callBack;
    }

    public interface AddWhiteCallBack{
        void onPositiveClick();
        void saveWhiteList();
    }
}

package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.xloger.exlink.app.R;

/**
 * Created by xloger on 1月6日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class StepTwoActivity extends Activity implements View.OnClickListener {

    private TextView urlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_two);
        urlTextView = (TextView) findViewById(R.id.step_two_url);
        urlTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(urlTextView.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
//        cm.getText();//获取粘贴信息
        Toast.makeText(this,"复制成功",Toast.LENGTH_SHORT).show();
    }
}

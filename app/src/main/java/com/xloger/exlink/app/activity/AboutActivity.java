package com.xloger.exlink.app.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xloger.exlink.app.BuildConfig;
import com.xloger.exlink.app.R;

/**
 * Created on 16/6/1 下午3:39.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //自动匹配版本号
        TextView versions= findViewById(R.id.about_version);
        String versionName = BuildConfig.VERSION_NAME;
        versions.setText("版本号："+versionName);

        findViewById(R.id.about_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cmb = (ClipboardManager) AboutActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("429861674"); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
                Toast.makeText(AboutActivity.this,getString(R.string.copy_succeed),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

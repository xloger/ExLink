package com.xloger.exlink.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

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
        TextView versions= (TextView) findViewById(R.id.about_version);
        String versionName = BuildConfig.VERSION_NAME;
        versions.setText("版本号："+versionName);
    }
}

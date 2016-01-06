package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;

import java.util.List;

/**
 * Created by xloger on 1月6日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class StepThreeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_three);
        Intent intent = getIntent();
        String activityName = intent.getStringExtra("activityName");
        String extrasKey=intent.getStringExtra("extrasKey");
        int position = intent.getIntExtra("position", -1);
        List<App> appList = FileUtil.getAppList();
        if (appList != null) {
            App testApp = appList.get(position);
            testApp.setActivityName(activityName);
            testApp.setExtrasKey(extrasKey);
            testApp.setIsTest(false);
            FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
        }
    }
}

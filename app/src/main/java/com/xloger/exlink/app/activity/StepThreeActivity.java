package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.entity.Rule;
import com.xloger.exlink.app.util.AppUtil;
import com.xloger.exlink.app.util.FileUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by xloger on 1月6日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class StepThreeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_three);
        Intent intent = getIntent();
        String activityName = intent.getStringExtra("activityName");
        String extrasKey=intent.getStringExtra("extrasKey");
        int position = intent.getIntExtra("position", -1);
        List<App> appList = AppUtil.getAppList();
        if (appList != null) {
            App testApp = appList.get(position);

            Set<Rule> rules = testApp.getRules();
            if (rules == null) {
                rules=new HashSet<Rule>();
                testApp.setRules(rules);
            }
            rules.add(new Rule(activityName,extrasKey));
            testApp.setIsTest(false);
            AppUtil.save(appList);
        }


        //修改自定义Url
        FileUtil.getInstance().save(Constant.DIFFERENT_URL_FILE_NAME,"0".getBytes());

    }
}

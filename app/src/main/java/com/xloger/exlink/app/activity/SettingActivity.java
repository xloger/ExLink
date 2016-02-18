package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.util.FileUtil;

public class SettingActivity extends Activity implements View.OnClickListener {

    private CheckBox debugMode;
    private CheckBox hiddenIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        hiddenIcon = (CheckBox) findViewById(R.id.setting_hidden_icon);
        debugMode = (CheckBox) findViewById(R.id.setting_debug);

        hiddenIcon.setOnClickListener(this);
        debugMode.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("config", 0);
        boolean isDebugMode = sp.getBoolean("isDebugMode", false);
        debugMode.setChecked(isDebugMode);
        boolean isHiddenIcon = sp.getBoolean("isHiddenIcon", false);
        hiddenIcon.setChecked(isHiddenIcon);


    }


    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("config", 0);
        switch (v.getId()){
            case R.id.setting_debug:
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isDebugMode",debugMode.isChecked());
                edit.apply();

                FileUtil fileUtil = FileUtil.getInstance();
                fileUtil.save(Constant.IS_DEBUG_FILE_NAME, (debugMode.isChecked() + "").getBytes());
                fileUtil.setReadable(Constant.IS_DEBUG_FILE_NAME);
                break;
            case R.id.setting_hidden_icon:
                SharedPreferences.Editor edit2 = sp.edit();
                edit2.putBoolean("isHiddenIcon",hiddenIcon.isChecked());
                edit2.apply();
                ComponentName componentName=new ComponentName(this,XposedIntoActivity.class);
                if (hiddenIcon.isChecked()){
                    PackageManager packageManager = getPackageManager();
                    packageManager.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
                }else {
                    PackageManager p = getPackageManager();
                    p.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }
                break;
        }
    }
}

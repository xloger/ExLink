package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import com.xloger.exlink.app.R;

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
        if (v.getId()==debugMode.getId()){
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("isDebugMode",debugMode.isChecked());
            edit.apply();
        }else if(v.getId()==hiddenIcon.getId()){
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("isHiddenIcon",hiddenIcon.isChecked());
            edit.apply();
            if (hiddenIcon.isChecked()){
                PackageManager packageManager = getPackageManager();
                packageManager.setComponentEnabledSetting(getComponentName(),PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
            }else {
                PackageManager p = getPackageManager();
                p.setComponentEnabledSetting(getComponentName(),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
            }
        }
    }
}

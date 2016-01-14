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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        debugMode = (CheckBox) findViewById(R.id.setting_debug);

        debugMode.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("config", 0);
        boolean isDebugMode = sp.getBoolean("isDebugMode", false);
        debugMode.setChecked(isDebugMode);



    }


    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("config", 0);
        if (v.getId()==debugMode.getId()){
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("isDebugMode",debugMode.isChecked());
            edit.apply();
        }
    }
}

package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.util.FileUtil;

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

            FileUtil fileUtil = FileUtil.getInstance();
            fileUtil.save(Constant.IS_DEBUG_FILE_NAME, (debugMode.isChecked() + "").getBytes());
            fileUtil.setReadable(Constant.IS_DEBUG_FILE_NAME);
        }
    }
}

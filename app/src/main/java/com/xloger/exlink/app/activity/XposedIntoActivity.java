package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by xloger on 2月18日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
<<<<<<< HEAD
public class XposedIntoActivity extends Activity {
=======
public class XposedIntoActivity extends BaseActivity {
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}

package com.xloger.exlink.app.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import com.xloger.exlink.app.R;

/**
 * Created by xloger on 7月17日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class Loading extends Dialog {
    public Loading(Context context) {
        super(context,android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }
}

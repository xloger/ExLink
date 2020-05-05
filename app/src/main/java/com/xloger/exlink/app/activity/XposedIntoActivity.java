package com.xloger.exlink.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xloger.xlib.tool.XPermission;
import com.xloger.xlib.tool.Xlog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by xloger on 2月18日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class XposedIntoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Xlog.toast("该软件需要使用 SD 卡存储规则，请给予对应权限。不授予的话将只能使用默认规则。");
//        XPermission.INSTANCE.requestPermission(this, new XPermission.XPermissionCallback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onRefuse(@NotNull List<String> list) {
//                Xlog.toast("该软件需要使用 SD 卡存储规则，请给予对应权限。不授予的话将只能使用默认规则。");
//            }
//        }, XPermission.INSTANCE.getWrite_SD());
        Intent intent = new Intent(XposedIntoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

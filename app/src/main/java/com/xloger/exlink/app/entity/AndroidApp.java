package com.xloger.exlink.app.entity;

import android.graphics.drawable.Drawable;

/**
 * Created on 16/6/1 下午6:20.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class AndroidApp {
    private String name;
    private String packageName;
    private Drawable icon;
    private boolean isSystemApp;

    public AndroidApp() {
    }

    public AndroidApp(String name, String packageName, Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }
}

package com.xloger.exlink.app.entity;

import org.json.JSONObject;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class App {
    private int id;
    private String appName;
    private String packageName;
    private String activityName;
    private boolean isUse;
    private boolean isUserBuild;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public boolean isUse() {
        return isUse;
    }

    public void setIsUse(boolean isUse) {
        this.isUse = isUse;
    }

    public boolean isUserBuild() {
        return isUserBuild;
    }

    public void setIsUserBuild(boolean isUserBuild) {
        this.isUserBuild = isUserBuild;
    }

    public void parseJson(JSONObject jsonObject){
        //TODO 解析json
    }

    public String toJson(){
        String ret = null;
        //TODO 生成Json格式的字符串

        return ret;
    }
}

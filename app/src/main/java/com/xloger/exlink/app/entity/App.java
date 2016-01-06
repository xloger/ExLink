package com.xloger.exlink.app.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class App implements Serializable {
    private int id;
    private String appName;
    private String packageName;
    private String activityName;
    private String extrasKey;
    private boolean isUse;
    private boolean isUserBuild;
    private boolean isTest;

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

    public String getExtrasKey() {
        return extrasKey;
    }

    public void setExtrasKey(String extrasKey) {
        this.extrasKey = extrasKey;
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

    public boolean isTest() {
        return isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }

    public void parseJson(JSONObject jsonObject){
        //TODO 解析json
    }

    public String toJson(){
        String ret = null;
        //TODO 生成Json格式的字符串

        return ret;
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", activityName='" + activityName + '\'' +
                ", extrasKey='" + extrasKey + '\'' +
                ", isUse=" + isUse +
                ", isUserBuild=" + isUserBuild +
                ", isTest=" + isTest +
                '}';
    }
}

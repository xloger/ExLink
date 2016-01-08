package com.xloger.exlink.app.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class App implements Serializable {
    private int id;
    private String appName;
    private String packageName;
    private Set<Rule> rules;
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

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
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

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", rules=" + rules +
                ", isUse=" + isUse +
                ", isUserBuild=" + isUserBuild +
                ", isTest=" + isTest +
                '}';
    }
}

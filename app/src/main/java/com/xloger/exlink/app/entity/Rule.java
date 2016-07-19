package com.xloger.exlink.app.entity;

import java.io.Serializable;

/**
 * Created by xloger on 1月8日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class Rule implements Serializable {
    private String activityName;
    private String extrasKey;

    public Rule(String activityName, String extrasKey) {
        this.activityName = activityName;
        this.extrasKey = extrasKey;
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

    @Override
    public String toString() {
        return "Rule{" +
                "activityName='" + activityName + '\'' +
                ", extrasKey='" + extrasKey + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null||!(o instanceof Rule)) {
            return false;
        }
        if (!activityName.equals(((Rule) o).getActivityName())){
            return false;
        }

        if (!extrasKey.equals(((Rule) o).getExtrasKey())){
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 2*activityName.hashCode()+3*extrasKey.hashCode();
    }
}

package com.xloger.exlink.app.entity

import java.io.Serializable

/**
 * Created by xloger on 1月8日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
class Rule(var activityName: String?, var extrasKey: String?) : Serializable {

    override fun toString(): String {
        return "Rule{" +
                "activityName='" + activityName + '\'' +
                ", extrasKey='" + extrasKey + '\'' +
                '}'
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || o !is Rule) {
            return false
        }
        if (activityName != o.activityName) {
            return false
        }

        return extrasKey == o.extrasKey

    }

    override fun hashCode(): Int {
        return 2 * activityName!!.hashCode() + 3 * extrasKey!!.hashCode()
    }

    companion object {
        private const val serialVersionUID = 86222585354990243L
    }
}

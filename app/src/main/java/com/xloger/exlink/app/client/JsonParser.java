package com.xloger.exlink.app.client;


import com.xloger.exlink.app.entity.App;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class JsonParser {
    private JsonParser(){

    }

    public static List<App> getAppList(String json) throws JSONException {
        List<App> appList = null;
        if (json != null) {
            JSONObject jsonObject=new JSONObject(json);
            //TODO 解析 Json
        }

        return appList;
    }

    public static JSONObject toJson(Object object){
        JSONObject jsonObject=null;
        return jsonObject;
    }
}

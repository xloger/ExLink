package com.xloger.exlink.app.util;


import com.xloger.exlink.app.client.JsonParser;
import com.xloger.exlink.app.entity.App;
import org.json.JSONException;

import java.util.List;

/**
 * Created by xloger on 1月1日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class DataUtil {
    private static final String appFileName="appList";

    private DataUtil(){

    }


    public List<App> getAppList(){
        List<App> appList = null;
        byte[] bytes = FileUtil.getInstance().load(appFileName);
        String appJson=bytes.toString();
        try {
            appList= JsonParser.getAppList(appJson);
        } catch (JSONException e) {
            MyLog.log("Parser appList's json had an Error");
            e.printStackTrace();
        }
        return appList;
    }
}

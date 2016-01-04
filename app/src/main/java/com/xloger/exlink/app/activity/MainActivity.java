package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.AppAdapter;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {
    private List<App> appList;
    private ListView listView;
    private AppAdapter appAdapter;
    private Button addApp;

    private static final int nowInitVersion=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        SharedPreferences sp = getSharedPreferences("config", 0);
        int initVersion = sp.getInt("initVersion", 0);

        Object firstRun = FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
        if (firstRun==null||nowInitVersion!=initVersion) {
            initAppData();
            FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
            FileUtil.getInstance().setReadable(Constant.APP_FILE_NAME);

            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("initVersion",nowInitVersion);
            editor.apply();
        }else {
            appList= (List<App>) firstRun;
        }


        appAdapter = new AppAdapter(this,appList);
        listView.setAdapter(appAdapter);


    }

    private void initView(){
        listView = (ListView) findViewById(R.id.app_list);
        addApp = (Button) findViewById(R.id.add_app);
    }

    private void initAppData(){
        appList=new LinkedList<App>();
        App qq=new App();
        qq.setAppName("QQ");
        qq.setPackageName("com.tencent.mobileqq");
        qq.setActivityName("com.tencent.mobileqq.activity.QQBrowserDelegationActivity");
        qq.setIsUse(true);
        qq.setIsUserBuild(false);
        appList.add(qq);

        App qqLite=new App();
        qqLite.setAppName("QQ轻聊版");
        qqLite.setPackageName("com.tencent.qqlite");
        qqLite.setActivityName("com.tencent.mobileqq.activity.QQBrowserDelegationActivity");
        qqLite.setIsUse(true);
        qqLite.setIsUserBuild(false);
        appList.add(qqLite);

        App qqi=new App();
        qqi.setAppName("QQ国际版");
        qqi.setPackageName("com.tencent.mobileqqi");
        qqi.setActivityName("com.tencent.mobileqq.activity.QQBrowserDelegationActivity");
        qqi.setIsUse(true);
        qqi.setIsUserBuild(false);
        appList.add(qqi);

//        App weChat=new App();
//        weChat.setAppName("微信");
//        weChat.setPackageName("com.tencent.mm");
//
//        weChat.setIsUse(true);
//        weChat.setIsUserBuild(false);
//        appList.add(weChat);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.xloger.exlink.app.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private List<App> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Object firstRun = FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
        if (true) {
            initAppData();
            FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
        }

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                List<App> appList=new LinkedList<App>();
//
//                App app=new App();
//                app.setId(1);
//                app.setAppName("123");
//                app.setPackageName("456");
//
//                appList.add(app);
//
//                App app3=new App();
//                app3.setId(3);
//                app3.setAppName("33");
//                app3.setPackageName("336");
//                appList.add(app3);
//
//                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);

            }
        });
        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object test = FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
                List<App> app2= (List<App>) test;
                Toast.makeText(MainActivity.this,app2.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAppData(){
        appList=new LinkedList<App>();
        App qq=new App();
        qq.setAppName("QQ");
        qq.setPackageName("com.tencent.mobileqq");
        qq.setActivityName("com.tencent.mobileqq.activity.QQBrowserDelegationActivity");
        appList.add(qq);

        App qqLite=new App();
        qqLite.setAppName("QQ轻聊版");
        qqLite.setPackageName("com.tencent.qqlite");
        qqLite.setActivityName("com.tencent.mobileqq.activity.QQBrowserDelegationActivity");
        appList.add(qqLite);

        App qqi=new App();
        qqi.setAppName("QQ国际版");
        qqi.setPackageName("com.tencent.mobileqqi");
        qqi.setActivityName("com.tencent.mobileqq.activity.QQBrowserDelegationActivity");
        appList.add(qqi);

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

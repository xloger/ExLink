package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.AppAdapter;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private List<App> appList;
    private ListView listView;
    private AppAdapter appAdapter;
    private Button addApp;

    private Context context;

    private static final int nowInitVersion=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAppList();

        context=MainActivity.this;

        appAdapter = new AppAdapter(this,appList,itemCallBack);
        listView.setAdapter(appAdapter);

        addApp.setOnClickListener(this);

    }

    private void initView(){
        listView = (ListView) findViewById(R.id.app_list);
        addApp = (Button) findViewById(R.id.add_app);
    }

    private void initAppList(){
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
    public void onClick(View v) {
        if (v.getId()==addApp.getId()){
            final View oneStepView = LayoutInflater.from(MainActivity.this).inflate(R.layout.step_one, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("步骤一").setView(oneStepView);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText editText= (EditText) oneStepView.findViewById(R.id.new_rule_package_name);
                    String newRulePackageName=editText.getText().toString();
                    if (!"".equals(newRulePackageName)&&newRulePackageName.length()!=0) {
                        App testApp=new App();
                        testApp.setAppName("测试用例");
                        testApp.setPackageName(newRulePackageName);
                        testApp.setIsUse(true);
                        testApp.setIsUserBuild(true);
                        testApp.setIsTest(true);

                        appList.add(testApp);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                    }
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

    private static String[] longClickList=new String[]{"删除"};

    private ItemCallBack itemCallBack=new ItemCallBack() {
        @Override
        public void onClick(int position, View view) {
            if (view instanceof CheckBox){
                CheckBox checkBox= (CheckBox) view;
                appList.get(position).setIsUse(checkBox.isChecked());
                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                Toast.makeText(context,"规则已保存，重启后生效",Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onLongClick(final int position, View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("操作");
            builder.setItems(longClickList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==0){
                        if (!appList.get(position).isUserBuild()){
                            Toast.makeText(context,"系统规则不允许删除",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        appList.remove(position);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                        appAdapter.notifyDataSetChanged();
                    }
                }
            });
            builder.create().show();
        }
    };


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    public interface ItemCallBack{
        void onClick(int position,View view);
        void onLongClick(int position,View view);
    }
}

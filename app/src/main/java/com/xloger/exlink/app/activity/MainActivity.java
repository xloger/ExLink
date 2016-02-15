package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.xloger.exlink.app.BuildConfig;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.AppAdapter;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.entity.Rule;
import com.xloger.exlink.app.util.FileUtil;
import com.xloger.exlink.app.util.MyLog;
import com.xloger.exlink.app.util.ViewTool;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener {
    private List<App> appList;
    private ListView listView;
    private AppAdapter appAdapter;
    private Button addApp;

    private Context context;

    private static final int nowInitVersion=10;
    private Button show;
    private TextView readme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        initView();
        initAppList();
        openStepTwo();
        debugMode();

        appAdapter = new AppAdapter(this,appList,itemCallBack);
        listView.setAdapter(appAdapter);
        ViewTool.setListViewHeightBasedOnChildren(listView);

        addApp.setOnClickListener(this);
        readme.setOnClickListener(this);
        readme.getPaint().setAntiAlias(true);

    }

    private void initView(){
        listView = (ListView) findViewById(R.id.app_list);
        addApp = (Button) findViewById(R.id.add_app);
        show = (Button) findViewById(R.id.show);
        readme = (TextView) findViewById(R.id.main_read_me);
    }

    private void initAppList(){
        SharedPreferences sp = getSharedPreferences("config", 0);
        int initVersion = sp.getInt("initVersion", 0);

        Object firstRun = FileUtil.loadObject(Constant.APP_URL, Constant.APP_FILE_NAME);
        if (firstRun==null) {
            initAppData();
            FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
            FileUtil.getInstance().setReadable(Constant.APP_FILE_NAME);


        }else {
            appList= (List<App>) firstRun;
        }

        if(nowInitVersion!=initVersion){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("initVersion",nowInitVersion);
            editor.apply();

            int initAppVersion = sp.getInt("initAppVersion", -1);
            SharedPreferences.Editor editor2 = sp.edit();
            editor2.putInt("initAppVersion", BuildConfig.VERSION_CODE);
            editor2.apply();

            if (initAppVersion>=5){
                //大于1.3版本执行更新操作
                updateAppData();
                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME, appList);
            }


        }


    }

    private void initAppData(){
        MyLog.log("进行了初始化规则");
        appList=new LinkedList<App>();
        App qq=new App();
        qq.setAppName("QQ");
        qq.setPackageName("com.tencent.mobileqq");
        Set<Rule> qqRule=new HashSet<Rule>();
        qqRule.add(new Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity","url"));
        qq.setRules(qqRule);
        Set<String> qqWhiteUrl=new HashSet<String>();
        qqWhiteUrl.add("h5.qzone.qq.com");
        qqWhiteUrl.add("qzs.qzone.qq.com");
        qqWhiteUrl.add("jq.qq.com");
        qq.setWhiteUrl(qqWhiteUrl);
        qq.setIsUse(true);
        qq.setIsUserBuild(false);
        appList.add(qq);

        App qqLite=new App();
        qqLite.setAppName("QQ轻聊版");
        qqLite.setPackageName("com.tencent.qqlite");
        Set<Rule> qqLiteRule=new HashSet<Rule>();
        qqLiteRule.add(new Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity","url"));
        qqLite.setRules(qqLiteRule);
        qqLite.setIsUse(true);
        qqLite.setIsUserBuild(false);
        appList.add(qqLite);

        App qqi=new App();
        qqi.setAppName("QQ国际版");
        qqi.setPackageName("com.tencent.mobileqqi");
        Set<Rule> qqiRule=new HashSet<Rule>();
        qqiRule.add(new Rule("com.tencent.mobileqq.activity.QQBrowserDelegationActivity", "url"));
        qqi.setRules(qqiRule);
        qqi.setIsUse(true);
        qqi.setIsUserBuild(false);
        appList.add(qqi);

        App tieba=new App();
        tieba.setAppName("百度贴吧");
        tieba.setPackageName("com.baidu.tieba");
        Set<Rule> tiebaRule=new HashSet<Rule>();
        tiebaRule.add(new Rule("com.baidu.tieba.imMessageCenter.im.chat.PersonalChatActivity","tag_url"));
        tiebaRule.add(new Rule("com.baidu.tieba.pb.pb.main.PbActivity","tag_url"));
        tieba.setRules(tiebaRule);
        tieba.setIsUse(true);
        tieba.setIsUserBuild(false);
        appList.add(tieba);

        App weibo=new App();
        weibo.setAppName("微博");
        weibo.setPackageName("com.sina.weibo");
        Set<Rule> weiboRule=new HashSet<Rule>();
        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.weiyou.DMSingleChatActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.page.NewCardListActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.feed.DetailWeiboActivity","com_sina_weibo_weibobrowser_url"));        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));
        weibo.setRules(weiboRule);
        Set<String> weiboWhiteUrl=new HashSet<String>();
        weiboWhiteUrl.add("card.weibo.com");
        weibo.setWhiteUrl(weiboWhiteUrl);
        weibo.setIsUse(true);
        weibo.setIsUserBuild(false);
        appList.add(weibo);

        App weChat=new App();
        weChat.setAppName("微信");
        weChat.setPackageName("com.tencent.mm");
        Set<Rule> weChatRule=new HashSet<Rule>();
        weChatRule.add(new Rule("com.tencent.mm.ui.LauncherUI","rawUrl"));
        weChat.setRules(weChatRule);
        Set<String> weChatWhiteUrl=new HashSet<String>();
        weChatWhiteUrl.add("open.weixin.qq.com");
        weChat.setWhiteUrl(weChatWhiteUrl);
        weChat.setIsUse(true);
        weChat.setIsUserBuild(false);
        appList.add(weChat);

    }

    private void updateAppData(){
        if (appList.size()==6){
            return;
        }

        List<App> tempAppList=appList.subList(0,appList.size()-1);

        initAppData();
        for (int i=7;i<tempAppList.size();i++){
            appList.add(tempAppList.get(i));
        }
        
    }

    private void openStepTwo(){
        for (int i=0;i<appList.size();i++){
            if (appList.get(i).isTest()){
                Intent intent = new Intent(context,StepTwoActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void debugMode(){
        SharedPreferences sp = getSharedPreferences("config", 0);
        boolean isDebugMode = sp.getBoolean("isDebugMode", false);

        if (isDebugMode){
            show.setVisibility(View.VISIBLE);
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView= (TextView) MainActivity.this.findViewById(R.id.show_text);
                    textView.setText(appList.toString());
                    textView.setTextIsSelectable(true);
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==addApp.getId()){
            final View oneStepView = LayoutInflater.from(MainActivity.this).inflate(R.layout.step_one, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(getString(R.string.step_one)).setView(oneStepView);
            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText appEditText= (EditText) oneStepView.findViewById(R.id.new_rule_app_name);
                    EditText packageEditText= (EditText) oneStepView.findViewById(R.id.new_rule_package_name);
                    String newRuleAppName=appEditText.getText().toString();
                    String newRulePackageName=packageEditText.getText().toString();
                    if (!"".equals(newRulePackageName)&&newRulePackageName.length()!=0) {
                        App testApp=new App();
                        if ("".equals(newRuleAppName)) {
                            testApp.setAppName(getString(R.string.demo));
                        }else {
                            testApp.setAppName(newRuleAppName);
                        }
                        testApp.setPackageName(newRulePackageName);
                        testApp.setIsUse(true);
                        testApp.setIsUserBuild(true);
                        testApp.setIsTest(true);

                        appList.add(testApp);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                        ViewTool.setListViewHeightBasedOnChildren(listView);
                        Toast.makeText(context,getString(R.string.step_one_text4),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }else if(v.getId()==readme.getId()){
            Intent intent=new Intent(context,ReadMeActivity.class);
            startActivity(intent);
        }
    }

//    private String[] longClickList=new String[]{getString(R.string.item_click_text1),getString(R.string.item_click_text2),getString(R.string.item_click_text3)};

    private ItemCallBack itemCallBack=new ItemCallBack() {
        @Override
        public void onClick(int position, View view) {
            if (view instanceof CheckBox){
                CheckBox checkBox= (CheckBox) view;
                appList.get(position).setIsUse(checkBox.isChecked());
                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                Toast.makeText(context,getString(R.string.item_check_text),Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onLongClick(final int position, View view) {
            String[] longClickList=new String[]{getString(R.string.item_click_text1),getString(R.string.item_click_text2),getString(R.string.item_click_text3)};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(getString(R.string.item_click_title));
            builder.setItems(longClickList, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which==0){
                        appList.get(position).setIsTest(true);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                        openStepTwo();
                    }else if (which==1){
                        final View oneStepView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_white, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(getString(R.string.add_white_title)).setView(oneStepView);
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText whiteNameEditText= (EditText) oneStepView.findViewById(R.id.add_white_name);
                                String whiteName=whiteNameEditText.getText().toString();
                                if (!"".equals(whiteName)&&whiteName.length()!=0) {
                                    App changeApp=appList.get(position);
                                    Set<String> whiteUrl = changeApp.getWhiteUrl();
                                    if (whiteUrl == null) {
                                        whiteUrl=new HashSet<String>();
                                        changeApp.setWhiteUrl(whiteUrl);
                                    }
                                    if (!"".equals(whiteName)) {
                                        whiteUrl.add(whiteName);
                                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                                        Toast.makeText(context,getString(R.string.add_white_succeed),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }else if (which==2){
                        if (!appList.get(position).isUserBuild()){
                            Toast.makeText(context,getString(R.string.del_system_rule_toask),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        appList.remove(position);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                        ViewTool.setListViewHeightBasedOnChildren(listView);
                        appAdapter.notifyDataSetChanged();
                    }
                }
            });
            builder.create().show();
        }
    };


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
            Intent intent=new Intent(context,SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public interface ItemCallBack{
        void onClick(int position,View view);
        void onLongClick(int position,View view);
    }
}

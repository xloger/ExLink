package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
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
import com.xloger.exlink.app.view.AddWhiteDialog;
import com.xloger.exlink.app.view.StepOneDialog;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private List<App> appList;
    private ListView listView;
    private AppAdapter appAdapter;
    private FloatingActionButton addApp;
    private boolean isShowingDebug=false;

    private Context context;

    private static final int nowInitVersion=14;
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

//        setOverflowShowingAlways();

    }

    @Override
    protected void onResume() {
        super.onResume();
        upDateAppList();
        appAdapter.notifyDataSetChanged();
        debugMode();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.app_list);
        addApp = (FloatingActionButton) findViewById(R.id.add_app);
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

            int initAppVersionCode = sp.getInt("initAppVersionCode", -1);
            String initAppVersionName=sp.getString("initAppVersion","");
            SharedPreferences.Editor editor2 = sp.edit();
            editor2.putString("initAppVersion", BuildConfig.VERSION_NAME);
            editor2.putInt("initAppVersionCode", BuildConfig.VERSION_CODE);
            editor2.apply();

            if (initAppVersionCode>=5){
                //大于1.3版本执行更新操作
                updateAppData();
                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME, appList);
            }else if(initAppVersionCode==-1){
                updateAppData();
                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME, appList);
            } else {
                initAppData();
                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME, appList);
                FileUtil.getInstance().setReadable(Constant.APP_FILE_NAME);

            }


        }


    }

    private void upDateAppList(){
        List<App> apps = FileUtil.getAppList();
        if (apps != null) {
            appList.clear();
            appList.addAll(apps);
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
//        qqWhiteUrl.add("h5.qzone.qq.com");
//        qqWhiteUrl.add("qzs.qzone.qq.com");
        qqWhiteUrl.add("qzone.qq.com");
        qqWhiteUrl.add("qun.qq.com");
        qqWhiteUrl.add("jq.qq.com");
        qqWhiteUrl.add("mqq.tenpay.com");
        qqWhiteUrl.add("mp.qq.com");
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
        weiboRule.add(new Rule("com.sina.weibo.feed.DetailWeiboActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","com_sina_weibo_weibobrowser_url"));
        weiboRule.add(new Rule("com.sina.weibo.feed.HomeListActivity","ExDat"));
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
        if (appList.size()<=6){
            initAppData();
            return;
        }

        List<App> tempAppList=appList.subList(0,appList.size());

        initAppData();
        for (int i=6;i<tempAppList.size();i++){
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
            show.setClickable(true);
            show.setOnClickListener(this);
        }else {
            show.setVisibility(View.GONE);
            show.setClickable(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_app:
                StepOneDialog dialog=new StepOneDialog(context);
                dialog.setPositiveListener(new StepOneDialog.StepOneCallBack() {
                    @Override
                    public void onPositiveClick(App app) {
                        appList.add(app);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                        ViewTool.setListViewHeightBasedOnChildren(listView);
                    }
                });
                StepOneDialog.Builder builder=dialog.getBuilder();
                builder.create().show();
                break;
            case R.id.main_read_me:
                Intent intent=new Intent(context,ReadMeActivity.class);
                startActivity(intent);
                break;
            case R.id.show:
                TextView textView= (TextView) MainActivity.this.findViewById(R.id.show_text);
                if (isShowingDebug){
                    textView.setText("");
                }else {
                    textView.setText(appList.toString());
                    textView.setTextIsSelectable(true);
                }
                isShowingDebug=!isShowingDebug;
                break;
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
            }else {
                onLongClick(position,view);
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
//                        appList.get(position).setIsTest(true);
//                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
//                        openStepTwo();
                        Intent intent=new Intent(context,EditRuleActivity.class);
                        intent.putExtra("position",position);
                        startActivity(intent);
                    }else if (which==1){
                        AddWhiteDialog addWhiteDialog=new AddWhiteDialog(context,appList.get(position));
                        addWhiteDialog.setCallBack(new AddWhiteDialog.AddWhiteCallBack() {
                            @Override
                            public void onPositiveClick() {

                            }

                            @Override
                            public void saveWhiteList() {
                                FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                            }
                        });
                        android.support.v7.app.AlertDialog.Builder builder = addWhiteDialog.getBuilder();
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
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_instruction:
                intent= new Intent(context,ReadMeActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent=new Intent(context,SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.action_about:
                intent=new Intent(context,AboutActivity.class);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 通过反射，强制显示Overflow（即右上角的菜单按钮）
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface ItemCallBack{
        void onClick(int position,View view);
        void onLongClick(int position,View view);
    }
}

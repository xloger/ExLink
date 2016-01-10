package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.AppAdapter;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.entity.Rule;
import com.xloger.exlink.app.util.FileUtil;
import com.xloger.exlink.app.util.MyLog;
import com.xloger.exlink.app.util.StreamUtil;
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

    private static final int nowInitVersion=7;
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

        appAdapter = new AppAdapter(this,appList,itemCallBack);
        listView.setAdapter(appAdapter);
        ViewTool.setListViewHeightBasedOnChildren(listView);

        addApp.setOnClickListener(this);
        readme.setOnClickListener(this);
        readme.getPaint().setAntiAlias(true);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView= (TextView) MainActivity.this.findViewById(R.id.show_text);
                textView.setText(appList.toString());
//                String s="sinaweibo://browser?url=http://share.acg.tv/av3439126&sinainternalbrowser=topnav&share_menu=1&url_type=39&object_type=video&pos=2";
//                String ret= StreamUtil.parseUrl(s);
//                textView.setText(ret);
            }
        });

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
        weibo.setRules(weiboRule);
        Set<String> weiboWhiteUrl=new HashSet<String>();
        weiboWhiteUrl.add("card.weibo.com");
        weibo.setWhiteUrl(weiboWhiteUrl);
        weibo.setIsUse(true);
        weibo.setIsUserBuild(false);
        appList.add(weibo);

//        App weChat=new App();
//        weChat.setAppName("微信");
//        weChat.setPackageName("com.tencent.mm");
//
//        weChat.setIsUse(true);
//        weChat.setIsUserBuild(false);
//        appList.add(weChat);

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

    @Override
    public void onClick(View v) {
        if (v.getId()==addApp.getId()){
            final View oneStepView = LayoutInflater.from(MainActivity.this).inflate(R.layout.step_one, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("步骤一").setView(oneStepView);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText appEditText= (EditText) oneStepView.findViewById(R.id.new_rule_app_name);
                    EditText packageEditText= (EditText) oneStepView.findViewById(R.id.new_rule_package_name);
                    String newRuleAppName=appEditText.getText().toString();
                    String newRulePackageName=packageEditText.getText().toString();
                    if (!"".equals(newRulePackageName)&&newRulePackageName.length()!=0) {
                        App testApp=new App();
                        if ("".equals(newRuleAppName)) {
                            testApp.setAppName("测试用例");
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
                        Toast.makeText(context,"请重启手机，之后再打开本App",Toast.LENGTH_SHORT).show();
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
        }else if(v.getId()==readme.getId()){
            Intent intent=new Intent(context,ReadMeActivity.class);
            startActivity(intent);
        }
    }

    private static String[] longClickList=new String[]{"添加规则","添加白名单","删除"};

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
                        appList.get(position).setIsTest(true);
                        FileUtil.getInstance().saveObject(Constant.APP_FILE_NAME,appList);
                        openStepTwo();
                    }else if (which==1){
                        final View oneStepView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_white, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("添加白名单").setView(oneStepView);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                                        Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                                    }
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
                    }else if (which==2){
                        if (!appList.get(position).isUserBuild()){
                            Toast.makeText(context,"系统规则不允许删除",Toast.LENGTH_SHORT).show();
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

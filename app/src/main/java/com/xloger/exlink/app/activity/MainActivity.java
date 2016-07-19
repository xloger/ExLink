package com.xloger.exlink.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.*;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.AppAdapter;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.AppUtil;
import com.xloger.exlink.app.util.MyLog;
import com.xloger.exlink.app.util.ViewTool;
import com.xloger.exlink.app.view.AddWhiteDialog;
import com.xloger.exlink.app.view.StepOneDialog;

import java.lang.reflect.Field;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private List<App> appList;
    private ListView listView;
    private AppAdapter appAdapter;
    private FloatingActionButton addApp;
    private boolean isShowingDebug=false;

    private Context context;

    private Button show;
    private TextView readme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=MainActivity.this;
        initView();
        //为了方便，每次进该App时更新一次规则
        AppUtil.updateAppData();
        appList= AppUtil.getAppList();
        MyLog.e(appList.toString());
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
        appList=AppUtil.getAppList();
        appAdapter.notifyDataSetChanged();
        debugMode();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.app_list);
        addApp = (FloatingActionButton) findViewById(R.id.add_app);
        show = (Button) findViewById(R.id.show);
        readme = (TextView) findViewById(R.id.main_read_me);
    }



    private void openStepTwo(){
        for (int i=0;i<appList.size();i++){
            if (appList.get(i).isUse()&&appList.get(i).isTest()){
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
                        AppUtil.save(appList);
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
                AppUtil.save(appList);
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
                                AppUtil.save(appList);
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
                        AppUtil.save(appList);
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

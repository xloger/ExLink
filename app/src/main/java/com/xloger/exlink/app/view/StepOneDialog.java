package com.xloger.exlink.app.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.adapter.ChooseAppAdapter;
import com.xloger.exlink.app.entity.AndroidApp;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.AndroidAppUtil;

import java.util.List;

/**
 * Created on 16/6/1 下午3:57.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
public class StepOneDialog extends AlertDialog implements View.OnClickListener {
    private Context context;
    private StepOneCallBack callBack;
    private EditText appEditText;
    private EditText packageEditText;
    private Button chooseBtn;

    public StepOneDialog(Context context) {
        super(context);
        this.context=context;

    }

    public Builder getBuilder(){
        Builder builder=new Builder(context);
        final View oneStepView = LayoutInflater.from(context).inflate(R.layout.step_one, null);
        chooseBtn = (Button) oneStepView.findViewById(R.id.step_one_choose);
        appEditText = (EditText) oneStepView.findViewById(R.id.new_rule_app_name);
        packageEditText = (EditText) oneStepView.findViewById(R.id.new_rule_package_name);
        chooseBtn.setOnClickListener(this);
        builder.setTitle(getString(R.string.step_one));
        builder.setView(oneStepView);
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newRuleAppName= appEditText.getText().toString();
                String newRulePackageName= packageEditText.getText().toString();
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

                    if (callBack != null) {
                        callBack.onPositiveClick(testApp);
                    }
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
        return builder;
    }

    private String getString(int id){
        return context.getString(id);
    }

    public void setPositiveListener(StepOneCallBack callBack){
        this.callBack=callBack;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.step_one_choose:
                openChooseDialog();
                break;
        }
    }

    private void openChooseDialog(){
        new ChooseAppTask().execute();
    }

    class ChooseAppTask extends AsyncTask<Void,Void,List<AndroidApp>>{

        private Loading loading;

        @Override
        protected void onPreExecute() {
            loading = new Loading(context);
            loading.show();
        }

        @Override
        protected void onPostExecute(final List<AndroidApp> appList) {
            loading.dismiss();
            AlertDialog.Builder builder=new Builder(context);
            ChooseAppAdapter adapter=new ChooseAppAdapter(context,appList);
            builder.setAdapter(adapter, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    appEditText.setText(appList.get(which).getName());
                    packageEditText.setText(appList.get(which).getPackageName());
                }
            });
            builder.create().show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<AndroidApp> doInBackground(Void... params) {
            List<AndroidApp> appList = AndroidAppUtil.getUserAppInfo(context);
            return appList;
        }
    }


    public interface StepOneCallBack{
        void onPositiveClick(App app);
    }

}



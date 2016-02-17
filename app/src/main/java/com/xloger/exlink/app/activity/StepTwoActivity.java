package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.util.FileUtil;

/**
 * Created by xloger on 1月6日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
public class StepTwoActivity extends Activity implements View.OnClickListener {

    private TextView urlTextView;
    private Button differentUrlButton;
    private EditText differentUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_two);
        urlTextView = (TextView) findViewById(R.id.step_two_url);
        urlTextView.setOnClickListener(this);

        differentUrlEditText = (EditText) findViewById(R.id.step_two_different_url);
        differentUrlButton = (Button) findViewById(R.id.step_two_different_button);
        differentUrlButton.setOnClickListener(this);

        FileUtil fileUtil = FileUtil.getInstance();
        byte[] bytes = fileUtil.load(Constant.DIFFERENT_URL_FILE_NAME);
        if (bytes!=null&&bytes[0]=='1'){
            urlTextView.setText(new String(bytes,1,bytes.length-1));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.step_two_url:
                ClipboardManager cmb = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(urlTextView.getText()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
//              cm.getText();//获取粘贴信息
                Toast.makeText(this,getString(R.string.copy_succeed),Toast.LENGTH_SHORT).show();
                break;
            case R.id.step_two_different_button:
                String differentUrl=differentUrlEditText.getText().toString();
                if (differentUrl.length()!=0) {
                    FileUtil fileUtil = FileUtil.getInstance();
                    fileUtil.save(Constant.DIFFERENT_URL_FILE_NAME,("1"+differentUrl).getBytes());
                    fileUtil.setReadable(Constant.DIFFERENT_URL_FILE_NAME);
                    Toast.makeText(this, getString(R.string.change_succeed),Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }
}

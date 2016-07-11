package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.support.v4.content.ContextCompat;
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.util.FileUtil;
<<<<<<< HEAD
=======
import com.xloger.exlink.app.util.MyLog;
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604

/**
 * Created by xloger on 1月6日.
 * Author:xloger
 * Email:phoenix@xloger.com
 */
<<<<<<< HEAD
public class StepTwoActivity extends Activity implements View.OnClickListener {
=======
public class StepTwoActivity extends BaseActivity implements View.OnClickListener {
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604

    private TextView urlTextView;
    private Button differentUrlButton;
    private EditText differentUrlEditText;
<<<<<<< HEAD
=======
    private boolean isShowCustom=false;
    private View customLayout;
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_two);
<<<<<<< HEAD
        urlTextView = (TextView) findViewById(R.id.step_two_url);
        urlTextView.setOnClickListener(this);

        differentUrlEditText = (EditText) findViewById(R.id.step_two_different_url);
        differentUrlButton = (Button) findViewById(R.id.step_two_different_button);
        differentUrlButton.setOnClickListener(this);
=======

        String title = getIntent().getStringExtra("title");
        if (title!=null&&!"".equals(title)) {
            setTitle(title);
        }else {
            setTitle(getResources().getString(R.string.step_two));
        }

        urlTextView = (TextView) findViewById(R.id.step_two_url);
        differentUrlEditText = (EditText) findViewById(R.id.step_two_different_url);
        differentUrlButton = (Button) findViewById(R.id.step_two_different_button);
        Button customBtn=(Button)findViewById(R.id.step_two_custom_btn);
        Button defaultBtn=(Button)findViewById(R.id.step_two_back_default);
        customLayout = findViewById(R.id.step_two_custom_layout);
        urlTextView.setOnClickListener(this);
        differentUrlButton.setOnClickListener(this);
        customBtn.setOnClickListener(this);
        defaultBtn.setOnClickListener(this);
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604

        FileUtil fileUtil = FileUtil.getInstance();
        byte[] bytes = fileUtil.load(Constant.DIFFERENT_URL_FILE_NAME);
        if (bytes!=null&&bytes[0]=='1'){
            urlTextView.setText(new String(bytes,1,bytes.length-1));
        }

    }

<<<<<<< HEAD
=======

>>>>>>> fb21a1c5d161c95756e68719066566aeed260604
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
<<<<<<< HEAD
                    Toast.makeText(this, getString(R.string.change_succeed),Toast.LENGTH_SHORT).show();
                }
                break;

=======
                    urlTextView.setText(differentUrl);
                    Toast.makeText(this, getString(R.string.change_succeed),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.step_two_custom_btn:
                if (isShowCustom){
                    customLayout.setVisibility(View.GONE);
                }else {
                    customLayout.setVisibility(View.VISIBLE);
                }
                isShowCustom=!isShowCustom;
                break;
            case R.id.step_two_back_default:
                FileUtil.getInstance().save(Constant.DIFFERENT_URL_FILE_NAME,"0".getBytes());
                urlTextView.setText(getString(R.string.test_url));
                break;
>>>>>>> fb21a1c5d161c95756e68719066566aeed260604
        }

    }
}

package com.xloger.exlink.app.activity;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.xloger.exlink.app.Constant;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.entity.App;
import com.xloger.exlink.app.util.FileUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp=MainActivity.this.getSharedPreferences("123",0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("123","123");
                editor.apply();

                FileUtil.getInstance().save("file","2333".getBytes());

                List<App> appList=new LinkedList<App>();

                App app=new App();
                app.setId(1);
                app.setAppName("123");
                app.setPackageName("456");

                appList.add(app);

                App app3=new App();
                app3.setId(3);
                app3.setAppName("33");
                app3.setPackageName("336");
                appList.add(app3);

                FileUtil.getInstance().saveObject("test",appList);

            }
        });
        Button button2=(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object test = FileUtil.loadObject(Constant.APP_URL, "test");
                List<App> app2= (List<App>) test;
//                Toast.makeText(MainActivity.this,"id:"+app2.getId()+" appname:"+app2.getAppName(),Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,app2.toString(),Toast.LENGTH_SHORT).show();
            }
        });
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

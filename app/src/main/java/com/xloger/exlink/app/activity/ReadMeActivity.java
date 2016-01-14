package com.xloger.exlink.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.xloger.exlink.app.BuildConfig;
import com.xloger.exlink.app.R;
import com.xloger.exlink.app.util.MyLog;

import java.util.Locale;

public class ReadMeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);

        WebView webView= (WebView) findViewById(R.id.read_me_web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        String language = Locale.getDefault().getLanguage();
        if (language.equals("zh")){
            webView.loadUrl("file:///android_asset/readme.html");
        }else {
            webView.loadUrl("file:///android_asset/readme_en.html");
        }


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.startsWith("http")) {
                    return false;
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        MyLog.log(e);
                    }
                    return true;
                }

            }
        });

    }


}

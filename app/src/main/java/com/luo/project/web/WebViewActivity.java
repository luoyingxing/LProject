package com.luo.project.web;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.luo.project.R;
import com.luo.project.vector.VectorActivity;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.web_view);
        textView = (TextView) findViewById(R.id.tv_load);

        webView.requestFocusFromTouch();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSBridge(), "android");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("WebViewActivity", "url -- " + url);
                view.loadUrl(url);
                count++;
                return true;
            }

        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                textView.setText("进度：" + newProgress + "%");
            }

        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:sum(3,8)");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    testEvaluateJavascript(webView);
                }
            }
        });

    }

    public class JSBridge {
        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), "通过Natvie传递的Toast:" + message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void jump() {
            startActivity(new Intent(WebViewActivity.this, VectorActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void testEvaluateJavascript(WebView webView) {
        webView.evaluateJavascript("getGreetings()", new ValueCallback() {

            @Override
            public void onReceiveValue(Object value) {
                Log.i("WebViewActivity", "value : " + value);
                textView.append((CharSequence) value);
            }
        });
    }

    private String url;
    private int count;

    @Override
    protected void onStart() {
        super.onStart();

        url = "https://www.baidu.com";

        // 打开本地sd卡内的index.html文件
        // "content://com.android.htmlfileprovider/sdcard/index.html"

//        url = "file:///android_asset/web.html";
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (count > 0) {
            webView.goBack();
            count--;
        } else {
            super.onBackPressed();
        }
    }
}
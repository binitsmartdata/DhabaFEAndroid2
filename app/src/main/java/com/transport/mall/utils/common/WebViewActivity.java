package com.transport.mall.utils.common;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.transport.mall.R;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    String url;
    ProgressDialog progressDialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        findViewById(R.id.close).setOnClickListener(v -> {
            finish();
        });
        url = "https://transportmall.com/About-Us.html";
        String from = getIntent().getStringExtra("from");
        if (from != null && !from.isEmpty()) {
            if (from.equalsIgnoreCase("privacy_policy")) {
                url = "https://transportmall.com/privacy-policy.html";
            } else if (from.equalsIgnoreCase("terms_conditions")) {
                url = "https://transportmall.com/terms-and-conditions.html";
            } else {
                url = "https://transportmall.com/About-Us.html";
            }
        }
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.please_wait));
        progressDialog.setIndeterminateDrawable(ContextCompat.getDrawable(WebViewActivity.this,
                R.drawable.progress_drawable));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        webView.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    progressDialog = null;
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }
}
package com.xuhongchuan.axenote.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xuhongchuan.axenote.utils.L;

/**
 * Created by xuhongchuan on 16/5/3.
 */
public class RichEditor extends WebView {

    private static final String SETUP_HTML = "file:///android_asset/webview2.html";

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint("setJavaScriptEnabled")
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getSettings().setJavaScriptEnabled(true); // 支持JS
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new EditorWebViewClient());
        loadUrl(SETUP_HTML);
//        loadUrl("https://www.baidu.com/");
    }

    protected class EditorWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}

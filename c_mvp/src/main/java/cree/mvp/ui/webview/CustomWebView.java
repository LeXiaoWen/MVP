package cree.mvp.ui.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.net.URL;

import cree.mvp.R;

/**
 * Title: 自定义的带进度条webview
 * Description:
 * Copyright:Copyright(c)2017
 * Company: Cree
 * CreateTime:2017/5/8  16:09
 *
 * @author luyongjiang
 * @version 1.0
 */
public class CustomWebView extends FrameLayout {

    private ViewGroup mGroup;
    private ProgressBar mProgressBar;
    private LayoutInflater mFrom;
    private WebView mWebView;
    private CustomChromeWebClient mClient;
    private CustomWebClient mViewClient;
    private CustomWebViewIntercept mCustomWebViewIntercept;


    public CustomWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CustomWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mFrom = LayoutInflater.from(context);
        mGroup = (ViewGroup) mFrom.inflate(R.layout.wi_cust_web_layout, null, false);
        this.addView(mGroup);
        mProgressBar = (ProgressBar) mGroup.findViewById(R.id.pb_loading);
        mWebView = (WebView) mGroup.findViewById(R.id.wv_web);
        initConfig();


    }

    private void initConfig() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置JS支持
        webSettings.setUseWideViewPort(true);//设置图片适合webview
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不缓存web内容
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置可以通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);//设置自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置html编码方式

        webSettings.setSupportZoom(true);//设置支持缩放
        webSettings.setBuiltInZoomControls(true);//设置是否启用系统的缩放面板
        webSettings.setDisplayZoomControls(false);//设置是否系统的缩放面板

        mViewClient = new CustomWebClient(mProgressBar);
        mClient = new CustomChromeWebClient(mProgressBar);
        mWebView.setWebViewClient(mViewClient);
        mWebView.setWebChromeClient(mClient);
    }

    /**
     * 是否又上一级可以返回
     *
     * @return
     */
    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    /**
     * 激活
     */
    public void onResume() {
        mWebView.onResume();
    }

    /**
     * 暂停
     */
    public void onPause() {
        mWebView.onPause();
    }

    /**
     * 返回上一页
     */
    public void goBack() {
        mWebView.goBack();
    }

    /**
     * 加载网址 通过传入String的方式
     *
     * @param url 网址
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    /**
     * 加载网址 通过传入URL的方式
     *
     * @param url
     */
    public void loadUrl(URL url) {
        mWebView.loadUrl(url.toString());
    }

    /**
     * 设置拦截器
     * 拦截器是在子线程中做操作的,如果想要操纵UI需要自己去实现
     *
     * @param customWebViewIntercept 拦截器
     */
    public void setCustomWebViewIntercept(CustomWebViewIntercept customWebViewIntercept) {
        mCustomWebViewIntercept = customWebViewIntercept;
    }

    /**
     * 获取当前控件
     *
     * @return
     */
    public ViewGroup getGroup() {
        return mGroup;
    }

    private class CustomWebClient extends WebViewClient {
        private long currTime;
        private ProgressBar mProgressBar;

        public CustomWebClient(ProgressBar progressBar) {
            mProgressBar = progressBar;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (System.currentTimeMillis() - currTime > 1000) {
                mProgressBar.setVisibility(View.VISIBLE);
                currTime = System.currentTimeMillis();
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (mCustomWebViewIntercept != null) {
                if (mCustomWebViewIntercept.onIntercept(url)) {

                } else {
                    view.loadUrl(url);
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        ///网页里面所有会用到的链接
        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            requestFinish();
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            requestFinish();
            super.onReceivedError(view, request, error);
        }

        private void requestFinish() {
            if (mProgressBar.isShown()) {
                mProgressBar.setProgress(0);
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    private class CustomChromeWebClient extends WebChromeClient {
        private ProgressBar mProgressBar;

        public CustomChromeWebClient(ProgressBar progressBar) {
            mProgressBar = progressBar;
        }


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {

                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 自定义WebView的拦截器
     */
    public interface CustomWebViewIntercept {
        /**
         * 拦截回调
         *
         * @param url 即将加载网页地址
         * @return True执行拦截操作, False则继续加载网页.
         */
        boolean onIntercept(String url);
    }
}

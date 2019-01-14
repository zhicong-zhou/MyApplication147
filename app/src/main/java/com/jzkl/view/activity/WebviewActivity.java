package com.jzkl.view.activity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.ToastUtil;

import butterknife.BindView;

/*
 * 合作单位
 * */
public class WebviewActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;

    String webviewTitle, webviewUrl;
    String cy_url = "http://h52.uniallpay.com/pages/homepage/applyandsignin.html?appuuid=12546f7e70df4963966db5aafffbe83c&bankId=5&bankCardId=300031&authorization=719070_745f6731ce534c7c9280f1b23af3d7fc&authflag=authflag";
    protected ImmersionBar mImmersionBar;
    String mobile;
    CustomDialog customDialog;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData() {
        webviewTitle = getIntent().getStringExtra("webviewTitle");
        webviewUrl = getIntent().getStringExtra("webviewUrl");

        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        customDialog = new CustomDialog(WebviewActivity.this,R.style.CustomDialog);
        customDialog.show();

        if (webviewUrl != null) {
            if(webviewTitle!=null){
                commonTitle.setText("易宝支付");
//                String ybPay =  "https://shouyin.yeepay.com/nc-cashier-wap/wap/request/10000420859/*IGaLVWnMaqdOjOh3v5ohw%3D%3D";
                openWeb(webviewUrl);
            }else {
                commonTitle.setText(webviewTitle);
                openWeb(webviewUrl);
            }
        } else {
            if (webviewTitle.equals("轮播")) {
                String bannUrl = getIntent().getStringExtra("bannUrl");
                openWeb(bannUrl);
            }
        }
    }

    private void openWeb(String webUrl) {
        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.requestFocus();
        //点击超链接的时候重新在原来的进程上加载URL
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                customDialog.dismiss();
                if(url.indexOf("result?token=") != -1){
                    ToastUtil.show("交易成功");
                    finish();
                }
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                customDialog.dismiss();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                customDialog.dismiss();
            }
        });
        webview.loadUrl(webUrl);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*====================不写下面的  退出webview后视频声音还在======================*/
    @Override
    protected void onResume() {
        super.onResume();
        if (webview != null) {
            webview.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webview != null) {
            webview.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.destroy();
        }
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
//    /*用户信息*/
//    public void getUserInfo() {
//        userinfo = new SharedPreferencesUtil().getUserInfo(this);
//        try {
//            JSONObject json = new JSONObject(userinfo);
//            JSONObject user = json.getJSONObject("user");
//            userId = user.getString("userId");
//            name = user.getString("name");
//            mobile = user.getString("mobile");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}

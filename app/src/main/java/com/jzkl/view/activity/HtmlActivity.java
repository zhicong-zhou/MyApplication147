package com.jzkl.view.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class HtmlActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.pay_html)
    WebView webView;
    String htmlPay,orderNum;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_html;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
    }

    @OnClick({R.id.common_back})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                payBack();
                break;
        }
    }
    /*成功 或 失败 返回不同页面*/
    private void payBack() {
        Map<String,String> map = new HashMap<>();
        map.put("orderNum",orderNum);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.shop_order_code)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    Intent intent = new Intent(HtmlActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else if(code == 500){
                                    finish();
                                }else {
                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    @Override
    protected void initData() {
        commonTitle.setText("杉德线上支付");
        String mid = getIntent().getStringExtra("mid");
        String userId = getIntent().getStringExtra("userId");
        String sesionId = getIntent().getStringExtra("sesionId");
        orderNum = getIntent().getStringExtra("orderNum");

        htmlPay = "https://cashier.sandpay.com.cn/fastPay/quickPay/showBindAndPay?sid=" + sesionId +"&mid="+ mid +"&userId="+userId;
//        String shopDetail = htmlPay.replaceAll("/static","https://cashier.sandpay.com.cn/static");
        openWeb();
    }

    private void openWeb() {
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        /*加上button就能用了*/
        webSettings.setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.requestFocus();
        //点击超链接的时候重新在原来的进程上加载URL
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
//        webView.loadDataWithBaseURL(null, shopDetail, "text/html", "utf-8", null);
        webView.loadUrl(htmlPay);
    }
}

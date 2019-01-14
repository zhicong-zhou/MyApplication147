package com.jzkl.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.app.ApplicationDB;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.util.newinfo.ActivityCollector;
import com.jzkl.view.jpush.ExampleUtil;
import com.jzkl.view.jpush.LocalBroadcastManager;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements TextWatcher{

//    @BindView(R.id.common_back)
//    TextView commonBack;
//    @BindView(R.id.common_title)
//    TextView commonTitle;
//    @BindView(R.id.common_img)
//    ImageView commonImg;
    @BindView(R.id.login_tel)
    EditText loginTel;
    @BindView(R.id.login_pwd)
    EditText loginPwd;
    @BindView(R.id.login_but)
    Button loginBut;
    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_forget)
    TextView loginForget;

    Intent intent;
    CustomDialog customDialog;
    String uName, uPwd,userId;
    private long exitTime = 0;
    protected ImmersionBar mImmersionBar;
    public static boolean isForeground = false;

    @Override
    protected void initView() {

    }

    @OnClick({R.id.login_but, R.id.login_register, R.id.login_forget})
    public void Onclick(View view) {
        switch (view.getId()) {
            /*登录*/
            case R.id.login_but:
                uName = loginTel.getText().toString().trim();
                uPwd = loginPwd.getText().toString().trim();
                if (uName.equals("")) {
                    ToastUtil.show("手机号不能为空");
                    return;
                } else if (uName.length() != 11) {
                    ToastUtil.show("手机号不合法");
                    return;
                } else if (uPwd.equals("")) {
                    ToastUtil.show("密码不能为空");
                    return;
                }
                getLogin();

                break;
            /*立即注册*/
            case R.id.login_register:
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("register", "0");
                startActivity(intent);
                break;
            /*忘记密码*/
            case R.id.login_forget:
                intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("register", "1");
                startActivity(intent);
                break;
        }
    }

    // ========================================初始化 语音合成对象
    private void init() {
        JPushInterface.init(getApplicationContext());
        // 调用 Handler 来异步设置别名
        //上下文、别名【Sting行】、标签【Set型】、回调
        if(userId!=null && !userId.equals("")){
            JPushInterface.setAlias(this,Integer.parseInt(userId), userId +"_");
//            //查询
//            JPushInterface.getAlias(this, 0);
        }
    }

    @Override
    public void onPause() {
        isForeground = false;
        super.onPause();
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.jzkl.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        String deviceId = ExampleUtil.getDeviceId(this.getApplicationContext());
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");

                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    /*================极光结束===============*/

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login2;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @Override
    protected void initData() {
//        commonTitle.setText("登录");
//        commonBack.setVisibility(View.GONE);

        loginTel.addTextChangedListener(this);
        loginPwd.addTextChangedListener(this);
    }

    private void getLogin() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        final Map<String, String> map = new HashMap<>();
        map.put("mobile", uName);
        map.put("password", uPwd);
        /*转化*/
//        String jsonObject1 = "{ \"mobile\":\"18435793423\",\"password\":\"1234567890\"}";
        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.login)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            customDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    String token = jsonObject.getString("token");
                                    String currentCredit = jsonObject.getString("currentCredit");
                                    userId = jsonObject.getString("userId");

                                    String userInfo = "{\"mobile\":" + uName + ",\"password\":" + uPwd + ",\"token\":" + token + "}";
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setToken(LoginActivity.this, userInfo);
                                    intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("loginFirst", currentCredit);
                                    startActivity(intent);
                                    finish();

                                    /*极光*/
                                    init();
                                    registerMessageReceiver();  // used for receive msg
                                } else {
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
                        customDialog.dismiss();
                        ToastUtil.show("" + e);
                    }
                });
    }

    /*退出*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - exitTime < 2000) {
                ActivityCollector.exitApp();
            } else {
                Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (loginTel.getText().toString().trim().length() == 11&&loginPwd.getText().toString().trim().length() >= 1) {//判断账号是否11位
            loginBut.setBackground(getResources().getDrawable(R.drawable.shape_button));
            loginBut.setTextColor(Color.parseColor("#ffffff"));
        } else {
            loginBut.setBackground(getResources().getDrawable(R.drawable.shape_button_gray));
            loginBut.setTextColor(Color.parseColor("#737679"));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*注销极光*/
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

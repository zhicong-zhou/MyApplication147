package com.jzkl.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.jpush.ExampleUtil;
import com.jzkl.view.jpush.LocalBroadcastManager;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 欢迎页
 *
 * */
public class WelcomeActivity extends BaseActivity {

    String userinfo,loginStatus,token,mobile,password,userId,currentCredit;
    private boolean isFirstIn;
    public static boolean isForeground = false;
    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initData() {

        /*接受的另一个APP 单独的不用*/
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String name = bundle.getString("name");
            String birthday = bundle.getString("birthday");
            if (name != null && birthday != null) {
                Toast.makeText(getApplicationContext(), "name:" + name + "    birthday:" + birthday, Toast.LENGTH_SHORT).show();
            }
        }

        getUser();
        SharedPreferences sp = getSharedPreferences("zzc", MODE_PRIVATE);
        isFirstIn = sp.getBoolean("isFirstIn", true);
        if (isFirstIn) {
            new Handler().postDelayed(myRunnable, 1000);
        } else {
            new Handler().postDelayed(myRunnable2, 1000);
        }
        /*极光*/
        init();
        registerMessageReceiver();  // used for receive msg
    }

    /*登录*/
    private void getLogin() {
        final Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", password);
        /*转化*/
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.login)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    String token = jsonObject.getString("token");
                                    currentCredit = jsonObject.getString("currentCredit");

                                    String userInfo = "{\"mobile\":" + mobile + ",\"password\":" + password + ",\"token\":" + token + "}";
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setToken(WelcomeActivity.this, userInfo);
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
                        ToastUtil.show(""+e);
                    }
                });
    }

    private final Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            //执行我们的业务逻辑
            goGuide();
        }
    };
    private final Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {
            //执行我们的业务逻辑
            goHome();
        }
    };

    // ========================================初始化 语音合成对象
    private void init() {
        JPushInterface.init(getApplicationContext());
        // 调用 Handler 来异步设置别名
        //上下文、别名【Sting行】、标签【Set型】、回调
        if(userId!=null && !userId.equals("")){
            JPushInterface.setAlias(this, Integer.parseInt(userId), userId+"_");
//            //查询
//            JPushInterface.getAlias(this, 0);
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
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
//        String deviceId = ExampleUtil.getDeviceId(getApplicationContext());
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

    private void goGuide() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goHome() {
        loginStatus = new SharedPreferencesUtil().getLogin(this);
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            mobile = json.getString("mobile");
            password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(mobile==null){
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            if (!loginStatus.equals("") ) {
                /*0打开   1关闭   come 0  是首页进入  1是设置进入*/
                if (loginStatus.toString().equals("0")) {
                    Intent intent = new Intent(WelcomeActivity.this, LoginShouActivity.class);
                    intent.putExtra("come", "0");
                    startActivity(intent);
                    finish();
                } else if (loginStatus.equals("1")) {
                    /*如果要三秒后在进入就是现在   不要就换成  SplashActivity*/
                    Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                    intent.putExtra("loginFirst", currentCredit);
                    startActivity(intent);
                    finish();
                }
            }else {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                intent.putExtra("loginFirst", currentCredit);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*注销极光*/
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            mobile = json.getString("mobile");
            token = json.getString("token");
            password = json.getString("password");
            getUserInfo();
            getLogin();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*用户信息*/
    private void getUserInfo() {
        OkHttpUtils.post(Webcon.url + Webcon.userInfo)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    JSONObject user = jsonObject.getJSONObject("user");
                                    userId = user.getString("userId");
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setUserInfo(WelcomeActivity.this, s);
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
                    }
                });
    }

}

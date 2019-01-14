package com.jzkl.view.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.adapter.CarListAdapter;
import com.jzkl.util.AppUtils;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.SlideButton;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.set_speech)
    RelativeLayout setRenz;
//    @BindView(R.id.set_up_tel)
//    RelativeLayout setUpTel;
    @BindView(R.id.set_up_pwd)
    RelativeLayout setUpPwd;

    @BindView(R.id.set_speech_open)
    SlideButton speech_open;
    @BindView(R.id.set_sslogin_open)
    SlideButton login_open;
    @BindView(R.id.set_up_zhiyin)
    RelativeLayout zhiyin;
    @BindView(R.id.version_code)
    TextView versionCode;

    @BindView(R.id.set_exit)
    Button setExit;


    private String serviceTitle;
    Intent intent;
    String loginPwd;
    ttsOnCheckListener listener;
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String userinfo,token;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @OnClick({R.id.common_back, R.id.set_up_pwd,R.id.set_exit,R.id.set_up_zhiyin})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
//            /*修改手机号*/
//            case R.id.set_up_tel:
//                intent = new Intent(this, SetUPTelActivity.class);
//                startActivity(intent);
//                break;
            /*修改密码*/
            case R.id.set_up_pwd:
                intent = new Intent(this, SetUPPwdActivity.class);
                startActivity(intent);
                break;
                /*退出*/
            case R.id.set_exit:
                LoginExit();
                break;
                /*新手指引*/
            case R.id.set_up_zhiyin:
                intent = new Intent(this,SetNewGuideActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_set;
    }

    @Override
    protected void initData() {
        serviceTitle = getIntent().getStringExtra("serviceTitle");
        commonTitle.setText(serviceTitle);

        getUser();
        /*版本号*/
        String version = AppUtils.getVersionName(this);
        versionCode.setText(version);
        /*获取开关状态*/
        String loginStatus = new SharedPreferencesUtil().getLogin(this);
        if(loginStatus!=null){
            if(loginStatus.equals("0")){
                login_open.setOpen(true);
            }else if(loginStatus.equals("1")){
                login_open.setOpen(false);
            }
        }

        /*获取语音开关状态*/
        String ttsStatus = new SharedPreferencesUtil().getTTs(this);
        if(ttsStatus!=null){
            if(ttsStatus.equals("0")){
                speech_open.setOpen(true);
            }else if(loginStatus.equals("1")){
                speech_open.setOpen(false);
            }
        }

        /*获取登录手势密码*/
        loginPwd = new SharedPreferencesUtil().getLoginPwd(this);
        /*语音开关*/
        speech_open.setOnSlideButtonChangeListener(new SlideButton.OnSlideButtonChangeListener() {
            @Override
            public void onButtonChange(SlideButton view, boolean isOpen) {
                if(isOpen){
                    new SharedPreferencesUtil().setTTs(SettingActivity.this,"0");
                    EventBus.getDefault().post(new EventsWIFI("0"));
//                    listener.onCheck("0");
                }else {
                    /*保存用户信息*/
                    new SharedPreferencesUtil().setTTs(SettingActivity.this,"1");
                    EventBus.getDefault().post(new EventsWIFI("1"));
                }
            }
        });
        /*手势登录*/
        login_open.setOnSlideButtonChangeListener(new SlideButton.OnSlideButtonChangeListener() {
            @Override
            public void onButtonChange(SlideButton view, boolean isOpen) {
                if(isOpen){
                    /*保存用户信息*/
                    if(loginPwd.equals("")){
                        new SharedPreferencesUtil().setLogin(SettingActivity.this,"0");
                        Intent intent = new Intent(SettingActivity.this,LoginShouActivity.class);
                        intent.putExtra("come","1");
                        startActivity(intent);
                    }
                }else {
                    /*保存用户信息*/
                    new SharedPreferencesUtil().setLogin(SettingActivity.this,"1");
                }
            }
        });
    }

    public interface  ttsOnCheckListener{
        void onCheck(String tts);
    }

    private void LoginExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("退出");
        builder.setMessage("确定退出？");

        builder.setPositiveButton("确定", new DatePickerDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                getEixt();
                SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                sp.edit().clear().commit();
                /*清空任务栈*/
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //    显示出该对话框
        final AlertDialog diolog = builder.show();
        /*设置字体颜色*/
        diolog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6DB0FF"));
        diolog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#6DB0FF"));



    }
    /*退出 不用了*/
    private void getEixt() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        OkHttpUtils.post(Webcon.url + Webcon.logout)
                .headers("token",token)
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
                                    SharedPreferences sp = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                                    sp.edit().clear().commit();
                                    /*清空任务栈*/
                                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                  intent.putExtra("setEixt","yes");
                                    startActivity(intent);
                                    finish();
                                    ToastUtil.show("退出成功");

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
                        ToastUtil.show(""+e);
                    }
                });
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            String mobile = json.getString("mobile");
            String password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

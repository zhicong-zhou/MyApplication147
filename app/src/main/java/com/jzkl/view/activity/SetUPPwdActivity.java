package com.jzkl.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyCountDownTimer;
import com.jzkl.util.MyCountDownTimerCode;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SetUPPwdActivity extends BaseActivity implements TextWatcher{


    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.set_upPwd)
    TextView setUpPwd;
    @BindView(R.id.set_upPwd_new)
    EditText setUpPwdNew;
    @BindView(R.id.up_sure_pwdtxt)
    TextView upSurePwdtxt;
    @BindView(R.id.set_upPwd_code)
    EditText UpCode;
    @BindView(R.id.set_up_codeBut)
    Button setUpCodeBut;

    @BindView(R.id.set_upPwd_but)
    Button setUpPwdBut;

    String userinfo,token,mobile,newPwd,newCode,smscode, smscodeEncrpt;

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

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_set_up_pwd;
    }

    @Override
    protected void initData() {
        commonTitle.setText("修改密码");
        getUser();
        setUpPwdNew.addTextChangedListener(this);
        UpCode.addTextChangedListener(this);
    }

    @OnClick({R.id.common_back,R.id.set_up_codeBut, R.id.set_upPwd_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
                /*获取验证码*/
            case R.id.set_up_codeBut:
                getCode();
                break;
            /*修改*/
            case R.id.set_upPwd_but:
                newPwd = setUpPwdNew.getText().toString().trim();
                newCode = UpCode.getText().toString().trim();
                if(newPwd.equals("")){
                    ToastUtil.show("密码不能为空");
                    return;
                }else if(newCode.equals("")){
                    ToastUtil.show("验证码不能为空");
                    return;
                }else if(!newCode.equals(smscode)){
                    ToastUtil.show("验证码不正确");
                    return;
                }
                getUpdate();
                break;
        }
    }

    /*获取验证码*/
    private void getCode() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        final Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        /*转化*/
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.telCode)
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
                                    new MyCountDownTimerCode(60000, 1000, setUpCodeBut).start();
                                    smscode = jsonObject.getString("smscode");
                                    /*验证码密文*/
                                    smscodeEncrpt = jsonObject.getString("smscodeEncrpt");
                                    ToastUtil.show("验证码获取成功");
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

    /*修改密码*/
    private void getUpdate() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        final Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", newPwd);
        map.put("smscode", newCode);
        map.put("smscodeEncrpt", smscodeEncrpt);

        /*转化*/
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.pwdUpdate)
                .headers("token",token)
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
                                    ToastUtil.show("修改成功");
                                    /*直接登录*/
                                    getLogin(mobile, newPwd);
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
                    }
                });
    }

    private void getLogin(final String uName, final String uPwd) {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        final Map<String, String> map = new HashMap<>();
        map.put("mobile", uName);
        map.put("password", uPwd);
        /*转化*/
        String json = new Gson().toJson(map);
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

                                    String userInfo = "{\"mobile\":" + uName + ",\"password\":" + uPwd + ",\"token\":" + token + "}";
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setToken(SetUPPwdActivity.this, userInfo);
                                    finish();
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
                    }
                });
    }


    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            mobile = json.getString("mobile");
            String password = json.getString("password");

            setUpPwd.setText(mobile);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (setUpPwdNew.getText().toString().trim().length() >= 1&&UpCode.getText().toString().trim().length() >= 1) {//判断账号是否11位
            setUpPwdBut.setBackground(getResources().getDrawable(R.drawable.shape_button));
            setUpPwdBut.setTextColor(Color.parseColor("#ffffff"));
        } else {
            setUpPwdBut.setBackground(getResources().getDrawable(R.drawable.shape_button_gray));
            setUpPwdBut.setTextColor(Color.parseColor("#737679"));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

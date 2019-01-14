package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyCountDownTimer;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity implements TextWatcher{

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.register_tel)
    EditText registerTel;
    @BindView(R.id.register_code_but)
    Button registerCodeBut;
    @BindView(R.id.register_code)
    EditText registerCode;
    @BindView(R.id.register_username)
    EditText registerName;
    @BindView(R.id.register_pwd_eid)
    EditText register_pwd_eid;

    @BindView(R.id.register_tjcode_ll)
    LinearLayout tjcode_ll;
    @BindView(R.id.register_tjcode)
    EditText tjcode;

    @BindView(R.id.register_but)
    Button registerBut;
    @BindView(R.id.register_pwd_ll)
    LinearLayout registerPwdLl;

    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String url,register, tel, code, pwd,rName,referralCode,smscode, smscodeEncrpt;

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

    @OnClick({R.id.common_back, R.id.register_code_but, R.id.register_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*验证码*/
            case R.id.register_code_but:
                tel = registerTel.getText().toString().trim();
                if (tel.equals("")) {
                    ToastUtil.show("手机号不能为空");
                    return;
                } else if (tel.length() != 11) {
                    ToastUtil.show("手机号不合法");
                    return;
                }
                getCode();
                break;
            /*确定*/
            case R.id.register_but:
                tel = registerTel.getText().toString().trim();
                code = registerCode.getText().toString().trim();
                pwd = register_pwd_eid.getText().toString().trim();
                rName = registerName.getText().toString().trim();
                referralCode = tjcode.getText().toString().trim();
                if (tel.equals("")) {
                    ToastUtil.show("手机号不能为空");
                    return;
                } else if (tel.length() != 11) {
                    ToastUtil.show("手机号不合法");
                    return;
                }else if (code.equals("")) {
                    ToastUtil.show("验证码不为空");
                    return;
                }
//                else if (!code.equals(smscode)) {
//                    ToastUtil.show("验证码不正确");
//                    return;
//                }
                else if (pwd.equals("")) {
                    ToastUtil.show("密码不能为空");
                    return;
                }
                if(register.equals("0")){
                    if (rName.equals("")) {
                        ToastUtil.show("用户名不能为空");
                        return;
                    }
                }
                getRigester();
                break;
        }
    }

    /*获取验证码*/
    private void getCode() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        final Map<String, String> map = new HashMap<>();
        map.put("mobile", tel);
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
                                    new MyCountDownTimer(60000, 1000, registerCodeBut).start();
                                    smscode = jsonObject.getString("smscode");
//                                    registerCode.setText(smscode);
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
                    }
                });
    }

    /*注册*/
    private void getRigester() {
        Map<String, String> map = new HashMap<>();
        if(register.equals("0")){
            map.put("loeadrMobile", "");
            map.put("mobile", tel);
            map.put("password", pwd);
            map.put("smscode", code);
            map.put("smscodeEncrpt", smscodeEncrpt);
            map.put("username", rName);
            map.put("referralCode", referralCode);
            url = Webcon.url + Webcon.register;
        }else if(register.equals("1")){
            map.put("mobile", tel);
            map.put("password", pwd);
            map.put("smscode", code);
            map.put("smscodeEncrpt", smscodeEncrpt);
            url = Webcon.url + Webcon.pwdUpdate;
        }
        /*转化*/
        final String json = new Gson().toJson(map);
        OkHttpUtils.post(url)
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
                                    /*直接登录*/
                                    getLogin(tel,pwd);
                                    /*忘记密码  把开关状态改了  手势密码设成空*/
                                    new SharedPreferencesUtil().setLogin(RegisterActivity.this,"1");
                                    new SharedPreferencesUtil().setLoginPwd(RegisterActivity.this, String.valueOf(""));
                                } else if (code == 500){
                                    ToastUtil.show(msg);
                                }else if(code == 501){
                                    popWin(msg);
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

    private void popWin(String msg) {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_intergter, null);
        TextView dialogText = dialogView.findViewById(R.id.dialog_text);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
//        dialogContent.setText(msg);
        dialogBtnConfirm.setText("确定");
        dialogBtnCancel.setText("取消");
        /*获取屏幕宽高*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;
        layoutDialog.setView(dialogView);
        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*确定*/
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tjcode_ll.setVisibility(View.VISIBLE);
            }
        });
        /*取消*/
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                tjcode_ll.setVisibility(View.GONE);
            }
        });

        final String kfTel = msg.substring(msg.indexOf("服")+1,msg.indexOf("获"));
        SpannableStringBuilder style = new SpannableStringBuilder(msg);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#2D77FF")), msg.indexOf("服")+1, msg.indexOf("获"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialogContent.setText(style);

        dialogContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndPermission.with(RegisterActivity.this).permission(Manifest.permission.CALL_PHONE).callback(new PermissionListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:"+ kfTel);
                        intent.setData(data);
                        startActivity(intent);
//                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Toast.makeText(RegisterActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();
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
                                    new SharedPreferencesUtil().setToken(RegisterActivity.this, userInfo);
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
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
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void initData() {
        register = getIntent().getStringExtra("register");
        if (register.equals("0")) {
            commonTitle.setText("注册");
            registerBut.setText("注册成功");
            registerName.setVisibility(View.VISIBLE);
        } else if (register.equals("1")) {
            commonTitle.setText("找回密码");
            registerBut.setText("保存修改");
            registerName.setVisibility(View.GONE);
        }

        registerTel.addTextChangedListener(this);
        registerCode.addTextChangedListener(this);
        register_pwd_eid.addTextChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (registerTel.getText().toString().trim().length() == 11&&!registerCode.getText().toString().trim().equals("")&&register_pwd_eid.getText().toString().trim().length() >= 1) {//判断账号是否11位
            registerBut.setBackground(getResources().getDrawable(R.drawable.shape_button));
            registerBut.setTextColor(Color.parseColor("#ffffff"));
        } else {
            registerBut.setBackground(getResources().getDrawable(R.drawable.shape_button_gray));
            registerBut.setTextColor(Color.parseColor("#737679"));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

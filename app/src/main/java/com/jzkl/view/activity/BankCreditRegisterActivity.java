package com.jzkl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.RegularUtil;
import com.jzkl.util.SharedPreferencesUtil;
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

/*
 * 信用卡申请
 * */
public class BankCreditRegisterActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.credit_register_name)
    EditText creditName;
    @BindView(R.id.credit_register_tel)
    EditText creditTel;
    @BindView(R.id.credit_register_iden)
    EditText creditIden;
    @BindView(R.id.credit_register_but)
    Button creditRegisterBut;

    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String cName,cTel,cIden;
    String userinfo,token;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bank_credit_register;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
    }

    @OnClick({R.id.common_back,R.id.credit_register_but})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*确认*/
            case R.id.credit_register_but:
                cName = creditName.getText().toString().trim();
                cTel = creditTel.getText().toString().trim();
                cIden = creditIden.getText().toString().trim();
                if(cName.equals("")){
                    ToastUtil.show("姓名不能为空");
                    return;
                }else if(cTel.equals("")){
                    ToastUtil.show("手机号不能为空");
                    return;
                }else if(cTel.length()!=11){
                    ToastUtil.show("手机号不合法");
                    return;
                }else if(!RegularUtil.isIDacard(cIden)){
                    ToastUtil.show("身份证号不合法");
                    return;
                }
                subCredit();
                break;
        }
    }
    /*申请*/
    private void subCredit() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("idno",cIden);
        map.put("mobile",cTel);
        map.put("name",cName);

        final String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.home_xyk_register)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    ToastUtil.show("申请成功");
                                    Intent intent = new Intent(BankCreditRegisterActivity.this, BankListCreditActivity.class);
                                    startActivity(intent);
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
                        customDialog.dismiss();
                        ToastUtil.show(e+"");
                    }
                });
    }

    @Override
    protected void initData() {
        commonTitle.setText("信用卡申请");
        getUser();
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        if(userinfo!=""){
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

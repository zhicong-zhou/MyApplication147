package com.jzkl.view.activity;

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
import com.jzkl.util.Arith;
import com.jzkl.util.CustomDialog;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 提现
 * */
public class CashActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.cash_name)
    TextView cashName;
    @BindView(R.id.cash_card)
    EditText cashCard;
    @BindView(R.id.cash_eid)
    EditText cashEid;
    @BindView(R.id.cash_jine)
    TextView cashJine;
    @BindView(R.id.cash_but)
    Button cashBut;

    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String userinfo,token,mCard,cashMoney,balance;

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

    @OnClick({R.id.common_back, R.id.cash_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*确定*/
            case R.id.cash_but:
                mCard = cashCard.getText().toString().trim();
                cashMoney = cashEid.getText().toString().trim();
                if(mCard.equals("")){
                    ToastUtil.show("银行卡号不能为空");
                    return;
                }else if(cashMoney.equals("")){
                    ToastUtil.show("金额不能为空");
                    return;
                }else if(Arith.sub(Double.parseDouble(cashMoney),Double.parseDouble(balance))>0.0){
                    ToastUtil.show("提现金额不能超出余额");
                    return;
                }
                subCash();
                break;
        }
    }
    /*提现*/
    private void subCash() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        Map<String,String> map = new HashMap<>();
        map.put("bankcard",mCard);
        map.put("amount",cashMoney);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.wallet_cash)
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
                                    ToastUtil.show(msg);
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
                        customDialog.dismiss();
                        ToastUtil.show(""+e);
                    }
                });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cash;
    }

    @Override
    protected void initData() {
        commonTitle.setText("提现");
        getUser();
        /*用去用户信息*/
        getUserInfo();
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                token = json.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUserInfo() {
        userinfo = new SharedPreferencesUtil().getUserInfo(this);
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                balance = json.getString("balance");
                JSONObject user = json.getJSONObject("user");
                String name = user.getString("name");
                String moble = user.getString("mobile");
                cashJine.setText("可用余额"+balance+"元");
                cashName.setText(moble);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

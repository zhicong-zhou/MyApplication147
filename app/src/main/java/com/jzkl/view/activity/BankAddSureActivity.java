package com.jzkl.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.PasswordEditText;
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
 *
 * 绑定确认
 * */
public class BankAddSureActivity extends BaseActivity {

    protected ImmersionBar mImmersionBar;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.bank_add_surell)
    LinearLayout bankAddSurell;
    @BindView(R.id.bank_add_sure_edi)
    PasswordEditText bankAddSureEdi;
    @BindView(R.id.bank_add_sure_but)
    Button bankAddSureBut;
    @BindView(R.id.bank_add_edill)
    LinearLayout bankAddEdill;

    @BindView(R.id.bank_add_sure_time)
    Button bank_time;
    @BindView(R.id.bank_add_sure_new)
    TextView bank_sure_new;


    String sureEdi,bankPay,mCard,url,sureUrl,orderid;
    String userinfo,token;
    int mCount = 59;
    CountDownTimer countDownTimer;
    CustomDialog customDialog;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bank_add_sure;
    }

    @OnClick({R.id.common_back,R.id.bank_add_sure_but,R.id.bank_add_sure_new})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                /*关闭计时*/
                break;
            case R.id.bank_add_sure_but:
                sureEdi = bankAddSureEdi.getText().toString().trim();
                if(sureEdi.equals("")){
                    ToastUtil.show("验证码不能为空");
                    return;
                }
                subSureBank();
                break;
                /*重新获取验证码*/
            case R.id.bank_add_sure_new:
                String ss = bank_time.getText().toString().trim();
                if(ss.equals("0")){
                    getYanCode();
                    countDownTimer.start();
                    bank_sure_new.setTextColor(Color.parseColor("#333333"));
                }
                break;
        }
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
        commonTitle.setText("确认刷卡");
        /*bankPay 0 是刷卡过来的  调验证码  1 是添加过来的*/
        bankPay = getIntent().getStringExtra("bankPay");
        mCard = getIntent().getStringExtra("mCard");

        /*****************计时器*******************/
        countDownTimer = new CountDownTimer(mCount*1000 + 1050 , 1000) {
            @Override
            public void onTick(long l) {
                if(bank_time!=null){
                    bank_time.setText(((int)l/1000)+"");
                }
            }
            @Override
            public void onFinish() {
                Log.d("CJT", "onFinish-------");
                if(bank_time!=null && bank_sure_new !=null){
                    bank_time.setText("0");
                    bank_sure_new.setTextColor(Color.parseColor("#6DB0FF"));
                }
                countDownTimer.cancel();
            }
        };
        countDownTimer.start();
        getUser();
    }

    private void getYanCode() {
        Map<String,String> map = new HashMap<>();

        if(mCard==null){
            /*刷卡验证*/
            map.put("orderid",bankPay);
            url = Webcon.url2 + Webcon.bank_binding_paycode;
        }else{
            /*绑定验证*/
            map.put("bankno",mCard);
            url = Webcon.url2 + Webcon.bank_binding_yancode;
        }
        String json = new Gson().toJson(map);

        OkHttpUtils.post(url)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if(code == 0){

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
                        ToastUtil.show(e+"");
                    }
                });
    }

    private void subSureBank() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        if(mCard ==null){
            map.put("orderid",bankPay);
            map.put("smscode",sureEdi);
            /*刷卡确认支付*/
            sureUrl= Webcon.url2 + Webcon.bank_binding_paypayConfirm;
        }else if(mCard!=null){
            /*绑卡确认*/
            map.put("bankno",mCard);
            map.put("smscode",sureEdi);
            sureUrl= Webcon.url2 + Webcon.bank_binding_Confirm;
        }
        String json = new Gson().toJson(map);

        OkHttpUtils.post(sureUrl)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                customDialog.dismiss();
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if(code == 0){
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.showSoftInput(bankAddSureEdi,InputMethodManager.SHOW_FORCED);
                                        imm.hideSoftInputFromWindow(bankAddSureEdi.getWindowToken(), 0); //强制隐藏键盘
                                    }
                                    if(mCard ==null){
                                        finish();
                                        ToastUtil.show("刷卡完成");
                                    }else if(mCard !=null){
                                        Intent intent = new Intent(BankAddSureActivity.this, BankListActivity.class);
                                        startActivity(intent);
                                        finish();
                                        ToastUtil.show("绑定成功");
                                    }
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
                        ToastUtil.show(e+"");
                        customDialog.dismiss();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
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

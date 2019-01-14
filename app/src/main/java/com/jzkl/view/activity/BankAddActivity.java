package com.jzkl.view.activity;

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
* 添加银行卡
* */
public class BankAddActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.bank_add_card)
    EditText bankAddCard;
    @BindView(R.id.bank_add_time)
    EditText bankAddTime;
    @BindView(R.id.bank_add_cvv2)
    EditText bankAddCvv2;
    @BindView(R.id.bank_add_tel)
    EditText bankAddTel;
    @BindView(R.id.bank_add_but)
    Button bankAddBut;

    protected ImmersionBar mImmersionBar;
    String mCard,mTime,mCvv,mTel;
    CustomDialog customDialog;
    String userinfo,token;

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
    protected void initView() {

    }

    @OnClick({R.id.common_back,R.id.bank_add_but})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*确定*/
            case R.id.bank_add_but:
                mCard = bankAddCard.getText().toString().trim();
                mTime = bankAddTime.getText().toString().trim();
                mCvv = bankAddCvv2.getText().toString().trim();
                mTel = bankAddTel.getText().toString().trim();
                if(mCard.equals("")){
                    ToastUtil.show("银行卡号不能为空");
                    return;
                }else if(mTel.equals("")){
                    ToastUtil.show("手机号不能为空");
                    return;
                }
                addBank();
                break;
        }
    }

    /*添加银行卡*/
    private void addBank() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("bankno",mCard);
        map.put("cvv2",mCvv);
        map.put("mobile",mTel);
        map.put("validdate",mTime);

        final String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url2 + Webcon.bank_bindingXf)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if(code==0){
//                                    Intent intent = new Intent(BankAddActivity.this,BankAddSureActivity.class);
//                                    intent.putExtra("bankPay","1");
//                                    intent.putExtra("mCard",mCard);
//                                    startActivity(intent);
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
                        ToastUtil.show(e+"");
                    }
                });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bank_add;
    }
    @Override
    protected void initData() {
        commonTitle.setText("添加银行卡");
        getUser();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

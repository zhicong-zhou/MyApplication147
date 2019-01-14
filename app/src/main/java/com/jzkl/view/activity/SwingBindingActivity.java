package com.jzkl.view.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 刷卡绑定
 * */
public class SwingBindingActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_text)
    TextView commonText;
    @BindView(R.id.swing_binding_name)
    EditText swingBindingName;
    @BindView(R.id.swing_binding_ident)
    EditText swingBindingIdent;
    @BindView(R.id.swing_binding_tel)
    EditText swingBindingTel;
    @BindView(R.id.swing_binding_card)
    EditText swingBindingCard;
    @BindView(R.id.swing_binding_addr)
    EditText swingBindingAddr;
    @BindView(R.id.swing_binding_but)
    Button swingBindingBut;

    String sName, sIdent, sTel, sCard, sAddr;
    CustomDialog customDialog;
    String userinfo, token;
    protected ImmersionBar mImmersionBar;
    String authId,acctId;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_swing_binding;
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

    @OnClick({R.id.common_back, R.id.swing_binding_but,R.id.common_text})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*确认*/
            case R.id.swing_binding_but:
                sName = swingBindingName.getText().toString().trim();
                sIdent = swingBindingIdent.getText().toString().trim();
                sTel = swingBindingTel.getText().toString().trim();
                sCard = swingBindingCard.getText().toString().trim();
                sAddr = swingBindingAddr.getText().toString().trim();
                if (sName.equals("")) {
                    ToastUtil.show("姓名不能为空");
                    return;
                } else if (sIdent.equals("")) {
                    ToastUtil.show("身份证号不能为空");
                    return;
                } else if (!RegularUtil.isIDacard(sIdent)) {
                    ToastUtil.show("身份证号不合法");
                    return;
                } else if (sTel.equals("")) {
                    ToastUtil.show("手机号不能为空");
                    return;
                } else if (sCard.equals("")) {
                    ToastUtil.show("借记卡号不能为空");
                    return;
                } else if (sCard.length()<16) {
                    ToastUtil.show("借记卡号小于16位不合法");
                    return;
                } else if (sAddr.equals("")) {
                    ToastUtil.show("地址不能为空");
                    return;
                }
                if(swingBindingBut.getText().toString().equals("绑定")){
                    subSwingBinding();
                }else if(swingBindingBut.getText().toString().equals("修改")){
                    upSwingBinding();
                }
                break;
                /*编辑*/
            case R.id.common_text:
                /*只有卡号可以修改*/
                swingBindingCard.setFocusableInTouchMode(true);
                swingBindingCard.requestFocus();
                swingBindingBut.setText("修改");
                break;
        }
    }

    /*刷卡绑定*/
    private void subSwingBinding() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("username", sName);
        map.put("idno", sIdent);
        map.put("bankno", sCard);
        map.put("mobile", sTel);
        map.put("address", sAddr);

        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    ToastUtil.show("绑定成功");
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

    /*查询卡信息*/
    private void getBankBinding() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding_info)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    commonText.setVisibility(View.VISIBLE);
                                    commonText.setText("编辑");

                                    JSONObject auth = jsonObject1.getJSONObject("auth");
                                    authId = auth.getString("authId");
                                    String acctName = auth.getString("acctName");
                                    String idno = auth.getString("idno");
                                    String phone = auth.getString("phone");
                                    acctId = auth.getString("acctId");
                                    String address = auth.getString("address");
                                      /*加星星*/
//                                    String  cardNo  = HideDataUtil.hideCardNo(acctId);
//                                    String  telNo  = HideDataUtil.hidePhoneNo(phone);

                                    swingBindingName.setFocusable(false);
                                    swingBindingIdent.setFocusable(false);
                                    swingBindingTel.setFocusable(false);
                                    swingBindingCard.setFocusable(false);
                                    swingBindingAddr.setFocusable(false);

                                    swingBindingName.setText(acctName);
                                    swingBindingIdent.setText(idno);
                                    swingBindingTel.setText(phone);
                                    swingBindingCard.setText(acctId);
                                    swingBindingAddr.setText(address);

                                } else {
                                    commonText.setVisibility(View.GONE);
                                    commonText.setText("编辑");
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

    /*修改卡信息*/
    private void upSwingBinding(){
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("authid",authId);
        map.put("bankno",sCard);

        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding_upd)
                .headers("token", token)
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
                                if(code == 0){
                                    finish();
                                    ToastUtil.show("修改成功");
                                }else {
                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {

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
    protected void initData() {
        commonTitle.setText("刷卡绑定");
        getUser();
        getBankBinding();
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

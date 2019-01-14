package com.jzkl.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.EventsSwing;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.R;
import com.jzkl.util.Arith;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 刷卡
 * */
public class SwingCardActivity extends BaseActivity implements TextWatcher {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.swing_price_txt)
    TextView swingPriceTxt;
    @BindView(R.id.swing_price_eid)
    EditText swingPriceEid;
    @BindView(R.id.swing_card_txt)
    TextView swingCardTxt;
    @BindView(R.id.swing_card_eid)
    TextView swingCardEid;
    @BindView(R.id.swing_yj)
    TextView swing_yj;
    @BindView(R.id.swing_but)
    TextView swingBut;
    @BindView(R.id.swing_eid)
    LinearLayout swingEid;

    protected ImmersionBar mImmersionBar;
    String price, card, bankCard, bankCardId;
    CustomDialog customDialog;
    String userinfo, token, rate,settfee;

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
    }

    @Override
    protected void initView() {

    }

    @Subscribe
    public void onEvent(EventsSwing event) {
        bankCard = event.getCard();
        bankCardId = event.getCarId();
        swingCardEid.setText(bankCard);
    }

    @OnClick({R.id.common_back, R.id.swing_but, R.id.swing_card_eid})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*确认*/
            case R.id.swing_but:
                price = swingPriceEid.getText().toString().trim();
                card = swingCardEid.getText().toString().trim();
                if (price.equals("")) {
                    ToastUtil.show("金额不能为空");
                    return;
                } else if (card.equals("请选择银行卡")) {
                    ToastUtil.show("请选择银行卡");
                    return;
                }
                subSwing();
                break;
            /*选择银行卡*/
            case R.id.swing_card_eid:
                Intent intent = new Intent(this, BankListActivity.class);
                intent.putExtra("bankSelect", "0");
                startActivity(intent);
                break;
        }
    }

    /*确认刷卡*/
    private void subSwing() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("amount", price);
        map.put("bankcardid", bankCardId);

        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding_pay)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                Log.e("ADAD",jsonObject1.toString());
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    String orderid = jsonObject1.getString("orderid");
                                    String channle = jsonObject1.getString("channle");
                                    if (channle.equals("易宝支付")) {
                                        boolean flag = jsonObject1.has("url");
                                        String ybUrl = null;
                                        if (flag) {
                                            ybUrl = jsonObject1.getString("url");
                                            Intent intent1 = new Intent(SwingCardActivity.this, WebviewActivity.class);
                                            intent1.putExtra("webviewTitle", "易宝支付");
                                            intent1.putExtra("webviewUrl", ybUrl);
                                            startActivity(intent1);
                                        }
                                    }else {
                                        Intent intent = new Intent(SwingCardActivity.this, BankAddSureActivity.class);
                                        intent.putExtra("bankPay", orderid);
                                        startActivity(intent);
                                    }
//                                    popPay(orderid);
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

    /*刷卡完成  不用了*/
    private void popPay(final String orderid) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_pay, null);
        ImageView close = dialogView.findViewById(R.id.pop_pay_close);
        Button button = dialogView.findViewById(R.id.pop_pay_but);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.8;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹窗在底部*/
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*关闭*/
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        /*交易完成*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(SwingCardActivity.this, BankAddSureActivity.class);
                intent.putExtra("bankPay", orderid);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_swing_card;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        commonTitle.setText("快速刷卡");
        getUser();
    }


    @Override
    protected void onResume() {
        super.onResume();
        swingPriceEid.setText("");
        getUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            getUserInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserInfo() {
        userinfo = new SharedPreferencesUtil().getUserInfo(this);
        if (userinfo != "") {
            try {
                JSONObject json = new JSONObject(userinfo);
                JSONObject user = json.getJSONObject("user");
                rate = user.getString("rate");
                settfee = user.getString("settfee");
                swing_yj.setText("收取" + rate + "%+" + settfee + "服务费");

                swingPriceEid.addTextChangedListener(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (swingPriceEid.getText().toString().trim().length() >= 2) {
//            float fl = Integer.parseInt(rate)/100;
//            double aa = Arith.mul(Double.parseDouble(swingPriceEid.getText().toString()),Double.parseDouble(String.valueOf(fl)));
//            String ab = Arith.limitDouble2(aa,2);
            String a = new BigDecimal(rate).multiply(new BigDecimal(swingPriceEid.getText().toString())).divide(new BigDecimal(100),2, RoundingMode.HALF_UP)+"";
            swing_yj.setText("收取" + a + "+" + settfee + "服务费");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

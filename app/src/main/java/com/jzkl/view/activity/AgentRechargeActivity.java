package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.CarShop;
import com.jzkl.Bean.Charge;
import com.jzkl.Bean.PayWay;
import com.jzkl.Bean.ShopListShop;
import com.jzkl.R;
import com.jzkl.adapter.AgentChargeGridAdapter;
import com.jzkl.adapter.PayWayListAdapter;
import com.jzkl.adapter.ShopGridAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 代理充值
 * */
public class AgentRechargeActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.agent_recharge_grid)
    MyGridView rechargeGrid;
    @BindView(R.id.agent_recharge_list)
    MyListView rechargeList;
    @BindView(R.id.agent_recharge_pay)
    Button agentRechargePay;

    @BindView(R.id.recharge_money)
    ImageView recharge_money;


    protected ImmersionBar mImmersionBar;
    List<PayWay> list;
    private String [] payTitle = {"支付宝","微信"};
    private int [] payImg = {R.mipmap.pay_alipay,R.mipmap.pay_wechat};
    PayWayListAdapter adapter;

    List<Charge> listGrid;
    AgentChargeGridAdapter adapterGrid;
    String rechargeType;
    CustomDialog customDialog;
    String userinfo, token,carShopId,orderNum;
    String charset, sign, signType, extend,mid,userId,defaultId;
    JSONObject data;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_agent_recharge;
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

    @OnClick({R.id.common_back,R.id.agent_recharge_pay})
    public void onCilck(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*支付*/
            case R.id.agent_recharge_pay:
                if(rechargeType.equals("0")){
                    subOrder();
                }else if(rechargeType.equals("1")){
                    subUpdate("3");
                }else if(rechargeType.equals("2")){
                    subUpdate("2");
                }else if(rechargeType.equals("3")){
                    subUpdate("1");
                }
                break;

        }
    }

    @Override
    protected void initData() {
        getUser();
        /*rechargeType 0 短信充值  1 199充值  2 499充值  3 1999充值*/
        rechargeType = getIntent().getStringExtra("rechargeType");
        if(rechargeType.equals("0")){
            commonTitle.setText("短信充值");
            recharge_money.setVisibility(View.GONE);
            rechargeGrid.setVisibility(View.VISIBLE);

            /*充值金额*/
            getDate2();
            rechargeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapterGrid.setSelection(position);
                    Charge charge = (Charge) rechargeGrid.getAdapter().getItem(position);
                    carShopId = charge.getChargeId();
                }
            });
        }else if(rechargeType.equals("1")){
            commonTitle.setText("黄金会员");
            recharge_money.setVisibility(View.VISIBLE);
            recharge_money.setImageResource(R.mipmap.agent_money1);
            rechargeGrid.setVisibility(View.GONE);
        }else if(rechargeType.equals("2")){
            commonTitle.setText("白金会员");
            recharge_money.setVisibility(View.VISIBLE);
            recharge_money.setImageResource(R.mipmap.agent_money2);
            rechargeGrid.setVisibility(View.GONE);
        }else if(rechargeType.equals("3")){
            commonTitle.setText("钻石会员");
            recharge_money.setVisibility(View.VISIBLE);
            recharge_money.setImageResource(R.mipmap.agent_money3);
            rechargeGrid.setVisibility(View.GONE);
        }

        /*=====================支付列表======暂时不用了=========================*/
        list = new ArrayList<>();
        getDate();
        adapter = new PayWayListAdapter(this,list);
        rechargeList.setAdapter(adapter);
        rechargeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);

            }
        });
    }
    /*获取 充值金额*/
    private void getDate2() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        /*classifyId 0 是短信*/
        Map<String,String> map = new HashMap<>();
        map.put("classifyId", "0");
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.shop_category_list)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                listGrid = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    for (int i = 0; i <array.length() ; i++) {
                                        Charge charge = new Charge();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String goodId = jsonObject1.getString("id");
                                        String shopName = jsonObject1.getString("title");
                                        String price = jsonObject1.getString("price");
                                        String type = jsonObject1.getString("type");
                                        String credit = jsonObject1.getString("credit");

                                        charge.setChargeMoney(price);
                                        charge.setChargeTitle(shopName);
                                        charge.setChargeId(goodId);
                                        listGrid.add(charge);
                                    }
                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    carShopId = jsonObject0.getString("id");
                                }else {
                                    ToastUtil.show(msg);
                                }
                                adapterGrid = new AgentChargeGridAdapter(AgentRechargeActivity.this,listGrid);
                                rechargeGrid.setAdapter(adapterGrid);

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

    /*升级下单 3 是黄金 2 白金 1 钻石*/
    private void subUpdate(String type) {
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.shop_add_SubOrder)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    orderNum = jsonObject.getString("orderNum");
                                    customDialog = new CustomDialog(AgentRechargeActivity.this,R.style.CustomDialog);
                                    customDialog.show();
                                    pay();
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
                        ToastUtil.show("" + e);
                    }
                });
    }

    /*下单*/
    private void subOrder() {
        Map<String, String> map = new HashMap<>();
        /*shopBuy 0立即购买  1 购物车结算*/
        map.put("itemId", carShopId);
        map.put("num", "1");
        map.put("standardId", "0");
        map.put("addressId", "");
        String url = Webcon.url + Webcon.shop_order_buy;

        final String json = new Gson().toJson(map);
        OkHttpUtils.post(url)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    orderNum = jsonObject.getString("orderNum");
                                    customDialog = new CustomDialog(AgentRechargeActivity.this,R.style.CustomDialog);
                                    customDialog.show();
                                    pay();
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
                        ToastUtil.show("" + e);
                    }
                });
    }

    /*支付*/
    private void pay() {
        Map<String,String> map = new HashMap<>();
        map.put("orderNum",orderNum);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.shop_order_payData)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String code = jsonObject.getString("code");
                                String msg = jsonObject.getString("msg");
                                if (code.equals("0")) {
                                    ToastUtil.show("下单成功");
                                    charset = jsonObject.getString("charset");
                                    data = jsonObject.getJSONObject("data");
                                    JSONObject body = data.getJSONObject("body");
                                    JSONObject head =  data.getJSONObject("head");
                                    userId = body.getString("userId");
                                    mid = head.getString("mid");
                                    sign = jsonObject.getString("sign");
                                    signType = jsonObject.getString("signType");
                                    extend = jsonObject.getString("extend");
                                    String url = jsonObject.getString("url");
                                    subPay(url);
                                } else if (code.equals("500")){
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

    private void subPay(String url){
        String dataPay = data.toString().replaceAll("\\\\","");
        Map<String, String> map = new HashMap<>();
        map.put("charset", charset);
//        map.put("sign", "cYbhO01%2BxO7pLQNGSDw7EeiojXjvhaVGDHsMiWBPTqKvfU6EAOesQna4%2BMcFSI5K3Op5EvW%2FUb8Ijf4Vb24w0FA%2BDS%2FI0msG50EVyWTTWsPtcFn4vcK5HJ%2F7lHSbiXqu7AeGBmnz6uVRoxKTjpsaoxwV4WFJ3ZFel3Uyo6lO%2FuOkZ7p3cVPrBdqGXtpHQxIzTXpc7p95alMAM3CUEWzQgWSpT%2FhTvn8IBV2bywv6xPFydeIZF1VYkswH69m0FjcVk5oLLVY1whee0UBZHyryLFnGy14tordmv3eh31%2F24TaQUNuBt9bpf2n%2BXdD4skJyB4OBRgpfjcLvBU%2F%2BK1pHYg%3D%3D");
//        map.put("data", "{\"head\":{\"accessType\":\"1\",\"plMid\":\"\",\"method\":\"sandPay.fastPay.quickPay.index\",\"productId\":\"00000016\",\"mid\":\"18838056\",\"channelType\":\"07\",\"reqTime\":\"20181120130501\",\"version\":\"1.0\"},\"body\":{\"clearCycle\":\"0\",\"extend\":\"\",\"totalAmount\":\"000000000001\",\"orderTime\":\"20181120130501\",\"subject\":\"test01Title\",\"notifyUrl\":\"http://223.11.8.157:8080/sync\",\"frontUrl\":\"http://223.11.8.157:8080/sync\",\"orderCode\":\"D20181120130501\",\"body\":\"test01Desc\",\"userId\":\"000004\",\"currencyCode\":\"156\"}}");
        map.put("sign", sign);
        map.put("data", dataPay);
        map.put("signType", signType);
        map.put("extend", extend);

        OkHttpUtils.post(url)
                .headers("Accept","application/HTML")
                .params(map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {

                            int start = s.indexOf("=",s.indexOf("sessionId="));
                            int end = s.indexOf("&",s.indexOf("sessionId="));
                            String sesionId = s.substring(start+1,end);

                            Intent intent = new Intent(AgentRechargeActivity.this,HtmlActivity.class);
                            intent.putExtra("sesionId",sesionId);
                            intent.putExtra("userId",userId);
                            intent.putExtra("mid",mid);
                            intent.putExtra("orderNum",orderNum);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.show(""+e);
                    }
                });
    }

    private void getDate() {
        for (int i = 0; i <payTitle.length ; i++) {
            PayWay payWay = new PayWay();
            payWay.setPayWayImg(payImg[i]);
            payWay.setPayWayTitle(payTitle[i]);
            list.add(payWay);
        }
    }

    /*获取用户信息*/
    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        if (userinfo != "") {
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
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

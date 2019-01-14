package com.jzkl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.Address;
import com.jzkl.Bean.EventsAddressInfo;
import com.jzkl.Bean.OrderSure;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.adapter.AddressListAdapter;
import com.jzkl.adapter.OrderSureListAdapter;
import com.jzkl.util.Arith;
import com.jzkl.util.BaseCallBack;
import com.jzkl.util.BaseOkHttpClient;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 确认订单
 * */
public class OrderSureActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.order_sure_ll)
    LinearLayout orderSureLl;
    @BindView(R.id.order_sure_img)
    ImageView orderSureImg;
    @BindView(R.id.order_sure_name)
    TextView orderSureName;
    @BindView(R.id.order_sure_tel)
    TextView orderSureTel;
    @BindView(R.id.order_sure_addr)
    TextView orderSureAddr;
    @BindView(R.id.order_sure_tishi)
    TextView orderSureTishi;
    @BindView(R.id.order_sure_arrow)
    ImageView orderSureArrow;
    @BindView(R.id.order_addr_rl)
    RelativeLayout orderAddrRl;
    @BindView(R.id.order_sure_line)
    TextView orderSureLine;
    @BindView(R.id.order_sure_list)
    MyListView orderSureList;
    @BindView(R.id.order_sure_tatail)
    TextView orderSureTatail;
    @BindView(R.id.order_sure_but)
    TextView orderSureBut;

    List<OrderSure> list;
    protected ImmersionBar mImmersionBar;
    String carShopDetail, shopBuy, totalPrice, url, ss,orderNum;
    CustomDialog customDialog;
    String userinfo, token, carShopId, carShopSizeId, carShopNum, carShopPrice,shopType;
    String charset, sign, signType, extend,mid,userId,defaultId;
    JSONObject data;
    String addrName,telphone,addrAear,address;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order_sure;
    }

    /*收货地址*/
    @Subscribe
    public void onEvent(EventsAddressInfo event) {
        defaultId = event.getaddressId();
        addrName  = event.getaddressName();
        telphone  = event.getaddressTel();
        addrAear  = event.getAddressAear();
        address = event.getaddressDetail();

        orderSureName.setText("收货人:" + addrName);
        orderSureTel.setText(telphone);
        orderSureAddr.setText("收地址:" + addrAear + address);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        commonTitle.setText("确认订单");
        carShopDetail = getIntent().getStringExtra("carShopDetail");
        shopBuy = getIntent().getStringExtra("shopBuy");
        totalPrice = getIntent().getStringExtra("totalPrice");

        getUser();
        getAddress();
        list = new ArrayList<>();
        getData();
        OrderSureListAdapter adapter = new OrderSureListAdapter(this, list);
        orderSureList.setAdapter(adapter);

        if(shopType.equals("1")){
            orderSureTatail.setText("￥" + totalPrice);
        }else {
            orderSureTatail.setText(totalPrice + "积分");
        }
    }

    /*上一级传过来的值   并赋值*/
    private void getData() {
        try {
            JSONArray jsonArray = new JSONArray(carShopDetail);
            StringBuilder carStr = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                OrderSure orderSure = new OrderSure();
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                carShopId = jsonObject.getString("carShopId");
                carShopSizeId = jsonObject.getString("carShopSizeId");
                String carShopColor = jsonObject.getString("carShopColor");
                String carShopImg = jsonObject.getString("carShopImg");
                String carShopName = jsonObject.getString("carShopName");
                carShopNum = jsonObject.getString("carShopNum");
                carShopPrice = jsonObject.getString("carShopPrice");
                shopType = jsonObject.getString("carShopType");

                orderSure.setSureShopId(carShopId);
                orderSure.setSureShopSizeId(carShopSizeId);
                orderSure.setSureColor(carShopColor);
                orderSure.setSureImg(carShopImg);
                orderSure.setSureName(carShopName);
                orderSure.setSureNum(carShopNum);
                orderSure.setSurePrice(carShopPrice);
                orderSure.setSureType(shopType);
                //当循环到最后一个的时候 就不添加逗号,
                if (i == list.size() - 1) {
                    carStr.append(carShopId);
                } else {
                    carStr.append(carShopId);
                    carStr.append(",");
                }
                list.add(orderSure);
            }
            ss = carStr.substring(0, carStr.length() - 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.common_back, R.id.order_addr_rl, R.id.order_sure_but})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*地址*/
            case R.id.order_addr_rl:
                Intent intent = new Intent(OrderSureActivity.this, AddressActivity.class);
                intent.putExtra("orderAddress", "1");
                startActivity(intent);
                break;
            /*确认支付*/
            case R.id.order_sure_but:
                if(addrName==null){
                    ToastUtil.show("请添加收货人地址");
                    return;
                }
                subOrder();
                break;

        }
    }

    /*下单*/
    private void subOrder() {
        Map<String, String> map = new HashMap<>();
        /*shopBuy 0立即购买  1 购物车结算*/
        if (shopBuy.equals("0")) {
            map.put("itemId", carShopId);
            map.put("num", carShopNum);
            map.put("standardId", carShopSizeId);
            map.put("addressId", defaultId);
            url = Webcon.url + Webcon.shop_order_buy;
        } else if (shopBuy.equals("1")) {
            map.put("cartIds", ss);
            map.put("addressId", defaultId);
            url = Webcon.url + Webcon.shop_order_carbuy;
        }
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
                                    customDialog = new CustomDialog(OrderSureActivity.this,R.style.CustomDialog);
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
        /*shopType 1 钱  否则是  积分*/
        if(shopType.equals("1")){
            url = Webcon.url + Webcon.shop_order_payData;
        }else {
            url = Webcon.url + Webcon.shop_Credit_pay;
        }
        String json = new Gson().toJson(map);
        OkHttpUtils.post(url)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if(shopType.equals("1")){
                                    /*金额支付*/
                                    if (code == 0) {
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
                                    } else {
                                        ToastUtil.show(msg);
                                    }
                                }else {//积分支付
                                    if (code == 0) {
                                        ToastUtil.show("支付成功");
                                        Intent intent = new Intent(OrderSureActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }else {
                                        ToastUtil.show(msg);
                                    }
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

                            Intent intent = new Intent(OrderSureActivity.this,HtmlActivity.class);
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

    /*地址列表 中取默认地址*/
    private void getAddress() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.address_list)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONArray array = jsonObject1.getJSONArray("addresses");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject jsonObject11 = (JSONObject) array.get(i);
                                        defaultId = jsonObject11.getString("id");
                                        telphone = jsonObject11.getString("phone");
                                        addrName = jsonObject11.getString("name");
                                        addrAear = jsonObject11.getString("address");
                                        address = jsonObject11.getString("detail");
                                        String isDefault = jsonObject11.getString("isDefault");
                                        if (isDefault.equals("1")) {
                                            orderSureName.setText("收货人:" + addrName);
                                            orderSureTel.setText(telphone);
                                            orderSureAddr.setText("收地址:" + address + address);
                                        }
                                    }
                                } else {
                                    orderSureName.setText("");
                                    orderSureTel.setText("");
                                    orderSureAddr.setText("");
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
                        ToastUtil.show(e + "");
                    }
                });
    }
}

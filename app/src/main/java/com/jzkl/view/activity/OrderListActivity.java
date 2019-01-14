package com.jzkl.view.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.OrderList;
import com.jzkl.R;
import com.jzkl.adapter.OrderListAdapter;
import com.jzkl.util.Arith;
import com.jzkl.util.CustomDialog;
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
* 订单列表
* */
public class OrderListActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;

    @BindView(R.id.fragmet_order_dpay)
    TextView orderDpay;
    @BindView(R.id.fragmet_order_dsend)
    TextView orderDsend;
    @BindView(R.id.fragmet_order_drecive)
    TextView orderDrecive;
    @BindView(R.id.fragmet_order_finish)
    TextView orderFinish;

    @BindView(R.id.fragment_order_list)
    MyListView mOrderList;

    @BindView(R.id.fragment_order_list_no)
    LinearLayout noOrder;


    String orderPostion;
    List<OrderList> list;
    CustomDialog customDialog;
    String userinfo,token;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order;
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
        orderPostion = getIntent().getStringExtra("postion");
        commonTitle.setText("我的订单");

        getUser();
        if(orderPostion.equals("1")){
            orderDpay.setTextColor(Color.parseColor("#2D94FF"));
            orderDsend.setTextColor(Color.parseColor("#333333"));
            orderDrecive.setTextColor(Color.parseColor("#333333"));
            orderFinish.setTextColor(Color.parseColor("#333333"));
        }else if(orderPostion.equals("2")){
            orderDpay.setTextColor(Color.parseColor("#333333"));
            orderDsend.setTextColor(Color.parseColor("#2D94FF"));
            orderDrecive.setTextColor(Color.parseColor("#333333"));
            orderFinish.setTextColor(Color.parseColor("#333333"));
        }else if(orderPostion.equals("3")){
            orderDpay.setTextColor(Color.parseColor("#333333"));
            orderDsend.setTextColor(Color.parseColor("#333333"));
            orderDrecive.setTextColor(Color.parseColor("#2D94FF"));
            orderFinish.setTextColor(Color.parseColor("#333333"));

        }else if(orderPostion.equals("4")){
            orderDpay.setTextColor(Color.parseColor("#333333"));
            orderDsend.setTextColor(Color.parseColor("#333333"));
            orderDrecive.setTextColor(Color.parseColor("#333333"));
            orderFinish.setTextColor(Color.parseColor("#2D94FF"));
        }
        getData(orderPostion);
    }


    @OnClick({R.id.common_back,R.id.fragmet_order_dpay,R.id.fragmet_order_dsend,R.id.fragmet_order_drecive,R.id.fragmet_order_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back:
                finish();
                break;
                /*待支付*/
            case R.id.fragmet_order_dpay:
                orderDpay.setTextColor(Color.parseColor("#2D94FF"));
                orderDsend.setTextColor(Color.parseColor("#333333"));
                orderDrecive.setTextColor(Color.parseColor("#333333"));
                orderFinish.setTextColor(Color.parseColor("#333333"));
                getData("1");
                break;
                /*待发货*/
            case R.id.fragmet_order_dsend:
                orderDpay.setTextColor(Color.parseColor("#333333"));
                orderDsend.setTextColor(Color.parseColor("#2D94FF"));
                orderDrecive.setTextColor(Color.parseColor("#333333"));
                orderFinish.setTextColor(Color.parseColor("#333333"));
                getData("2");
                break;
                /*已收货*/
            case R.id.fragmet_order_drecive:
                orderDpay.setTextColor(Color.parseColor("#333333"));
                orderDsend.setTextColor(Color.parseColor("#333333"));
                orderDrecive.setTextColor(Color.parseColor("#2D94FF"));
                orderFinish.setTextColor(Color.parseColor("#333333"));
                getData("3");
                break;
            case R.id.fragmet_order_finish:
                orderDpay.setTextColor(Color.parseColor("#333333"));
                orderDsend.setTextColor(Color.parseColor("#333333"));
                orderDrecive.setTextColor(Color.parseColor("#333333"));
                orderFinish.setTextColor(Color.parseColor("#2D94FF"));
                getData("4");
                break;
        }
    }

    private void getData(final String type) {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("status",type);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.shop_order_list)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    if(array.length()==0){
                                        noOrder.setVisibility(View.VISIBLE);
                                        mOrderList.setVisibility(View.GONE);
                                    }
                                    for (int i = 0; i <array.length() ; i++) {
                                        noOrder.setVisibility(View.GONE);
                                        mOrderList.setVisibility(View.VISIBLE);
                                        OrderList orderList = new OrderList();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String orderId = jsonObject1.getString("id");
                                        String title = jsonObject1.getString("title");
                                        String price = jsonObject1.getString("price");
                                        String standardName = jsonObject1.getString("standardName");
                                        String orderNum = jsonObject1.getString("orderNum");
                                        String num = jsonObject1.getString("num");
                                        String type = jsonObject1.getString("type");
                                        String credit = jsonObject1.getString("credit");
                                        String creditSum = jsonObject1.getString("creditSum");
                                        String logoUrl = jsonObject1.getString("logoUrl");
                                        String priceSum = jsonObject1.getString("priceSum");
                                        String status = jsonObject1.getString("status");

                                        orderList.setOrderId(orderId);
                                        orderList.setOrderName(title);
                                        orderList.setOrderBh(orderNum);
                                        orderList.setOrderPrice(price);
                                        orderList.setOrderNum(num);
                                        orderList.setOrderImg(logoUrl);
                                        orderList.setOrderType(type);
                                        orderList.setOrderCredit(credit);
                                        orderList.setOrderStatus(status);
                                        orderList.setOrderTotail(priceSum);
                                        orderList.setOrderTotailCredit(creditSum);

                                        list.add(orderList);
                                    }
                                }else {
                                   ToastUtil.show(msg);
                                }
                                OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this,list,type);
                                mOrderList.setAdapter(adapter);
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
}

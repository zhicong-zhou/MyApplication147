package com.jzkl.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.jzkl.Bean.Wallet;
import com.jzkl.R;
import com.jzkl.util.Arith;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.HtmlActivity;
import com.jzkl.view.activity.OrderDetailActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class WalletListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Wallet> mList;
    String userinfo, token,orderNum;
    CustomDialog customDialog;
    JSONObject data;
    String charset, sign, signType, extend,mid,userId,defaultId;

    public WalletListAdapter(Context mContext, List<Wallet> mList){
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        getUser();
        final Wallet list = (Wallet) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ShopGridHolder shopHolder = null;

        if(convertView==null){
            shopHolder = new ShopGridHolder();
            convertView = inflater.inflate(R.layout.item_wallet_juan,null);
            shopHolder.wallet_rl  = convertView.findViewById(R.id.item_wallet_rl);
            shopHolder.wallet_name  = convertView.findViewById(R.id.item_wallet_name);
            shopHolder.wallet_class  = convertView.findViewById(R.id.item_wallet_class);
            convertView.setTag(shopHolder);

        }else {
            shopHolder = (ShopGridHolder) convertView.getTag();
        }
        double price = Arith.mul(Double.parseDouble(list.getWalletPirce()),Double.parseDouble(list.getWalletNum()));
        shopHolder.wallet_name.setText(list.getWalletName()+ price +"元代金券");
        shopHolder.wallet_class.setText(list.getWalletClass());

        shopHolder.wallet_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.getWalletStatus().equals("1")){
                    pay(list.getWalletNo());
                }else {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("dizId", list.getWalletId());
                    intent.putExtra("dizName", list.getWalletName());
                    mContext.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ShopGridHolder{
        TextView wallet_name;
        TextView wallet_class;
        RelativeLayout wallet_rl;
    }


    /*支付*/
    private void pay(String orderNum) {
        customDialog = new CustomDialog(mContext,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("orderNum",orderNum);
        /*shopType 1 钱  否则是  积分*/
        String url = Webcon.url + Webcon.shop_order_payData;
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

                            Intent intent = new Intent(mContext,HtmlActivity.class);
                            intent.putExtra("sesionId",sesionId);
                            intent.putExtra("userId",userId);
                            intent.putExtra("mid",mid);
                            intent.putExtra("orderNum",orderNum);
                            mContext.startActivity(intent);
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
        userinfo = new SharedPreferencesUtil().getToken(mContext);
        if (userinfo != "") {
            try {
                JSONObject json = new JSONObject(userinfo);
                token = json.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

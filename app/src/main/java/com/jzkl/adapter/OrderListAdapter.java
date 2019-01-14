package com.jzkl.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jzkl.Bean.OrderList;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.Arith;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.OkHttpUtils;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.HtmlActivity;
import com.jzkl.view.activity.IntegrterDetailActivity;
import com.jzkl.view.activity.IntergrterSuccessActivity;
import com.jzkl.view.activity.OrderDetailActivity;
import com.jzkl.view.activity.OrderSureActivity;
import com.jzkl.view.activity.PayWayActivity;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class OrderListAdapter extends BaseAdapter {

    List<OrderList> list;
    Activity context;
    CustomDialog customDialog;
    String userinfo,token,type;
    String charset, sign, signType, extend,mid,userId;
    JSONObject data;
    String url,shopType;

    public OrderListAdapter(Activity context,List<OrderList> list,String type){
        this.context = context;
        this.list = list;
        this.type = type;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        getUser();
        final OrderList orderList = (OrderList) getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        OrderListHolder holder = null;

        if(convertView == null){
            holder = new OrderListHolder();
            convertView = inflater.inflate(R.layout.item_order_list,null);
            holder.order_rl = convertView.findViewById(R.id.item_order_rl);
            holder.order_bh = convertView.findViewById(R.id.item_order_bh);
            holder.order_status = convertView.findViewById(R.id.item_order_status);
            holder.order_img = convertView.findViewById(R.id.item_order_img);
            holder.order_name = convertView.findViewById(R.id.item_order_name);
            holder.order_price = convertView.findViewById(R.id.item_order_price);
            holder.order_num = convertView.findViewById(R.id.item_order_num);
            holder.order_tatail = convertView.findViewById(R.id.item_order_tatail);
            holder.order_sure = convertView.findViewById(R.id.item_order_sure);
            holder.order_cancel = convertView.findViewById(R.id.item_order_cancel);
            convertView.setTag(holder);
        }else {
            holder = (OrderListHolder) convertView.getTag();
        }

        holder.order_bh.setText("订单编号："+orderList.getOrderBh());
        if(orderList.getOrderStatus().equals("1")){
            holder.order_status.setText("待支付");
        }else if(orderList.getOrderStatus().equals("2")){
            holder.order_status.setText("买家已付款");
        }else if(orderList.getOrderStatus().equals("3")){
            holder.order_status.setText("卖家已发货");
        }else if(orderList.getOrderStatus().equals("4")){
            holder.order_status.setText("已完成");
        }
        if(orderList.getOrderImg().equals("")||orderList.getOrderImg().equals("null")){
            holder.order_img.setImageResource(R.mipmap.pop_shop_img);
        }else {
            OkHttpUtils.picassoImage(orderList.getOrderImg(),context,holder.order_img);
        }
        holder.order_name.setText(orderList.getOrderName());
        if(orderList.getOrderType().equals("1")){
            holder.order_price.setText("￥"+orderList.getOrderPrice());
            holder.order_tatail.setText("合计：￥"+ orderList.getOrderTotail());
        }else {
            holder.order_price.setText(orderList.getOrderCredit()+"积分");
            holder.order_tatail.setText("合计："+ orderList.getOrderTotailCredit() +"积分");
        }
        shopType = orderList.getOrderType();

        holder.order_num.setText("x"+orderList.getOrderNum());
        /*跳转详情*/
//        holder.order_rl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,OrderDetailActivity.class);
//                context.startActivity(intent);
//            }
//        });
        if(type.equals("1")){
            holder.order_sure.setVisibility(View.VISIBLE);
            holder.order_cancel.setVisibility(View.VISIBLE);

            holder.order_sure.setText("立即支付");
            holder.order_cancel.setText("取消订单");
            /*确认支付*/
            holder.order_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context,PayWayActivity.class);
//                    context.startActivity(intent);
                    customDialog = new CustomDialog(context,R.style.CustomDialog);
                    customDialog.show();
                    pay(orderList.getOrderBh());
                }
            });
            /*取消*/
            holder.order_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popWin(orderList.getOrderId(),position);
                }
            });
        }else if(type.equals("2")){
            holder.order_sure.setVisibility(View.GONE);
            holder.order_cancel.setVisibility(View.GONE);
        }else if(type.equals("3")){
            holder.order_sure.setVisibility(View.VISIBLE);
            holder.order_cancel.setVisibility(View.GONE);
            holder.order_sure.setText("确认收货");
            /*确认收货*/
            holder.order_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sureOrder(orderList.getOrderId(),position);
                }
            });
        }else if(type.equals("4")){
            holder.order_sure.setVisibility(View.GONE);
            holder.order_cancel.setVisibility(View.GONE);
        }
        return convertView;
    }

    private void pay(final String orderNum) {
        Map<String,String> map = new HashMap<>();
        map.put("orderNum",orderNum);
        /*shopType 1 钱  否则是  积分*/
        if(shopType.equals("1")){
            url = Webcon.url + Webcon.shop_order_payData;
        }else {
            url = Webcon.url + Webcon.shop_Credit_pay;
        }
        String json = new Gson().toJson(map);
        com.lzy.okhttputils.OkHttpUtils.post(url)
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
                                    if (code == 0) {
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
                                        subPay(url,orderNum);
                                    } else {
                                        ToastUtil.show(msg);
                                    }
                                }else {//积分支付
                                    if (code == 0) {
                                        ToastUtil.show("支付成功");
                                        Intent intent = new Intent(context,MainActivity.class);
                                        context.startActivity(intent);
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

    private void subPay(String url, final String orderNum){

        String dataPay = data.toString().replaceAll("\\\\","");

        Map<String, String> map = new HashMap<>();
        map.put("charset", charset);
//        map.put("sign", "cYbhO01%2BxO7pLQNGSDw7EeiojXjvhaVGDHsMiWBPTqKvfU6EAOesQna4%2BMcFSI5K3Op5EvW%2FUb8Ijf4Vb24w0FA%2BDS%2FI0msG50EVyWTTWsPtcFn4vcK5HJ%2F7lHSbiXqu7AeGBmnz6uVRoxKTjpsaoxwV4WFJ3ZFel3Uyo6lO%2FuOkZ7p3cVPrBdqGXtpHQxIzTXpc7p95alMAM3CUEWzQgWSpT%2FhTvn8IBV2bywv6xPFydeIZF1VYkswH69m0FjcVk5oLLVY1whee0UBZHyryLFnGy14tordmv3eh31%2F24TaQUNuBt9bpf2n%2BXdD4skJyB4OBRgpfjcLvBU%2F%2BK1pHYg%3D%3D");
//        map.put("data", "{\"head\":{\"accessType\":\"1\",\"plMid\":\"\",\"method\":\"sandPay.fastPay.quickPay.index\",\"productId\":\"00000016\",\"mid\":\"18838056\",\"channelType\":\"07\",\"reqTime\":\"20181120130501\",\"version\":\"1.0\"},\"body\":{\"clearCycle\":\"0\",\"extend\":\"\",\"totalAmount\":\"000000000001\",\"orderTime\":\"20181120130501\",\"subject\":\"test01Title\",\"notifyUrl\":\"http://223.11.8.157:8080/sync\",\"frontUrl\":\"http://223.11.8.157:8080/sync\",\"orderCode\":\"D20181120130501\",\"body\":\"test01Desc\",\"userId\":\"000004\",\"currencyCode\":\"156\"}}");
        map.put("sign", sign);
        map.put("data", dataPay);
        map.put("signType", signType);
        map.put("extend", extend);

        com.lzy.okhttputils.OkHttpUtils.post(url)
                .headers("Accept","application/HTML")
                .params(map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {

                            int start = s.indexOf("=",s.indexOf("sessionId="));
                            int end = s.indexOf("&",s.indexOf("sessionId="));
                            String sesionId = s.substring(start+1,end);

                            Intent intent = new Intent(context,HtmlActivity.class);
                            intent.putExtra("sesionId",sesionId);
                            intent.putExtra("userId",userId);
                            intent.putExtra("mid",mid);
                            intent.putExtra("orderNum",orderNum);
                            context.startActivity(intent);
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.show(""+e);
                    }
                });
    }

    private void popWin(final String orderId, final int position) {
        //加载布局并初始化组件0
        View dialogView = LayoutInflater.from(context).inflate(R.layout.pop_intergter, null);
        TextView dialogText = dialogView.findViewById(R.id.dialog_text);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(context, R.style.AlertDialog);

        dialogContent.setText("确定删除吗？");
        dialogBtnConfirm.setText("确定");
        dialogBtnCancel.setText("取消");
//        layoutDialog.setTitle("提示");
//        layoutDialog.setIcon(R.mipmap.ic_launcher_round);
        /*获取屏幕宽高*/
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*确定*/
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteOrder(orderId,position);
            }
        });
        /*取消*/
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    /*删除订单*/
    private void deleteOrder(String orderId, final int position) {
        customDialog = new CustomDialog(context,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("orderId",orderId);
        String json = new Gson().toJson(map);

        com.lzy.okhttputils.OkHttpUtils.post(Webcon.url + Webcon.shop_order_deleta)
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
                                    list.remove(position);
                                    notifyDataSetChanged();
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
                    }
                });
    }
    /*确认收货*/
    private void sureOrder(String orderId,final int position) {
        customDialog = new CustomDialog(context,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("orderId",orderId);
        String json = new Gson().toJson(map);

        com.lzy.okhttputils.OkHttpUtils.post(Webcon.url + Webcon.shop_order_confrim)
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
                                    list.remove(position);
                                    notifyDataSetChanged();
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
                    }
                });
    }
    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(context);
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

    class OrderListHolder{
        RelativeLayout order_rl;
        TextView order_bh;
        TextView order_status;
        ImageView order_img;
        TextView order_name;
        TextView order_price;
        TextView order_num;
        TextView order_tatail;
        TextView order_sure;
        TextView order_cancel;
    }
}

package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 电子卷下单
 * */
public class DianziJuanOrderActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.dianzi_ju_all)
    LinearLayout dianziJuAll;
    @BindView(R.id.dianzi_detail_img)
    ImageView dianziDetailImg;
    @BindView(R.id.dianzi_detail_title)
    TextView dianziDetailTitle;
    @BindView(R.id.dianzi_detail_but1)
    TextView dianziDetailBut1;
    @BindView(R.id.dianzi_detail_but2)
    TextView dianziDetailBut2;
    @BindView(R.id.dianzi_detail_time)
    TextView dianziDetailTime;
    @BindView(R.id.dianzi_detail_del)
    TextView dianziDetailDel;
    @BindView(R.id.dianzi_detail_edi)
    TextView dianziDetailEdi;
    @BindView(R.id.dianzi_detail_edi2)
    TextView dianziDetailEdi2;
    @BindView(R.id.dianzi_detail_add)
    TextView dianziDetailAdd;
    @BindView(R.id.dianzi_detail_price)
    TextView dianziDetailPrice;
    @BindView(R.id.dianzi_detail_dptitle)
    TextView dianziDetailDptitle;
//    @BindView(R.id.dianzi_detail_dpdis)
//    TextView dianziDetailDpdis;
    @BindView(R.id.dianzi_detail_dpaddr)
    TextView dianziDetailDpaddr;
    @BindView(R.id.dianzi_detail_dpll)
    TextView dianziDetailDpll;
    @BindView(R.id.dianzi_detail_tel)
    ImageView dianziDetailTel;
    @BindView(R.id.dianzi_detail_tel_jtime)
    TextView dianziDetailTelJtime;
    @BindView(R.id.dianzi_detail_totail_price)
    TextView totailPrice;
    @BindView(R.id.dianzi_detail_sub)
    LinearLayout dianziDetailSub;
    @BindView(R.id.dianzi_ju_button)
    LinearLayout dianziJuButton;

    @BindView(R.id.dianzi_detail_map)
    LinearLayout dianzi_detail_map;


    String yhjId,yhjName;
    CustomDialog customDialog;
    String userinfo, token,price,mobileDz;
    String charset, sign, signType, extend,mid,userId,orderNum;
    JSONObject data;
    int quantity=1;
    double endPrice;
    String longitude,latitude;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(android.R.color.white)
                .init();
    }


    @SuppressLint("MissingPermission")
    @OnClick({R.id.common_back,R.id.dianzi_detail_del,R.id.dianzi_detail_add,R.id.dianzi_detail_tel,R.id.dianzi_detail_sub,R.id.dianzi_detail_map})
    public void Onclikc(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*减*/
            case R.id.dianzi_detail_del:
//                quantity--;
//                if(quantity>0){
//                    dianziDetailEdi.setText(String.valueOf(quantity));
//                    endPrice = Arith.mul(Double.parseDouble(price),quantity);
//                    dianziDetailPrice.setText(Double.toString(endPrice));
//                    totailPrice.setText(Double.toString(endPrice));
//                }
                break;
                /*加*/
            case R.id.dianzi_detail_add:
//                if(quantity< 0){
//                    quantity = 1;
//                }
//                quantity++;
//                dianziDetailEdi.setText(String.valueOf(quantity));
//                endPrice = Arith.mul(Double.parseDouble(price),quantity);
//                dianziDetailPrice.setText(Double.toString(endPrice));
//                totailPrice.setText(Double.toString(endPrice));
                break;
                /*打电话*/
            case R.id.dianzi_detail_tel:
                /*ACTION_DIAL 跳转 拨号页面 ACTION_CALL直接拨打*/
                AndPermission.with(this).permission(Manifest.permission.CALL_PHONE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:"+ mobileDz);
                        intent.setData(data);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                        Uri packageURI = Uri.parse("package:" + getPackageName());
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        Toast.makeText(DianziJuanOrderActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();

                break;
                /*支付*/
            case R.id.dianzi_detail_sub:
                if(quantity == 1){
                    endPrice = Double.parseDouble(price);
                }
                subPay();
                break;
                /*地图*/
            case R.id.dianzi_detail_map:
                AndPermission.with(this).permission(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(DianziJuanOrderActivity.this,MapActivity.class);
                        intent.putExtra("longitude",longitude);
                        intent.putExtra("latitude",latitude);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Toast.makeText(DianziJuanOrderActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dianzi_juan_order;
    }

    @Override
    protected void initData() {
        yhjId = getIntent().getStringExtra("yhjId");
        yhjName = getIntent().getStringExtra("yhjName");
        commonTitle.setText(yhjName);

        getUser();
        getDiziDetail();
    }
    /*详情*/
    private void getDiziDetail() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("discountId",yhjId);

        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.My_reduction_detail)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if(code==0){
                                    JSONObject detail = jsonObject.getJSONObject("detail");
                                    String createTime = detail.getString("createTime");
                                    String deadline = detail.getString("deadline");
                                    String title = detail.getString("title");
                                    String logoUrl = detail.getString("logoUrl");
                                    String type = detail.getString("type");
                                    price = detail.getString("price");
                                    String rule = detail.getString("rule");
                                    String descript = detail.getString("descript");
                                    String address = detail.getString("address");
                                    String lable = detail.getString("lable");
                                    mobileDz = detail.getString("mobile");
                                    longitude = detail.getString("longitude");
                                    latitude = detail.getString("latitude");

                                    if(logoUrl.equals("")){
                                        dianziDetailImg.setImageResource(R.mipmap.user_head);
                                    }else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(logoUrl,DianziJuanOrderActivity.this,dianziDetailImg);
                                    }
                                    dianziDetailTitle.setText(title);
                                    String[] result= lable.split(",");
                                    if(result.length!=1){
                                        dianziDetailBut2.setVisibility(View.VISIBLE);
                                        String mDetailBut1 = result[0];
                                        String mDetailBut2 = result[1];
                                        dianziDetailBut1.setText(mDetailBut1);
                                        dianziDetailBut2.setText(mDetailBut2);
                                    }else {
                                        dianziDetailBut2.setVisibility(View.GONE);
                                        dianziDetailBut1.setText(lable);
                                    }
                                    dianziDetailTime.setText("有效期："+deadline);
                                    dianziDetailPrice.setText("￥"+price);
                                    dianziDetailDptitle.setText(title);
                                    dianziDetailDpaddr.setText(address);
                                    dianziDetailTelJtime.setText(rule);
                                    totailPrice.setText("￥"+price);
                                }else {
                                    ToastUtil.show("加载失败");
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
    /*下单*/
    private void subPay() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
//        map.put("amount", String.valueOf(quantity));
        map.put("amount","1");
        map.put("discountId",yhjId);
        map.put("price", String.valueOf(endPrice));

        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.My_reduction_pay)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if(code==0){
                                    orderNum = jsonObject.getString("orderNum");
                                    ToastUtil.show("下单成功");
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
                        customDialog.dismiss();
                        ToastUtil.show(e+"");
                    }

                });
    }

    /*支付*/
    private void pay() {
        customDialog = new CustomDialog(DianziJuanOrderActivity.this,R.style.CustomDialog);
        customDialog.show();

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
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
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

                            Intent intent = new Intent(DianziJuanOrderActivity.this,HtmlActivity.class);
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

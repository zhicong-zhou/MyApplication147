package com.jzkl.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
/*
* 电子卷订单详情
* */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.order_detail_img)
    ImageView orderDetailImg;
    @BindView(R.id.order_detail_title)
    TextView orderDetailTitle;
    @BindView(R.id.order_detail_but1)
    TextView orderDetailBut1;
    @BindView(R.id.order_detail_but2)
    TextView orderDetailBut2;
    @BindView(R.id.order_detail_time)
    TextView orderDetailTime;
    @BindView(R.id.order_detail_jnum)
    TextView orderDetailJnum;
    @BindView(R.id.order_detail_jcard)
    TextView orderDetailJcard;
    @BindView(R.id.order_detail_price)
    TextView orderDetailPrice;
    @BindView(R.id.order_detail_num)
    TextView orderDetailNum;
    @BindView(R.id.order_detail_xdtime)
    TextView orderDetailXdtime;
    @BindView(R.id.order_detail_dptitle)
    TextView orderDetailDptitle;
//    @BindView(R.id.order_detail_dpdis)
//    TextView orderDetailDpdis;
    @BindView(R.id.order_detail_dpaddr)
    TextView orderDetailDpaddr;
    @BindView(R.id.order_detail_dpll)
    TextView orderDetailDpll;
    @BindView(R.id.order_detail_tel)
    ImageView orderDetailTel;
    @BindView(R.id.order_detail_jtime)
    TextView order_detail_jtime;
    @BindView(R.id.order_detail_ll)
    LinearLayout order_detail_ll;

    @BindView(R.id.order_produce_erwma)
    ImageView produce_erwma;
    @BindView(R.id.order_map)
    LinearLayout order_map;

    protected ImmersionBar mImmersionBar;
    String mobile;
    CustomDialog customDialog;
    String userinfo, token,dizId,dizName;
    String longitude,latitude;

    @Override
    protected void initView() {
    }

    @SuppressLint("MissingPermission")
    @OnClick({R.id.common_back,R.id.order_detail_tel,R.id.order_map})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*调取电话*/
            case R.id.order_detail_tel:
                /*ACTION_DIAL 跳转 拨号页面 ACTION_CALL直接拨打*/
                AndPermission.with(this).permission(Manifest.permission.CALL_PHONE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + mobile);
                        intent.setData(data);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                        Uri packageURI = Uri.parse("package:" + getPackageName());
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                        Toast.makeText(OrderDetailActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();
                break;
                /*地图*/
            case R.id.order_map:
                AndPermission.with(this).permission(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(OrderDetailActivity.this,MapActivity.class);
                        intent.putExtra("longitude",longitude);
                        intent.putExtra("latitude",latitude);
                        startActivity(intent);
                    }
//
                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Toast.makeText(OrderDetailActivity.this, "没有权限无法拨打", Toast.LENGTH_LONG).show();
                    }
                }).start();

                break;
        }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initData() {
        dizId = getIntent().getStringExtra("dizId");
        dizName = getIntent().getStringExtra("dizName");
        commonTitle.setText(dizName);
//        yhjId = getIntent().getStringExtra("yhjId");
        getUser();
        getDetail();
    }

    private void getDetail() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        Map<String,String> map = new HashMap<>();
        map.put("discountOrderId",dizId);

        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.My_dianzi_order_detail)
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
                                if(code == 0){
                                    order_detail_ll.setVisibility(View.VISIBLE);
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("detail");
                                    String createTime = jsonObject1.getString("createTime");
                                    String amount = jsonObject1.getString("amount");
                                    String discountNum = jsonObject1.getString("discountNum");
                                    String orderNum = jsonObject1.getString("orderNum");

                                    JSONObject discountEntity = jsonObject1.getJSONObject("discountEntity");
                                    String title = discountEntity.getString("title");
                                    String logoUrl = discountEntity.getString("logoUrl");
                                    String type = discountEntity.getString("type");
                                    String price = discountEntity.getString("price");
                                    String lable = discountEntity.getString("lable");
                                    String rule = discountEntity.getString("rule");
                                    String deadline = discountEntity.getString("deadline");
                                    String address = discountEntity.getString("address");
                                    mobile = discountEntity.getString("mobile");
                                    longitude = discountEntity.getString("longitude");
                                    latitude = discountEntity.getString("latitude");

                                    if(logoUrl.equals("")){
                                        orderDetailImg.setImageResource(R.mipmap.user_head);
                                    }else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(logoUrl,OrderDetailActivity.this,orderDetailImg);
                                    }
                                    orderDetailTitle.setText(title);
                                    String[] result= lable.split(",");
                                    if(result.length!=1){
                                        orderDetailBut2.setVisibility(View.VISIBLE);
                                        String mDetailBut1 = result[0];
                                        String mDetailBut2 = result[1];
                                        orderDetailBut1.setText(mDetailBut1);
                                        orderDetailBut2.setText(mDetailBut2);
                                    }else {
                                        orderDetailBut2.setVisibility(View.GONE);
                                        orderDetailBut1.setText(lable);
                                    }
                                    orderDetailTime.setText("有效期至："+deadline);
                                    orderDetailJnum.setText("已使用("+amount+")张");
                                    orderDetailJcard.setText(discountNum);
                                    orderDetailPrice.setText(price+"元");
                                    orderDetailNum.setText(orderNum);
                                    orderDetailXdtime.setText(createTime);
                                    orderDetailDptitle.setText(title);
                                    orderDetailDpaddr.setText(address);
                                    order_detail_jtime.setText(rule);
                                    /*生成条形码*/
                                    oneCodeCreate();
                                }else {
                                    ToastUtil.show("暂无信息");
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

    /*生成条形码*/
    private void oneCodeCreate() {
        String str = orderDetailJcard.getText().toString().trim();
        int size = str.length();
        for (int i = 0; i < size; i++) {
            int c = str.charAt(i);
            if ((19968 <= c && c < 40623)) {
                Toast.makeText(OrderDetailActivity.this, "生成条形码的时刻不能是中文", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Bitmap bmp = null;
        try {
            if (str != null && !"".equals(str)) {
                bmp = CreateOneDCode(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp != null) {
            produce_erwma.setImageBitmap(bmp);
        }
    }

    /**
     * 用于将给定的内容生成成一维码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateOneDCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 500, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
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

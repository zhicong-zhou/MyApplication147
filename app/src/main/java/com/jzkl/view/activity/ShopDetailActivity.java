package com.jzkl.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.CarShop;
import com.jzkl.Bean.ShopSize;
import com.jzkl.R;
import com.jzkl.adapter.ShopSizeAdapter;
import com.jzkl.util.Arith;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class ShopDetailActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.shop_detail_title)
    TextView shopTitle;
    @BindView(R.id.shop_detail_img)
    ImageView shopDetailImg;
    @BindView(R.id.shop_de_ll)
    LinearLayout shopDeLl;
    @BindView(R.id.shop_price)
    TextView shopPrice;
    @BindView(R.id.shop_price_yj)
    TextView shopPriceYj;
    @BindView(R.id.shop_kauidi)
    TextView shopKauidi;
    @BindView(R.id.shop_buy_num)
    TextView shopBuyNum;
    @BindView(R.id.shop_address)
    TextView shopAddress;
    @BindView(R.id.shop_detail_content)
    TextView shop_detail_content;
    @BindView(R.id.shop_size)
    RelativeLayout shopSize;
    @BindView(R.id.shop_car_ll)
    LinearLayout shopCarLl;
    @BindView(R.id.shop_add_car)
    LinearLayout shopAddCar;
    @BindView(R.id.shop_car_service)
    LinearLayout service;

    @BindView(R.id.shop_detail_)
    WebView shop_detail_;

    @BindView(R.id.shop_buy_button)
    LinearLayout shopBuyButton;
    @BindView(R.id.shop_buy_button2)
    TextView buttonTxt;
    @BindView(R.id.shop_detail_contentll)
    LinearLayout shop_contentll;

    private View popView;
    private PopupWindow popupWindow;
    private  String [] sizeName ={"黑色-【可摇头+三档调节】","白色-【可摇头+三档调节】","蓝色色-【可摇头+三档调节】"};
    List<ShopSize> list;
    ShopSizeAdapter adapter;
    Intent intent;
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String shopName,shopId,credit,shopType,title,shopSizeId,logoUrl,detailUrl;
    GridView gridView;
    int quantity=1;
    String remainNumber,qq,endPrice,shopSizeName,shopSizePrice,shopSizeCredit;
    String userinfo,token,sPrice;
    Button pop_car_but,pop_buy_but;
    TextView pop_size_price;
    JSONArray array;

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
    protected int getLayoutRes() {
        return R.layout.shop_detail;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData() {
        /*中划线*/
        shopPriceYj.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        shopName = getIntent().getStringExtra("shopName");
        shopId = getIntent().getStringExtra("shopId");
        commonTitle.setText("商品详情");
        getUser();
        /*获取商品详情*/
        getShopDetail();


    }
    /*获取商品详情*/
    private void getShopDetail() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("itemId",shopId);
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.shop_detail)
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
                                    JSONObject shopDetail = jsonObject.getJSONObject("item");
                                    title = shopDetail.getString("title");
                                    logoUrl = shopDetail.getString("logoUrl");
                                    String address = shopDetail.getString("address");
                                    String salesNumber = shopDetail.getString("salesNumber");
                                    shopSizePrice = shopDetail.getString("price");
                                    String oldDescript = shopDetail.getString("oldDescript");
                                    credit = shopDetail.getString("credit");
                                    shopType = shopDetail.getString("type");
                                    String tag = shopDetail.getString("tag");
                                    detailUrl = shopDetail.getString("detailUrl");
                                    remainNumber = shopDetail.getString("remainNumber");
                                    qq = shopDetail.getString("qq");

                                    if(logoUrl.equals("")){
                                        shopDetailImg.setImageResource(R.mipmap.pop_shop_img);
                                    }else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(logoUrl,ShopDetailActivity.this,shopDetailImg);
                                    }
                                    shopTitle.setText(title);
                                    if(shopType.equals("1")){
                                        shopCarLl.setVisibility(View.VISIBLE);
                                        shopAddCar.setVisibility(View.VISIBLE);
                                        service.setVisibility(View.VISIBLE);
                                        shopPrice.setText("￥"+shopSizePrice);
                                        buttonTxt.setText("立即购买");
                                    }else {
                                        shopCarLl.setVisibility(View.GONE);
                                        shopAddCar.setVisibility(View.GONE);
                                        service.setVisibility(View.GONE);
                                        shopPrice.setText(credit+"积分");
                                        shopBuyButton.setBackgroundResource(R.color.shop_buttonBg);
                                        buttonTxt.setText("积分兑换");
                                    }
                                    shopPriceYj.setText("原价："+oldDescript+"元");
                                    shopBuyNum.setText("销量："+salesNumber+"件");
                                    shopAddress.setText(address);
                                    if(tag.equals("")|| tag.equals("null")){
                                        shop_contentll.setVisibility(View.GONE);
                                    }else {
                                        shop_contentll.setVisibility(View.VISIBLE);
                                        shop_detail_content.setText(tag);
                                    }
                                    String aa = "<img width='100%' src='" + detailUrl + " '/>";
                                    /*转化成 webview*/
                                    shop_detail_.loadDataWithBaseURL(null,getHtmlData(aa)+"", "text/html",  "utf-8", null);
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

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    /*添加购物车*/
    private void addCar(){
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("itemId",shopId);
        map.put("num", String.valueOf(quantity));
        map.put("standardId",shopSizeId);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.shop_add_cart)
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
                                    ToastUtil.show("添加成功");
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
                    }
                });
    }

    @OnClick({R.id.common_back,R.id.shop_size,R.id.shop_car_ll,R.id.shop_add_car,R.id.shop_buy_button,R.id.shop_car_service})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
                /*选择规格*/
            case R.id.shop_size:
                popWin();
                break;
                /*购物车*/
            case R.id.shop_car_ll:
                intent = new Intent(ShopDetailActivity.this,CarActivity.class);
                startActivity(intent);
                break;
                /*加入购物车*/
            case R.id.shop_add_car:
                popWin();
                break;
                /*立即购买*/
            case R.id.shop_buy_button:
                popWin();
                break;
                /*客服*/
            case R.id.shop_car_service:
                if(isQQClientAvailable(ShopDetailActivity.this)){
                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+qq+"&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                }else{
                    ToastUtil.show("请安装QQ客户端");
                }
                break;
        }
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void popWin() {
        popView = LayoutInflater.from(this).inflate(R.layout.pop_shop_size,null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);
        gridView = popView.findViewById(R.id.pop_gride_size);
        RelativeLayout pop_sizell = popView.findViewById(R.id.pop_sizell);
        ImageView pop_size_exit = popView.findViewById(R.id.pop_size_exit);
        final ImageView pop_size_img = popView.findViewById(R.id.pop_size_img);
        final TextView pop_size_name = popView.findViewById(R.id.pop_size_name);
        pop_size_price = popView.findViewById(R.id.pop_size_price);
        pop_car_but = popView.findViewById(R.id.pop_size_carbut);
        pop_buy_but = popView.findViewById(R.id.pop_buy_but);
        TextView pop_size_del = popView.findViewById(R.id.pop_size_del);
        TextView pop_size_add = popView.findViewById(R.id.pop_size_add);
        final TextView pop_size_edi = popView.findViewById(R.id.pop_size_edi);
        TextView pop_size_num = popView.findViewById(R.id.pop_size_num);

        /*获取尺寸*/
        getDate();

        if(shopType.equals("1")){
            pop_car_but.setVisibility(View.VISIBLE);
                pop_size_price.setText("￥"+shopSizePrice);
        }else{
            pop_car_but.setVisibility(View.GONE);
                pop_size_price.setText(credit+"积分");
            pop_buy_but.setBackgroundResource(R.color.shop_buttonBg);
            pop_buy_but.setText("积分兑换");
        }

        if(logoUrl.equals("")){
            pop_size_img.setImageResource(R.mipmap.shop_bg_img);
        }else {
            com.jzkl.util.OkHttpUtils.picassoImage(logoUrl,this,pop_size_img);
        }
        pop_size_name.setText(title);
        /*库存*/
        pop_size_num.setText("剩余"+remainNumber+"件");
        /*尺寸的点击事件*/
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                ShopSize shopSize = (ShopSize) parent.getAdapter().getItem(position);
                shopSizeId = shopSize.getShopSizeId();
                shopSizeName = shopSize.getShopSizeColor();
                shopSizePrice = shopSize.getShopSizePrice();
                shopSizeCredit = shopSize.getShopSizeCredit();

                if(shopType.equals("1")){
                    pop_car_but.setVisibility(View.VISIBLE);
                    if(array.length()!=0){
                        pop_size_price.setText("￥"+shopSizePrice);
                        shopPrice.setText("￥"+shopSizePrice);
                    }else {
                        pop_size_price.setText("￥"+shopSizePrice);
                    }
                }else{
                    pop_car_but.setVisibility(View.GONE);
                    if(array.length()!=0){
                        pop_size_price.setText(shopSizeCredit+"积分");
                        shopPrice.setText(shopSizeCredit+"积分");
                    }else {
                        pop_size_price.setText("");
                    }
                    pop_buy_but.setBackgroundResource(R.color.shop_buttonBg);
                    pop_buy_but.setText("积分兑换");
                }
            }
        });
        /*关闭*/
        pop_size_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        /*加入购物车*/
        pop_car_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(shopSizeId == null || shopSizeId.equals("")){
                    shopSizeId = "0";
                }
                if(Integer.parseInt(remainNumber)>0){
                    addCar();
                }else {
                    ToastUtil.show("商品不足，无法添加");
                }
            }
        });
        /*立即购买*/
        pop_buy_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(shopSizeName == null){
                    shopSizeName = "";
                }
                if(shopSizeId == null){
                    shopSizeId = "";
                }
                if(Integer.parseInt(remainNumber)>0){
                    /*积分 还是 钱*/
                    if(shopType.equals("1")){
                        sPrice = shopSizePrice;
                    }else{
                        sPrice = shopSizeCredit;
                    }
                    List<CarShop> newCar = new ArrayList();
                    CarShop car = new CarShop();
                    car.setCarShopName(title);
                    car.setCarShopPrice(Double.parseDouble(sPrice));
                    car.setCarShopId(shopId);
                    car.setCarShopSizeId(shopSizeId);
                    car.setCarShopColor(shopSizeName);
                    car.setCarShopCredit(credit);
                    car.setCarShopNum(quantity);
                    car.setCarShopImg(logoUrl);
                    car.setCarShopType(shopType);
                    newCar.add(car);

                    if(quantity == 1){
                        endPrice = sPrice;
                    }
                    intent = new Intent(ShopDetailActivity.this,OrderSureActivity.class);
                    intent.putExtra("carShopDetail",new Gson().toJson(newCar));
                    intent.putExtra("shopBuy","0");
                    intent.putExtra("totalPrice",endPrice);
                    startActivity(intent);
                }else {
                    ToastUtil.show("商品不足，无法购买");
                }
            }
        });
        /*减*/
        pop_size_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity--;
                if(quantity>0){
                    pop_size_edi.setText(String.valueOf(quantity));
                    if(shopType.equals("1")){
                        double endPrice1 = Arith.mul(Double.parseDouble(shopSizePrice),quantity);
                        DecimalFormat df = new DecimalFormat("#0.00");
                        endPrice = df.format(endPrice1);
                        pop_size_price.setText("￥"+endPrice);
                    }else if(shopType.equals("2")){
                        double endPrice1 = Arith.mul(Double.parseDouble(shopSizeCredit),quantity);
                        DecimalFormat df = new DecimalFormat("#");
                        endPrice = df.format(endPrice1);
                        pop_size_price.setText(endPrice+"积分");
                    }
                }
            }
        });
        /*加*/
        pop_size_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity< 0){
                    quantity = 1;
                }
                quantity++;
                pop_size_edi.setText(String.valueOf(quantity));
                if(shopType.equals("1")){
                    double endPrice1 = Arith.mul(Double.parseDouble(shopSizePrice),quantity);
                    DecimalFormat df = new DecimalFormat("#0.00");
                    endPrice = df.format(endPrice1);
                    pop_size_price.setText("￥"+endPrice);
                }else if(shopType.equals("2")){
                    double endPrice1 = Arith.mul(Double.parseDouble(shopSizeCredit),quantity);
                    DecimalFormat df = new DecimalFormat("#");
                    endPrice = df.format(endPrice1);
                    pop_size_price.setText(endPrice+"积分");
                }
            }
        });
    }
    /*获取尺寸*/
    private void getDate() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("itemId",shopId);
        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.shop_size)
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
                                    array = jsonObject.getJSONArray("list");
                                    for (int i = 0; i < array.length(); i++) {
                                        ShopSize shopSize = new ShopSize();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String shopSzieName = jsonObject1.getString("name");
                                        String shopId = jsonObject1.getString("id");
                                        String price = jsonObject1.getString("price");
                                        String credit = jsonObject1.getString("credit");
                                        shopSize.setShopSizeColor(shopSzieName);
                                        shopSize.setShopSizeId(shopId);
                                        shopSize.setShopSizePrice(price);
                                        shopSize.setShopSizeCredit(credit);
                                        list.add(shopSize);
                                    }
                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    shopSizeName = jsonObject0.getString("name");
                                    shopSizeId = jsonObject0.getString("id");
                                    shopSizePrice = jsonObject0.getString("price");
                                    shopSizeCredit = jsonObject0.getString("credit");
                                    adapter = new ShopSizeAdapter(ShopDetailActivity.this,list);
                                    gridView.setAdapter(adapter);

                                    setListViewHeightBasedOnChildren(gridView);
                                    adapter.notifyDataSetChanged();

                                    if(shopType.equals("1")){
                                        pop_car_but.setVisibility(View.VISIBLE);
                                        pop_size_price.setText("￥"+shopSizePrice);
                                        shopPrice.setText("￥"+shopSizePrice);
                                    }else{
                                        pop_car_but.setVisibility(View.GONE);
                                        pop_size_price.setText(shopSizeCredit+"积分");
                                        shopPrice.setText(shopSizeCredit+"积分");
                                        pop_buy_but.setBackgroundResource(R.color.shop_buttonBg);
                                        pop_buy_but.setText("积分兑换");
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
                        ToastUtil.show(""+e);
                    }
                });
    }

    public void setListViewHeightBasedOnChildren(GridView listView) {
        DisplayMetrics dm = new DisplayMetrics();
        int heigth = dm.heightPixels;
        int width = dm.widthPixels;
        int popHeight = heigth/2;

        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }
        if (totalHeight<450){
            totalHeight = 450;
        }
        else if(totalHeight > 800){
            totalHeight = 600;
        }
        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
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

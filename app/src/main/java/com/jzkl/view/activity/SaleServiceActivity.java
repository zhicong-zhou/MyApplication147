package com.jzkl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.Charge;
import com.jzkl.Bean.ShopListShop;
import com.jzkl.R;
import com.jzkl.adapter.AgentChargeGridAdapter;
import com.jzkl.adapter.ShopGridAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
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
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class SaleServiceActivity extends BaseActivity {

    String serviceName;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.sale_service_name)
    TextView saleServiceName;
    @BindView(R.id.sale_service_gridview)
    MyGridView sGridview;
    @BindView(R.id.sale_service_scr)
    ScrollView scrollView;


    private String[] shopName = {"电器", "POS机", "箱包", "数码", "其他"};
    private int[] shopImage = {R.mipmap.shop_name_img1, R.mipmap.shop_name_img2, R.mipmap.shop_name_img3, R.mipmap.shop_name_img4, R.mipmap.shop_img};
    private String[] shopPrice = {"30", "30", "30", "30", "30"};
    List<ShopListShop> listName;
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String carShopId;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
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
        return R.layout.activity_sale_service;
    }

    @Override
    protected void initData() {
        serviceName = getIntent().getStringExtra("serviceName");
        commonTitle.setText(serviceName);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saleServiceName.setText(serviceName);

        getDate2();

        sGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopListShop mListShop = (ShopListShop) sGridview.getAdapter().getItem(position);
                Intent intent = new Intent(SaleServiceActivity.this, ShopDetailActivity.class);
                intent.putExtra("shopName", mListShop.getShopName());
                intent.putExtra("shopId", mListShop.getShopId());
                startActivity(intent);
            }
        });
        scrollView.smoothScrollTo(0,0);
    }

    private void getDate2() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        /*1 pos机  短信 0  -1 耗材 */
        Map<String,String> map = new HashMap<>();
        if(serviceName.equals("机具采购")){
            map.put("classifyId", "1");
        }else {
            map.put("classifyId", "-1");
        }
        String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.shop_category_list)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                listName = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    for (int i = 0; i <array.length() ; i++) {
                                        ShopListShop mListname = new ShopListShop();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String goodId = jsonObject1.getString("id");
                                        String shopName = jsonObject1.getString("title");
                                        String price = jsonObject1.getString("price");
                                        String logoUrl = jsonObject1.getString("logoUrl");
                                        String type = jsonObject1.getString("type");
                                        String credit = jsonObject1.getString("credit");

                                        mListname.setShopId(goodId);
                                        mListname.setShopName(shopName);
                                        mListname.setShopPrice(price);
                                        mListname.setShopImage(logoUrl);
                                        mListname.setShopType(type);
                                        mListname.setShopCredit(credit);
                                        listName.add(mListname);
                                    }
                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    carShopId = jsonObject0.getString("id");
                                }else {
                                    ToastUtil.show(msg);
                                }
                                ShopGridAdapter adapter1 = new ShopGridAdapter(SaleServiceActivity.this, listName,0);
                                sGridview.setAdapter(adapter1);
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

//        for (int i = 0; i < shopName.length; i++) {
//            ShopListShop mListname = new ShopListShop();
//            mListname.setShopName(shopName[i]);
//            mListname.setShopPrice(shopPrice[i]);
//            mListname.setShopImage2(shopImage[i]);
//            listName.add(mListname);
//        }
    }
}

package com.jzkl.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.ShopListShop;
import com.jzkl.R;
import com.jzkl.adapter.ShopGridAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.MyGridView;
import com.jzkl.util.RoundImageView;
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
import okhttp3.Call;
import okhttp3.Response;

public class StoreShopActivity extends BaseActivity {

    CustomDialog customDialog;
    String storeId,storeNamess,storeImg,storeDes,storeQQ;
    List<ShopListShop> listName;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.store_headImg)
    ImageViewPlus storeHeadImg;
    @BindView(R.id.store_name)
    TextView storeName;
    @BindView(R.id.store_detail)
    TextView storeDetail;
    @BindView(R.id.store_list)
    MyGridView storeList;
    @BindView(R.id.store_list_no)
    LinearLayout storeListNo;
    @BindView(R.id.store_imgbg)
    ImageView store_imgbg;



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
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_store_shop;
    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("storeId");
        storeNamess = getIntent().getStringExtra("storeName");
        storeImg = getIntent().getStringExtra("storeImg");
        storeDes = getIntent().getStringExtra("storeDes");
        storeQQ = getIntent().getStringExtra("storeQQ");
        commonTitle.setText("店铺详情");
        commonImg.setVisibility(View.VISIBLE);
        commonImg.setImageResource(R.mipmap.shop_detail_service);

        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isQQClientAvailable(StoreShopActivity.this)){
                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin="+storeQQ+"&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                }else{
                    ToastUtil.show("请安装QQ客户端");
                }
            }
        });

        com.jzkl.util.OkHttpUtils.picassoImage(storeImg,this,store_imgbg);
        com.jzkl.util.OkHttpUtils.picassoImage(storeImg,this,storeHeadImg);
        storeName.setText(storeNamess);
        if(storeDes.equals("null")){
            storeDetail.setText("");
        }else {
            storeDetail.setText(storeDes);
        }
        getDate2();

        storeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopListShop mListShop = (ShopListShop) storeList.getAdapter().getItem(position);

                Intent intent = new Intent(StoreShopActivity.this, ShopDetailActivity.class);
                intent.putExtra("shopName", mListShop.getShopName());
                intent.putExtra("shopId", mListShop.getShopId());
                startActivity(intent);
            }
        });
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

    /*列表*/
    private void getDate2() {
        customDialog = new CustomDialog(StoreShopActivity.this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("classifyId", String.valueOf(storeId));

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
                                    if(array.length()==0){
                                        storeListNo.setVisibility(View.VISIBLE);
                                        storeList.setVisibility(View.GONE);
                                    }else {
                                        storeListNo.setVisibility(View.GONE);
                                        storeList.setVisibility(View.VISIBLE);
                                        for (int i = 0; i <array.length() ; i++) {
                                            ShopListShop shopListShop = new ShopListShop();
                                            JSONObject jsonObject1 = (JSONObject) array.get(i);
                                            String goodId = jsonObject1.getString("id");
                                            String shopName = jsonObject1.getString("title");
                                            String logoUrl = jsonObject1.getString("logoUrl");
                                            String price = jsonObject1.getString("price");
                                            String type = jsonObject1.getString("type");
                                            String credit = jsonObject1.getString("credit");

                                            shopListShop.setShopId(goodId);
                                            shopListShop.setShopName(shopName);
                                            shopListShop.setShopImage(logoUrl);
                                            shopListShop.setShopPrice(price);
                                            shopListShop.setShopCredit(credit);
                                            shopListShop.setShopType(type);
                                            listName.add(shopListShop);
                                        }
                                    }
                                }else {
                                    ToastUtil.show(msg);
                                }
                                ShopGridAdapter adapter1 = new ShopGridAdapter(StoreShopActivity.this, listName,1);
                                storeList.setAdapter(adapter1);
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
}

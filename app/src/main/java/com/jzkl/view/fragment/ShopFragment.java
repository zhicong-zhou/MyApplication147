package com.jzkl.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseFragment;
import com.jzkl.Bean.ShopList;
import com.jzkl.Bean.ShopListShop;
import com.jzkl.R;
import com.jzkl.adapter.ShopGridAdapter;
import com.jzkl.adapter.ShopTitleAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.HorizontalListView;
import com.jzkl.util.MyGridView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.CarActivity;
import com.jzkl.view.activity.IntegrteListActivity;
import com.jzkl.view.activity.ShopDetailActivity;
import com.jzkl.view.activity.ShopJiBuyActivity;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

public class ShopFragment extends BaseFragment {


    @BindView(R.id.shop_car)
    ImageView shopCar;
    @BindView(R.id.shop_jifen)
    LinearLayout shopJifen;
    @BindView(R.id.shop_xianshi)
    LinearLayout shopXianshi;
    @BindView(R.id.mRecyclerView)
    HorizontalListView mRecyclerView;
    @BindView(R.id.shop_girdview)
    MyGridView shopGirdview;
    @BindView(R.id.shop_scr)
    ScrollView scrollView;

    @BindView(R.id.shop_list_no)
    LinearLayout shopListNo;


    private String[] shopTitle = {"电器", "POS机", "箱包", "数码", "其他", "电器", "POS机", "箱包", "数码", "其他"};
    List<ShopList> list;
    ShopTitleAdapter adapter;

    private String[] shopName = {"电器", "POS机", "箱包", "数码", "其他"};
    private int[] shopImage = {R.mipmap.shop_name_img1, R.mipmap.shop_name_img2, R.mipmap.shop_name_img3, R.mipmap.shop_name_img4, R.mipmap.shop_img};
    private String[] shopPrice = {"30", "30", "30", "30", "30"};
    List<ShopListShop> listName;
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    int shopId;
    String userinfo,token,credit;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void initData() {

        getUser();
        getDate();
        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                ShopList shopList = (ShopList) mRecyclerView.getAdapter().getItem(position);
                shopId = shopList.getShopId();
                /*列表*/
                getDate2();
            }
        });


        shopGirdview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopListShop mListShop = (ShopListShop) shopGirdview.getAdapter().getItem(position);

                Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
                intent.putExtra("shopName", mListShop.getShopName());
                intent.putExtra("shopId", mListShop.getShopId());
                startActivity(intent);
            }
        });
        /*方法一：解决fragment+scrollview+Recycleview切换fragment自动滚动到底部问题*/
        shopGirdview.setFocusable(false);
    }
    /*分类*/
    private void getDate() {
        OkHttpUtils.post(Webcon.url + Webcon.shop_category)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    for (int i = 0; i <array.length() ; i++) {
                                        ShopList shopList = new ShopList();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String name = jsonObject1.getString("name");
                                        int shopId = jsonObject1.getInt("id");
                                        shopList.setShopTitle(name);
                                        shopList.setShopId(shopId);
                                        list.add(shopList);
                                    }
                                    JSONObject jsonObject0 = (JSONObject) array.get(0);
                                    shopId = jsonObject0.getInt("id");
                                    adapter = new ShopTitleAdapter(getActivity(), list);
                                    mRecyclerView.setAdapter(adapter);
                                    /*默认加载*/
                                    getDate2();
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
//        for (int i = 0; i < shopTitle.length; i++) {
//            ShopList shopList = new ShopList();
//            shopList.setShopTitle(shopTitle[i]);
//            list.add(shopList);
//        }
    }
    /*列表*/
    private void getDate2() {
        customDialog = new CustomDialog(getActivity(),R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("classifyId", String.valueOf(shopId));

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
                                        shopListNo.setVisibility(View.VISIBLE);
                                        shopGirdview.setVisibility(View.GONE);
                                    }else {
                                        shopListNo.setVisibility(View.GONE);
                                        shopGirdview.setVisibility(View.VISIBLE);
                                        for (int i = 0; i <array.length() ; i++) {
                                            ShopListShop shopListShop = new ShopListShop();
                                            JSONObject jsonObject1 = (JSONObject) array.get(i);
                                            String goodId = jsonObject1.getString("id");
                                            String shopName = jsonObject1.getString("title");
                                            String logoUrl = jsonObject1.getString("logoUrl");
                                            String price = jsonObject1.getString("price");
                                            String type = jsonObject1.getString("type");
                                            String credit = jsonObject1.getString("credit");
                                            String remainNumber = jsonObject1.getString("remainNumber");

                                            shopListShop.setShopId(goodId);
                                            shopListShop.setShopName(shopName);
                                            shopListShop.setShopImage(logoUrl);
                                            shopListShop.setShopPrice(price);
                                            shopListShop.setShopCredit(credit);
                                            shopListShop.setShopType(type);
                                            shopListShop.setShopKuCun(remainNumber);
                                            listName.add(shopListShop);
                                        }
                                    }
                                }else {
                                    ToastUtil.show(msg);
                                }
                                ShopGridAdapter adapter1 = new ShopGridAdapter(getActivity(), listName,1);
                                shopGirdview.setAdapter(adapter1);
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

//        for (int i = 0; i < shopName.length; i++) {
//            ShopListShop mListname = new ShopListShop();
//            mListname.setShopName(shopName[i]);
//            mListname.setShopPrice(shopPrice[i]);
//            mListname.setShopImage(shopImage[i]);
//            listName.add(mListname);
//        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            initImmersionBar();
        }
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
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.shop_car, R.id.shop_xianshi, R.id.shop_jifen})
    public void Onclick(View view) {
        switch (view.getId()) {
            /*跳转购物车*/
            case R.id.shop_car:
                Intent intent = new Intent(getActivity(), CarActivity.class);
                startActivity(intent);
                break;
            /*限时抢购*/
            case R.id.shop_xianshi:
//                Intent intent2 = new Intent(getActivity(), ShopJiBuyActivity.class);
//                startActivity(intent2);
                ToastUtil.show("暂未开通");
                break;
            /*积分兑换*/
            case R.id.shop_jifen:
                Intent intent3 = new Intent(getActivity(), IntegrteListActivity.class);
                intent3.putExtra("walletyJifen", credit);
                startActivity(intent3);
                break;
        }
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(getActivity());
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                token = json.getString("token");
                /*用去用户信息*/
                getUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /*用户信息*/
    private void getUserInfo() {
        OkHttpUtils.post(Webcon.url + Webcon.userInfo)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    credit = jsonObject.getString("credit");
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
                    }
                });
    }

}

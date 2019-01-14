package com.jzkl.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.MyListView;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {

    protected ImmersionBar mImmersionBar;
    @BindView(R.id.search_back)
    TextView searchBack;
    @BindView(R.id.search_edit)
    EditText search_edit;
    @BindView(R.id.search_store)
    TextView searchStore;
    @BindView(R.id.search_shop)
    TextView searchShop;
    @BindView(R.id.search_list)
    MyListView searchList;
    @BindView(R.id.search_grid)
    MyGridView search_grid;


    String content;
    CustomDialog customDialog;
    CommonAdapter mAdapter;
    List<PubBean> list;
    String type = "1";

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
        return R.layout.activity_search;
    }

    @OnClick({R.id.search_back, R.id.search_store, R.id.search_shop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_store:
                searchStore.setTextColor(Color.parseColor("#6db0ff"));
                searchShop.setTextColor(Color.parseColor("#333333"));
                type = "1";
                if(content == null){
                    ToastUtil.show("请输入搜索内容");
                    return;
                }else if(content.equals("")){
                    ToastUtil.show("请输入搜索内容");
                    return;
                }
                getData(type);
                break;
            case R.id.search_shop:
                searchStore.setTextColor(Color.parseColor("#333333"));
                searchShop.setTextColor(Color.parseColor("#6db0ff"));
                type = "2";
                if(content == null){
                    ToastUtil.show("请输入搜索内容");
                    return;
                }else if(content.equals("")){
                    ToastUtil.show("请输入搜索内容");
                    return;
                }
                getData(type);
                break;
        }
    }

    @Override
    protected void initData() {
        searchStore.setTextColor(Color.parseColor("#6db0ff"));
        searchShop.setTextColor(Color.parseColor("#333333"));

        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 隐藏键盘
                    ((InputMethodManager) search_edit.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this
                                            .getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    content = search_edit.getText().toString().trim();
                    if(content.equals("")){
                        ToastUtil.show("请输入搜索内容");
                        return false;
                    }
                    getData(type);
                    return true;
                }
                return false;
            }
        });
    }

    private void getData(final String type) {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        /*type 1店铺 2 商品*/
        Map<String,String> map = new HashMap<>();
        map.put("content",content);
        map.put("type",type);
        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.shop_category_search)
                .upJson(json)
                .execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                if(s!=null){
                    try {
                        customDialog.dismiss();
                        JSONObject jsonObject = new JSONObject(s);
                        String code = jsonObject.getString("code");
                        String msg = jsonObject.getString("msg");
                        if(code.equals("0")){
                            list = PubBean.getJsonArr(jsonObject, "list");
                            if(type.equals("1")){
                                searchList.setVisibility(View.VISIBLE);
                                search_grid.setVisibility(View.GONE);
                                searchList.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                        SearchActivity.this, list, R.layout.item_search_store) {
                                    @Override
                                    public void convert(final ViewHolder holder, final PubBean pubBean) {
                                        RelativeLayout searchItem =  holder.getView(R.id.item_search_storell);
                                        ImageView imgbg =  holder.getView(R.id.item_search_imgbg);

                                        holder.setText(R.id.item_search_storeName, pubBean.getName());
                                        holder.setText(R.id.item_search_storeDetail, pubBean.getDescript());
                                        ImageView imageView = holder.getView(R.id.item_search_storeImg);
                                        if(pubBean.getIcon().equals("null")){
                                            imageView.setImageResource(R.mipmap.logo);
                                        }
                                        Glide.with(SearchActivity.this).load(pubBean.getIcon()).into(imageView);
                                        Glide.with(SearchActivity.this).load(pubBean.getIcon()).into(imgbg);

                                        searchItem.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(SearchActivity.this,StoreShopActivity.class);
                                                intent.putExtra("storeId",pubBean.getId());
                                                intent.putExtra("storeName",pubBean.getName());
                                                intent.putExtra("storeImg",pubBean.getIcon());
                                                intent.putExtra("storeDes",pubBean.getDescript());
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }else if(type.equals("2")){
                                searchList.setVisibility(View.GONE);
                                search_grid.setVisibility(View.VISIBLE);
                                search_grid.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                        SearchActivity.this, list, R.layout.item_shop_grid) {
                                    @Override
                                    public void convert(final ViewHolder holder, final PubBean pubBean) {
                                        LinearLayout itme_shop_ll = holder.getView(R.id.itme_shop_ll);
                                        itme_shop_ll.setVisibility(View.GONE);
                                        holder.setText(R.id.item_shop_name, pubBean.getTitle());
                                        holder.setText(R.id.item_shop_price, pubBean.getPrice());
                                        ImageView imageView = holder.getView(R.id.item_shop_img);
                                        if(pubBean.getLogoUrl().equals("null")){
                                            imageView.setImageResource(R.mipmap.logo);
                                        }
                                        Glide.with(SearchActivity.this).load(pubBean.getLogoUrl()).into(imageView);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(SearchActivity.this,ShopDetailActivity.class);
                                                intent.putExtra("shopName", pubBean.getTitle());
                                                intent.putExtra("shopId", pubBean.getId());
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
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
                ToastUtil.show(e + "");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

}

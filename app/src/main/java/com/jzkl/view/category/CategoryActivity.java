package com.jzkl.view.category;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.First;
import com.jzkl.Bean.Second;
import com.jzkl.R;
import com.jzkl.adapter.GridViewAdapter;
import com.jzkl.adapter.ListViewAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.MyListView;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.SearchActivity;
import com.jzkl.view.activity.ShopDetailActivity;
import com.jzkl.view.activity.StoreShopActivity;
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

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.category_back)
    TextView commonBack;
    @BindView(R.id.category_search)
    LinearLayout category_search;

    @BindView(R.id.categoryList)
    ListView categoryList;
    @BindView(R.id.imageView1)
    ImageView imageView;
    @BindView(R.id.girdview)
    GridView girdview;
    @BindView(R.id.category_list_no)
    LinearLayout categoryNo;

    ListViewAdapter listViewAdapter;
    GridViewAdapter gridAdapter;
    CustomDialog customDialog;
    List<First> firstList;
    List<Second> secondList;
    String firstId;
    protected ImmersionBar mImmersionBar;

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
        return R.layout.activity_category;
    }

    @OnClick({R.id.category_back,R.id.category_search})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.category_search:
                Intent intent = new Intent(CategoryActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.category_back:
                finish();
                break;
        }
    }
    @Override
    protected void initData() {

        /*获取一级分类*/
        getFirst();
        getListView();

        /*二级分类跳转商品详情*/
        girdview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Second second = (Second) girdview.getAdapter().getItem(position);
                Intent intent = new Intent(CategoryActivity.this
                        ,StoreShopActivity.class);
                intent.putExtra("storeId",second.getmSecondId());
                intent.putExtra("storeName",second.getmSecondName());
                intent.putExtra("storeImg",second.getmSecondImg());
                intent.putExtra("storeDes",second.getmSecondDetail());
                intent.putExtra("storeQQ",second.getmSecondQQ());
                startActivity(intent);
            }
        });
    }

    private void getFirst() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        String url = Webcon.url + Webcon.shop_category_first;
        OkHttpUtils.post(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            firstList = new ArrayList<>();
                            customDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("msg");
                            if (code.equals("0")) {
                                JSONArray array = jsonObject.getJSONArray("list");
                                for (int i = 0; i < array.length(); i++) {
                                    First first = new First();
                                    JSONObject jsonObject1 = (JSONObject) array.get(i);
                                    String title = jsonObject1.getString("name");
                                    String id = jsonObject1.getString("id");

                                    first.setmFirstName(title);
                                    first.setmFirstId(id);
                                    firstList.add(first);
                                }
                                listViewAdapter = new ListViewAdapter(CategoryActivity.this, firstList);
                                categoryList.setAdapter(listViewAdapter);
                                /*默认加载一个个*/
                                JSONObject jsonObject12 = (JSONObject) array.get(0);
                                getSecond(jsonObject12.getString("id"));
                            } else {
                                ToastUtil.show(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                    }
                });
    }

    private void getSecond(String categoryId) {
        Map<String, String> map = new HashMap<>();
        map.put("topClassId", categoryId);

        String url = Webcon.url + Webcon.shop_category_second;

        String json = new Gson().toJson(map);
        OkHttpUtils.post(url)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            secondList = new ArrayList<>();
                            customDialog.dismiss();
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            String msg = jsonObject.getString("msg");
                            if (code.equals("0")) {
                                JSONArray array = jsonObject.getJSONArray("list");
                                if(array.length()==0){
                                    categoryNo.setVisibility(View.VISIBLE);
                                    girdview.setVisibility(View.GONE);
                                }else {
                                    categoryNo.setVisibility(View.GONE);
                                    girdview.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < array.length(); i++) {
                                        Second second = new Second();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String title = jsonObject1.getString("name");
                                        String img_url = jsonObject1.getString("icon");
                                        String descript = jsonObject1.getString("descript");
                                        String id = jsonObject1.getString("id");
                                        String qq = jsonObject1.getString("qq");

                                        second.setmSecondName(title);
                                        second.setmSecondImg(img_url);
                                        second.setmSecondId(id);
                                        second.setmSecondDetail(descript);
                                        second.setmSecondQQ(qq);
                                        secondList.add(second);
                                    }
                                }
                                gridAdapter = new GridViewAdapter(CategoryActivity.this,secondList);
                                girdview.setAdapter(gridAdapter);
                            } else {
                                ToastUtil.show(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        customDialog.dismiss();
                    }
                });
    }

    private void getListView() {
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                First first = (First) categoryList.getAdapter().getItem(position);
                firstId = first.getmFirstId();
                listViewAdapter.setSelectedPosition(position);
                listViewAdapter.notifyDataSetInvalidated();

                /*二级菜单*/
                getSecond(firstId);
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

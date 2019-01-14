package com.jzkl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.IntergraterH;
import com.jzkl.R;
import com.jzkl.adapter.IntergHListAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 积分兑换
 * */
public class IntegrteListActivity extends BaseActivity {

    @BindView(R.id.integrter_back)
    TextView integrterBack;
    @BindView(R.id.integrter_num)
    TextView integrterNum;
    @BindView(R.id.integr_line)
    ImageView integrLine;
    @BindView(R.id.integrter_mingxi)
    TextView integrterMingxi;
    @BindView(R.id.integrter_list)
    MyListView integrterList;

    List<IntergraterH> list;
    private String intergName[] = {"摩拜单车30天骑行车","摩拜单车10天骑行车","摩拜单车20天骑行车","摩拜单车12天骑行车"};
    private String intergPrice[] = {"199.00","109.00","179.00","169.00"};
    private String intergNum[] = {"7","8","9","10"};
    private int intergImg[] = {R.mipmap.xianshi_img1,R.mipmap.xianshi_img2,R.mipmap.shop_name_img3,R.mipmap.shop_name_img4};
    protected ImmersionBar mImmersionBar;
    String walletyJifen;
    CustomDialog customDialog;

    @Override
    protected void initView() {

    }
    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                .statusBarDarkFont(false, 0.2f)
                .fitsSystemWindows(false)//true是沉浸式有高度  false没有
                .statusBarColor(R.color.transparent)
                .init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @OnClick({R.id.integrter_back,R.id.integrter_mingxi})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.integrter_back:
                finish();
                break;
                /*积分明细*/
            case R.id.integrter_mingxi:
                Intent intent = new Intent(IntegrteListActivity.this,IntegrterMingXActivity.class);
                intent.putExtra("walletyJifen",walletyJifen);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_integrte_list;
    }

    @Override
    protected void initData() {
        walletyJifen = getIntent().getStringExtra("walletyJifen");
        integrterNum.setText(walletyJifen);


        getData();

        integrterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IntergraterH intergraterH = (IntergraterH) parent.getAdapter().getItem(position);
                /*IntegrterDetailActivity*/
                Intent intent = new Intent(IntegrteListActivity.this,ShopDetailActivity.class);
//                intent.putExtra("intergraName",intergraterH.getInterHName());
                intent.putExtra("shopName", intergraterH.getInterHName());
                intent.putExtra("shopId", intergraterH.getInterHId());
                startActivity(intent);
            }
        });
    }
    /*列表*/
    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.shop_Credit_list)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                String code = jsonObject.getString("code");
                                if(code.equals("0")){
                                    JSONArray array = jsonObject.getJSONArray("list");
                                    for (int i = 0; i <array.length() ; i++) {
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        IntergraterH intergraterH = new IntergraterH();
                                        String interId = jsonObject1.getString("id");
                                        String title = jsonObject1.getString("title");
                                        String logoUrl = jsonObject1.getString("logoUrl");
                                        String price = jsonObject1.getString("price");
                                        String credit = jsonObject1.getString("credit");
                                        String type = jsonObject1.getString("type");
                                        String oldDescript = jsonObject1.getString("oldDescript");

                                        intergraterH.setInterHId(interId);
                                        intergraterH.setInterHImg(logoUrl);
                                        intergraterH.setInterHName(title);
                                        intergraterH.setInterHNum(credit);
                                        intergraterH.setInterHPrice(oldDescript);

                                        list.add(intergraterH);
                                    }
                                    IntergHListAdapter adapter = new IntergHListAdapter(IntegrteListActivity.this,list);
                                    integrterList.setAdapter(adapter);
                                }else {
                                    ToastUtil.show("暂无积分商品");
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
}

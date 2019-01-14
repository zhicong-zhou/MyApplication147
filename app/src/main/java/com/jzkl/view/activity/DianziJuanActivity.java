package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.PubBean;
import com.jzkl.Bean.Wallet;
import com.jzkl.R;
import com.jzkl.adapter.WalletListAdapter;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
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

public class DianziJuanActivity extends BaseActivity {

    List<Wallet> list;
    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.wallet_list)
    MyListView walletList;

    @BindView(R.id.fragment_dzj_list_no)
    LinearLayout dzjNo;


    private String walletNmae[] = {"鱼酷100元代金券", "鱼酷180元代金券"};
    private String walletClass[] = {"消费", "兑换"};
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String userinfo, token;

    @Override
    protected void initView() {

    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
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
        return R.layout.activity_dianzi_juan;
    }

    @Override
    protected void initData() {

        commonTitle.setText("电子券");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
        getData();

//        /*电子卷订单*/
//        walletList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Wallet wallet = (Wallet) walletList.getAdapter().getItem(position);
//                Intent intent = new Intent(DianziJuanActivity.this, OrderDetailActivity.class);
//                intent.putExtra("dizId", wallet.getWalletId());
//                intent.putExtra("dizName", wallet.getWalletName());
//                startActivity(intent);
//            }
//        });
    }

    private void getData() {
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();
        Map<String, String> map = new HashMap<>();
        map.put("limit", "10");
        map.put("page", "1");

        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.My_dianzi_list)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    JSONObject pages = jsonObject.getJSONObject("pages");
                                    JSONArray array = pages.getJSONArray("list");
                                    if (array.length() == 0 ) {
                                        dzjNo.setVisibility(View.VISIBLE);
                                        walletList.setVisibility(View.GONE);
                                    } else {
                                        dzjNo.setVisibility(View.GONE);
                                        walletList.setVisibility(View.VISIBLE);

                                        for (int i = 0; i < array.length(); i++) {
                                            Wallet wallet = new Wallet();
                                            JSONObject jsonObject1 = (JSONObject) array.get(i);
                                            String statusStr = jsonObject1.getString("statusStr");
                                            String status = jsonObject1.getString("status");
                                            String amount = jsonObject1.getString("amount");
                                            String id = jsonObject1.getString("id");
                                            String orderNum = jsonObject1.getString("orderNum");
                                            JSONObject discountEntity = jsonObject1.getJSONObject("discountEntity");
                                            String title = discountEntity.getString("title");
                                            String price = discountEntity.getString("price");
                                            String dianziId = discountEntity.getString("id");

                                            wallet.setWalletClass(statusStr);
                                            wallet.setWalletStatus(status);
                                            wallet.setWalletName(title);
                                            wallet.setWalletPirce(price);
                                            wallet.setWalletNum(amount);
                                            wallet.setWalletId(dianziId);
                                            wallet.setWalletNo(orderNum);
                                            list.add(wallet);
                                        }
                                    }
                                } else {
                                    ToastUtil.show("暂无订单");
                                }
                                WalletListAdapter adapter = new WalletListAdapter(DianziJuanActivity.this, list);
                                walletList.setAdapter(adapter);
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

package com.jzkl.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.Address;
import com.jzkl.Bean.EventsAddressInfo;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.R;
import com.jzkl.adapter.AddressListAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.addr_ll)
    LinearLayout addrLl;
    @BindView(R.id.addr_list)
    MyListView addrList;
    @BindView(R.id.addr_add)
    TextView addrAdd;
    @BindView(R.id.address_list_no)
    LinearLayout address_list_no;


    private String serviceTitle;

    private String[] addrName = {"张三", "李四", "旺旺", "张三", "李四", "旺旺"};
    private String[] addrTel = {"158****7844", "158****7849", "158****7854", "158****7844", "158****7849", "158****7854"};
    private String[] addrArea = {"山西省太原市万买士广场", "山西省太原市城风街", "山西省太原市高新区", "山西省太原市万买士广场", "山西省太原市城风街", "山西省太原市高新区"};
    List<Address> list;
    AddressListAdapter adapter;
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo, token, orderAddress, isDefault, status;

    @Override
    protected void initView() {

    }

    /*邀请推广切换*/
    @Subscribe
    public void onEvent(EventsWIFI event) {
        status = event.getWifiStatus();
//        ToastUtil.show(status);
        if (status.equals("isDefault")) {
            customDialog = new CustomDialog(this, R.style.CustomDialog);
            customDialog.show();
            getData();
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

    @OnClick({R.id.common_back, R.id.addr_add})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
            /*添加地址*/
            case R.id.addr_add:
                Intent intent = new Intent(AddressActivity.this, AddressEidtorActivity.class);
                intent.putExtra("addressStauts", "0");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_address;
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        commonTitle.setText("收货地址");
        orderAddress = getIntent().getStringExtra("orderAddress");

        getUser();
        customDialog = new CustomDialog(this, R.style.CustomDialog);
        customDialog.show();

        getData();
        addrList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address mAddress = (Address) addrList.getAdapter().getItem(position);
                String addrTel = mAddress.getAddressTel();
                String addrId = mAddress.getAddressId();
                String addrAear = mAddress.getAddressAear();
                String addrAddress = mAddress.getAddressAddress();
                String addrName = mAddress.getAddressName();
                /*orderAddress 0我的近来的  1 确认订单近来*/
                if (orderAddress.equals("1")) {
                    EventBus.getDefault().post(new EventsAddressInfo(addrId, addrName, addrTel, addrAear, addrAddress));
                    finish();
                } else if (orderAddress.equals("0")) {

                }
            }
        });
    }

    /*地址列表*/
    private void getData() {
        OkHttpUtils.post(Webcon.url + Webcon.address_list)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONArray array = jsonObject1.getJSONArray("addresses");
                                    address_list_no.setVisibility(View.GONE);
                                    addrList.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < array.length(); i++) {
                                        Address mAddress = new Address();
                                        JSONObject jsonObject11 = (JSONObject) array.get(i);
                                        String id = jsonObject11.getString("id");
                                        String createTime = jsonObject11.getString("createTime");
                                        String phone = jsonObject11.getString("phone");
                                        String name = jsonObject11.getString("name");
                                        String address = jsonObject11.getString("address");
                                        String detail = jsonObject11.getString("detail");
                                        isDefault = jsonObject11.getString("isDefault");

                                        mAddress.setAddressName(name);
                                        mAddress.setAddressTel(phone);
                                        mAddress.setAddressAear(address);
                                        mAddress.setAddressAddress(detail);
                                        mAddress.setAddressisDefault(isDefault);
                                        mAddress.setAddressId(id);
                                        list.add(mAddress);
                                    }
                                } else {
                                    address_list_no.setVisibility(View.VISIBLE);
                                    addrList.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter = new AddressListAdapter(AddressActivity.this, list);
                            addrList.setAdapter(adapter);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

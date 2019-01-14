package com.jzkl.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.PayWay;
import com.jzkl.R;
import com.jzkl.adapter.PayWayListAdapter;
import com.jzkl.util.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayWayActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.pay_way_list)
    MyListView payWayList;
    @BindView(R.id.pay_but)
    TextView payBut;

    List<PayWay> list;
    private String [] payTitle = {"支付宝","微信"};
    private int [] payImg = {R.mipmap.pay_alipay,R.mipmap.pay_wechat};
    PayWayListAdapter adapter;
    protected ImmersionBar mImmersionBar;

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

    @OnClick({R.id.common_back,R.id.pay_but})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*支付*/
            case R.id.pay_but:

                break;
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_pay_way;
    }

    @Override
    protected void initData() {
        commonTitle.setText("选择付款方式");

        list = new ArrayList<>();
        getDate();
        adapter = new PayWayListAdapter(this,list);
        payWayList.setAdapter(adapter);

        payWayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);

            }
        });
    }

    private void getDate() {
        for (int i = 0; i <payTitle.length ; i++) {
            PayWay payWay = new PayWay();
            payWay.setPayWayImg(payImg[i]);
            payWay.setPayWayTitle(payTitle[i]);
            list.add(payWay);
        }
    }
}

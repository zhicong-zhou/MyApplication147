package com.jzkl.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.ShopListShop;
import com.jzkl.R;
import com.jzkl.adapter.ShopGridAdapter;
import com.jzkl.util.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
* 代理购买的商品 不用了
* */
public class AgentActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.agent_gridview)
    MyGridView agentGridview;
    @BindView(R.id.agent_scr)
    ScrollView agentScr;

    List<ShopListShop> list;
    private String[] shopName = {"电器", "POS机", "箱包", "数码", "其他"};
    private int[] shopImage = {R.mipmap.shop_name_img1, R.mipmap.shop_name_img2, R.mipmap.shop_name_img3, R.mipmap.shop_name_img4, R.mipmap.shop_img};
    private String[] shopPrice = {"30", "30", "30", "30", "30"};
    private String[] shopType = {"1", "1", "1", "1", "1"};
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

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_agent;
    }

    @Override
    protected void initData() {
        commonTitle.setText("成为代理");

        /*返回*/
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        getDate();
        ShopGridAdapter adapter = new ShopGridAdapter(this, list,0);
        agentGridview.setAdapter(adapter);
        agentGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShopListShop mListShop = (ShopListShop) agentGridview.getAdapter().getItem(position);

                Intent intent = new Intent(AgentActivity.this, ShopDetailActivity.class);
                intent.putExtra("shopName", mListShop.getShopName());
                startActivity(intent);


            }
        });
        agentScr.smoothScrollTo(0,0);
    }

    private void getDate() {
        for (int i = 0; i < shopName.length; i++) {
            ShopListShop mListname = new ShopListShop();
            mListname.setShopName(shopName[i]);
            mListname.setShopPrice(shopPrice[i]);
//            mListname.setShopImage2(shopImage[i]);
            mListname.setShopType(shopType[i]);
            list.add(mListname);
        }
    }

}

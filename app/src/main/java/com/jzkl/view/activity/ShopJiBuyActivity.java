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
import com.jzkl.Bean.ShopJiBuy;
import com.jzkl.R;
import com.jzkl.adapter.ShopJIBuyListAdapter;
import com.jzkl.util.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/*
* 限时购
* */
public class ShopJiBuyActivity extends BaseActivity {


    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.shop_jibuy_time)
    TextView shopJibuyTime;
    @BindView(R.id.shop_jibuy_list)
    MyListView shopJibuyList;
    @BindView(R.id.shop_jibuy_scr)
    ScrollView mScrollView;


    List<ShopJiBuy> list;
    private String jiBuyName[] = {"家庭迷你静音吸湿空气除湿器","家庭迷你静音吸湿空","家庭迷你静音气除湿器","家庭你吸湿空气除湿器"};
    private String jiBuyConetnt[] = {"松下压缩机  打造100%全屋除湿","松下压缩机 "," 打造100%全屋除湿","松下压缩机"};
    private String jiBuyPrice[] = {"199.00","109.00","179.00","169.00"};
    private String jiBuyPriced[] = {"158.90","198.90","198.90","288.90"};
    private String jiBuyNum[] = {"7","8","9","10"};
    private int jiBuyImg[] = {R.mipmap.xianshi_img1,R.mipmap.xianshi_img2,R.mipmap.shop_name_img3,R.mipmap.shop_name_img4};
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
        return R.layout.activity_shop_ji_buy;
    }

    @Override
    protected void initData() {
        commonTitle.setText("限时购");
        /*返回*/
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        getData();
        ShopJIBuyListAdapter  adapter = new ShopJIBuyListAdapter(this,list);
        shopJibuyList.setAdapter(adapter);
        shopJibuyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShopJiBuyActivity.this,ShopDetailActivity.class);
                startActivity(intent);
            }
        });
        mScrollView.smoothScrollTo(0,0);
    }

    private void getData() {
        for (int i = 0; i <jiBuyName.length ; i++) {
            ShopJiBuy shopJiBuy = new ShopJiBuy();

            shopJiBuy.setJIBuyName(jiBuyName[i]);
            shopJiBuy.setJIBuyContent(jiBuyConetnt[i]);
            shopJiBuy.setJIBuyPrice(jiBuyPrice[i]);
            shopJiBuy.setJIBuyPriced(jiBuyPriced[i]);
            shopJiBuy.setJIBuyNum(jiBuyNum[i]);
            shopJiBuy.setJIBuyImg(jiBuyImg[i]);

            list.add(shopJiBuy);
        }
    }

}

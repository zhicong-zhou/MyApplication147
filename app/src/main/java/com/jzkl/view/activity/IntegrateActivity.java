package com.jzkl.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * 积分规则
 * */
public class IntegrateActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.integrte_rule_txt)
    EditText integrteRuleTxt;
    @BindView(R.id.integrte_rule_ll)
    LinearLayout integrteRuleLl;
    private String serviceTitle;
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

    @OnClick(R.id.common_back)
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_integrte;
    }

    @Override
    protected void initData() {
        commonTitle.setText("积分兑换规则");
        integrteRuleTxt.setFocusable(false);
    }
}

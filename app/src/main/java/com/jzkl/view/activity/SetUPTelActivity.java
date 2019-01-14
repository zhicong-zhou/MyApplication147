package com.jzkl.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUPTelActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.set_uptel_old)
    TextView setUptelOld;
    @BindView(R.id.set_uptel_new)
    EditText setUptelNew;
    @BindView(R.id.set_uptel_but)
    Button setUptelBut;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void initView() {

    }
    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
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
        return R.layout.activity_set_up_tel;
    }

    @Override
    protected void initData() {

        commonTitle.setText("修改手机号");
    }

    @OnClick({R.id.common_back,R.id.set_uptel_but})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*修改*/
            case R.id.set_uptel_but:
                finish();
                break;
        }
    }
}

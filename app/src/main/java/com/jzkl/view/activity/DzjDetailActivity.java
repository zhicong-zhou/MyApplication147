package com.jzkl.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzkl.Base.BaseActivity;
import com.jzkl.R;

import butterknife.BindView;

/*
 * 电子卷详情
 * */
public class DzjDetailActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dzj_detail;
    }

    @Override
    protected void initData() {
        commonTitle.setText("电子券详情");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

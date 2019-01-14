package com.jzkl.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.DkApply;
import com.jzkl.R;
import com.jzkl.adapter.DkListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * 贷款申请
 * */
public class DkListApplyActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.dk_apply_list)
    ListView dkApplyList;

    List<DkApply> list;
    private String dkTitle[] = {"卡卡贷-信用贷","卡卡贷-信用贷2","卡卡贷-信用贷3"};
    private String dkMoney[] = {"月供1234.45元","月供234.45元","月供124.45元"};
    private String dkYj[] = {"0.78%","0.68%","0.88%"};
    private int dkImg[] = {R.mipmap.ab,R.mipmap.ab,R.mipmap.ab};

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dk_list_apply;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @Override
    protected void initData() {
        commonTitle.setText("贷款申请");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        getData();
        DkListAdapter adapter = new DkListAdapter(this,list);
        dkApplyList.setAdapter(adapter);
    }

    private void getData() {
        for (int i = 0; i < dkTitle.length; i++) {
            DkApply dkApply = new DkApply();
            dkApply.setDkTitle(dkTitle[i]);
            dkApply.setDkMoney(dkMoney[i]);
            dkApply.setDkYj(dkYj[i]);
            dkApply.setDkImg(dkImg[i]);
            list.add(dkApply);
        }
    }
}

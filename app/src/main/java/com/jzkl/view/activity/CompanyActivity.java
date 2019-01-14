package com.jzkl.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.Company;
import com.jzkl.R;
import com.jzkl.adapter.CompanyListAdapter;
import com.jzkl.util.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * 合作单位
 * */
public class CompanyActivity extends BaseActivity {


    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.company_list)
    MyListView companyList;

    List<Company> list;
    private int [] companyImg = {R.mipmap.company_logo1,R.mipmap.company_logo2,R.mipmap.company_logo3,R.mipmap.company_logo4};
    private String [] companyName = {"张志勇","杨思文","王熙","温栋森"};
    private String [] companyTel = {"13233669111","4006619178","0351-3184779","15735876777"};
    private String [] companyAddr = {"山西省太原市轿车汽配市场。建设路与南内环街十字路口东北角","山西省太原市尖草坪区文兴路汇丰花苑小区临街底商","山西省繁峙县","山西省汾阳市杏花村"};
    private int [] companyDecs = {R.string.company_word1,R.string.company_word,R.string.company_word3,R.string.company_word4};

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
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_company;
    }

    @Override
    protected void initData() {
        commonTitle.setText("合作单位");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<>();
        getData();
        CompanyListAdapter adapter = new CompanyListAdapter(this,list);
        companyList.setAdapter(adapter);
    }

    private void getData() {
        for (int i = 0; i <companyImg.length ; i++) {
            Company company = new Company();
            company.setmCompanyImg(companyImg[i]);
            company.setmCompanyName(companyName[i]);
            company.setmCompanyTel(companyTel[i]);
            company.setmCompanyAear(companyAddr[i]);
            company.setmCompanyDecs(companyDecs[i]);
            list.add(company);
        }
    }

}

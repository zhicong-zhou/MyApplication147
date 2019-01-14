package com.jzkl.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.BankList;
import com.jzkl.R;
import com.jzkl.adapter.SetGuideListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * 新手指引
 * */
public class SetNewGuideActivity extends BaseActivity {


    @BindView(R.id.set_guide_back)
    TextView setGuideBack;
    @BindView(R.id.set_guide_list)
    ListView setGuideList;
    private String guideNew[] = {"如何实现快速刷卡"};
    private String guideNum[] = {"1"};
    List<BankList> list;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_set_new_guide;
    }

    @Override
    protected void initData() {

        setGuideBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList<>();
        getDate();
        SetGuideListAdapter adapter = new SetGuideListAdapter(this,list);
        setGuideList.setAdapter(adapter);
        /*item*/
        setGuideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankList bankList = (BankList) parent.getAdapter().getItem(position);
                Intent intent = new Intent(SetNewGuideActivity.this,SetGuideDeatilActivity.class);
                intent.putExtra("guideId",bankList.getBankName());
                startActivity(intent);
            }
        });
    }

    private void getDate() {
        for (int i = 0; i < guideNew.length; i++) {
            BankList bankList = new BankList();
            bankList.setBankName(guideNew[i]);
            bankList.setBankNo(guideNum[i]);
            list.add(bankList);
        }
    }

}

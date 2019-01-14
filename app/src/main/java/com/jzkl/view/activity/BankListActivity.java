package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.BankList;
import com.jzkl.Bean.EventsSwing;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.adapter.BankAddListAdapter;
import com.jzkl.adapter.BankListAdapter;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
* 银行卡
* */
public class BankListActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.bank_list_no)
    LinearLayout bank_list_no;

    @BindView(R.id.bank_list_add)
    ListView bankListAdd;

    protected ImmersionBar mImmersionBar;
    private String [] bankName = {"晋城银行","中国建设银行","中国平安银行"};
    private int [] bankImg = {R.mipmap.bank_add_list_img,R.mipmap.bank_add_list_img01,R.mipmap.bank_add_list_img02};
    private int [] bankUserImg = {R.mipmap.user_head,R.mipmap.user_head,R.mipmap.user_head};
    private String [] bankClass = {"贷记卡","借记卡","借记卡"};
    private String [] bankCard = {"**** **** **** 8954","**** **** **** 7154","**** **** **** 7954"};
    List<BankList> list;

    CustomDialog customDialog;
    String userinfo,token,bankSelect;
    CommonAdapter mAdapter;

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
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bank_list2;
    }

    @OnClick({R.id.common_back,R.id.common_img})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*添加*/
            case R.id.common_img:
                Intent intent = new Intent(this, BankAddActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void initData() {
        /*bankSelect 0  是刷卡近来的  1 是我的近来的*/
        bankSelect = getIntent().getStringExtra("bankSelect");

        commonTitle.setText("我的银行卡");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commonImg.setVisibility(View.VISIBLE);

        getUser();

        getData();

        bankListAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankList bankList = (BankList) bankListAdd.getAdapter().getItem(position);
                if(bankSelect.equals("0")){
                    EventBus.getDefault().post(new EventsSwing(bankList.getBankNo(),bankList.getBankId()));
                    finish();
                }
            }
        });

    }

    private void getData() {
        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding_list)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject1 = new JSONObject(s);
                                String msg = jsonObject1.getString("msg");
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONArray bankcards = jsonObject1.getJSONArray("bankcards");
                                    if(bankcards.length()==0){
                                        bank_list_no.setVisibility(View.VISIBLE);
                                        bankListAdd.setVisibility(View.GONE);
                                    }else {
                                        bank_list_no.setVisibility(View.GONE);
                                        bankListAdd.setVisibility(View.VISIBLE);

                                        for (int i = 0; i <bankcards.length() ; i++) {
                                            BankList bankList = new BankList();
                                            JSONObject jsonObject11 = (JSONObject) bankcards.get(i);
                                            String bankcardId = jsonObject11.getString("bankcardId");
                                            String bankName = jsonObject11.getString("bankName");
                                            String acctTypeStr = jsonObject11.getString("acctTypeStr");
                                            String cardnoStr = jsonObject11.getString("cardnoStr");
                                            String bankCode = jsonObject11.getString("bankCode");

                                            bankList.setBankId(bankcardId);
                                            bankList.setBankName(bankName);
                                            bankList.setBankClass(acctTypeStr);
                                            bankList.setBankNo(cardnoStr);
                                            bankList.setBankCode(bankCode);
                                            list.add(bankList);
                                        }
                                    }
                                    BankAddListAdapter adapter = new BankAddListAdapter(BankListActivity.this,list);
                                    bankListAdd.setAdapter(adapter);
                                }else {
                                    ToastUtil.show(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        ToastUtil.show(e+"");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            token = json.getString("token");
            String mobile = json.getString("mobile");
            String password = json.getString("password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
        getData();
    }
}

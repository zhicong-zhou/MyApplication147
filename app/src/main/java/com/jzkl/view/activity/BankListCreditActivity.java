package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.BankList;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.adapter.BankListAdapter;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyGridView;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/*
* 信用卡列表
* */
public class BankListCreditActivity extends BaseActivity {


    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.bank_list)
    MyGridView bankList;

    private String []bankId = {"1","2","3","4",};
    private String []bankName = {"中信银行","工行","交通银行","平安银行",};
    private String []bankBut = {"额度高","易下卡","下卡快","当日审批",};
    private int []bankImg = {R.mipmap.abc,R.mipmap.icbc,R.mipmap.psbc,R.mipmap.bohaib};

    List<BankList> list;
    CustomDialog customDialog;
    CommonAdapter mAdapter;

    @Override
    protected void initView() {

    }
    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .init();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_bank_list;
    }

    @Override
    protected void initData() {

        commonTitle.setText("信用卡办理");
        /*返回*/
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();
        bankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BankList bank = (BankList) bankList.getAdapter().getItem(position);
                Intent intent = new Intent(BankListCreditActivity.this, WebviewActivity.class);
                intent.putExtra("webviewTitle", bank.getBankName());
                intent.putExtra("webviewUrl", bank.getBankNo());
                startActivity(intent);
            }
        });
    }

    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("limit","200");
        map.put("page","0");

        final String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.home_xyk_list)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            customDialog.dismiss();
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    JSONObject pages = jsonObject.getJSONObject("pages");
                                    JSONArray array = pages.getJSONArray("list");
                                    for (int i = 0; i <array.length() ; i++) {
                                        BankList bankList = new BankList();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String title = jsonObject1.getString("title");
                                        String tag = jsonObject1.getString("tag");
                                        String logoUrl = jsonObject1.getString("logoUrl");
                                        String goUrl = jsonObject1.getString("goUrl");

                                        bankList.setBankName(title);
                                        bankList.setBankUserImg(logoUrl);
                                        bankList.setBankClass(tag);
                                        bankList.setBankNo(goUrl);

                                        list.add(bankList);
                                    }
                                    BankListAdapter adapter = new BankListAdapter(BankListCreditActivity.this,list);
                                    bankList.setAdapter(adapter);
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
                    }
                });
    }
}

package com.jzkl.view.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.MingXiDetail;
import com.jzkl.R;
import com.jzkl.adapter.MingXiDetailAdapter;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 积分明细  列表
 * */
public class IntegrterMingXActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.integr_mingxi_list)
    MyListView integrMingxiList;
    @BindView(R.id.integrter_mingxi_tatail)
    TextView mTatail;
    @BindView(R.id.integrter_list_no)
    LinearLayout integrter_list_no;


    List<MingXiDetail> list;
    private String mingxiMonth[] = {"8","7"};
    protected ImmersionBar mImmersionBar;
    CustomDialog dialog;
    String userinfo,token;
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
        return R.layout.activity_integrter_ming_x;
    }

    @Override
    protected void initData() {
        commonTitle.setText("积分明细");
        String walletyJifen = getIntent().getStringExtra("walletyJifen");
        mTatail.setText("总积分："+walletyJifen);
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
//        list = new ArrayList<>();
        getData();
//        MingXiMonthAdapter adapter = new MingXiMonthAdapter(this,list);
//        integrMingxiList.setAdapter(adapter);
    }

    private void getData() {
        dialog = new CustomDialog(this,R.style.CustomDialog);
        dialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.wallet_jifen_list)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        dialog.dismiss();
                        if(s!=null){
                            try {
                                list = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    JSONArray array = jsonObject.getJSONArray("messages");
                                    if(array.length()==0){
                                        integrter_list_no.setVisibility(View.VISIBLE);
                                        integrMingxiList.setVisibility(View.GONE);
                                    }else {
                                        integrter_list_no.setVisibility(View.GONE);
                                        integrMingxiList.setVisibility(View.VISIBLE);
                                        for (int i = 0; i <array.length() ; i++) {
                                            MingXiDetail mingXiDetail = new MingXiDetail();
                                            JSONObject jsonObject1 = (JSONObject) array.get(i);
                                            String createTime = jsonObject1.getString("createTime");
                                            String descript = jsonObject1.getString("descript");
                                            String credit = jsonObject1.getString("credit");
                                            mingXiDetail.setDetailTitle(descript);
                                            mingXiDetail.setDetailTime(createTime);
                                            mingXiDetail.setDetailPrice(credit);
                                            list.add(mingXiDetail);
                                        }
                                    }
                                    MingXiDetailAdapter adapter = new MingXiDetailAdapter(IntegrterMingXActivity.this,list);
                                    integrMingxiList.setAdapter(adapter);
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
                        dialog.dismiss();
                    }
                });
//        for (int i = 0; i <mingxiMonth.length ; i++) {
//            MingXi mingXi = new MingXi();
//            mingXi.setMingxiMonth(mingxiMonth[i]);
//            list.add(mingXi);
//        }
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                token = json.getString("token");
                String mobile = json.getString("mobile");
                String password = json.getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

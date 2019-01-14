package com.jzkl.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
* 我的余额
* */
public class MyYueActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.my_yue_ll)
    LinearLayout myYueLl;
    @BindView(R.id.my_yue_price)
    TextView myYuePrice;
    @BindView(R.id.my_yue_llbg)
    LinearLayout myYueLlbg;
    @BindView(R.id.my_yue_list)
    ListView myYueList;
    @BindView(R.id.my_yue_but)
    Button myYueBut;

    List<PubBean> list;
    private String yueTime[] = {"2018-09-24 14:01:45","2018-07-24 14:01:45","2018-08-24 14:01:45","2018-09-24 14:01:45","2018-07-24 14:01:45","2018-08-24 14:01:45","2018-07-24 14:01:45","2018-08-24 14:01:45"};
    private String yueTitle[] = {"9月份分润到账","7月份分润到账","8月份分润到账","9月份分润到账","7月份分润到账","8月份分润到账","7月份分润到账","8月份分润到账"};
    private String yuePrice[] = {"+122.5元","+122.5元","+222.5元","+122.5元","+122.5元","+222.5元","+122.5元","+222.5元"};
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo,token,walletyYue;
    CommonAdapter mAdapter;

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

    @OnClick({R.id.common_back,R.id.my_yue_but})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*确定提现*/
            case R.id.my_yue_but:
                Intent intent = new Intent(this,CashActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_yue;
    }

    @Override
    protected void initData() {
        commonTitle.setText("我的余额");
        walletyYue = getIntent().getStringExtra("walletyYue");
        myYuePrice.setText(walletyYue);
        getUser();
        getData();
    }

    private void getData() {
        customDialog = new  CustomDialog(this,R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.wallet_yue_list)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    list = PubBean.getJsonArr(jsonObject, "messages");
                                    myYueList.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                            MyYueActivity.this, list, R.layout.item_yue_detail) {
                                        @Override
                                        public void convert(final ViewHolder holder, final PubBean pubBean) {
                                            SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            try {
                                                Date date = sdf.parse(pubBean.getCreateTime());
                                                String format = sdf.format(date);
                                                holder.setText(R.id.item_yue_time, format);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            holder.setText(R.id.item_yue_title, pubBean.getDescript());
//                                            holder.setText(R.id.item_yue_time, format);
                                            holder.setText(R.id.item_yue_price, pubBean.getIncome());
                                        }
                                    });
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
                        customDialog.dismiss();
                    }
                });
//        for (int i = 0; i <yueTime.length ; i++) {
//            YueList yueList = new YueList();
//            yueList.setYueMonth(yueTime[i]);
//            yueList.setYuePrice(yuePrice[i]);
//            yueList.setYueTitle(yueTitle[i]);
//            list.add(yueList);
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

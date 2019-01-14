package com.jzkl.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.Wallet;
import com.jzkl.R;
import com.jzkl.adapter.WalletListAdapter;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/*
* 我的钱包
* */
public class WalletActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.wallet_img)
    ImageView walletImg;
    @BindView(R.id.wallet_yue)
    TextView walletYue;
    @BindView(R.id.wallet_yue_ll)
    RelativeLayout walletYueLl;
    @BindView(R.id.wallet_jifne_img)
    ImageView walletJifneImg;
    @BindView(R.id.wallet_jifne)
    TextView walletJifne;
    @BindView(R.id.wallet_jifne_ll)
    RelativeLayout walletJifneLl;

    Intent intent;
    protected ImmersionBar mImmersionBar;
    String userinfo,token,balance,credit;

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

    @OnClick({R.id.common_back,R.id.wallet_yue_ll,R.id.wallet_jifne_ll})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*余额*/
            case R.id.wallet_yue_ll:
                intent = new Intent(this,MyYueActivity.class);
                intent.putExtra("walletyYue",balance);
                startActivity(intent);
                break;
                /*积分*/
            case R.id.wallet_jifne_ll:
                intent = new Intent(this,IntegrteListActivity.class);
                intent.putExtra("walletyJifen",credit);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initData() {
        commonTitle.setText("我的钱包");
        getUser();
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(this);
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                token = json.getString("token");
                /*用去用户信息*/
                getUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*用户信息*/
    private void getUserInfo() {
        OkHttpUtils.post(Webcon.url + Webcon.userInfo)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    balance = jsonObject.getString("balance");
                                    credit = jsonObject.getString("credit");
                                    /*元*/
                                    walletYue.setText(balance+"元");
                                    /*积分*/
                                    walletJifne.setText(credit+"积分");
                                } else {
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
                        ToastUtil.show(""+e);
                    }
                });
    }
}

package com.jzkl.view.activity;

import android.util.Log;
import android.view.View;
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

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/*
 * 我的交易
 * */
public class MyTradeActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.my_trade)
    ListView myTrade;

    @BindView(R.id.trade_list_no)
    LinearLayout trade_list_no;

    List<PubBean> list;
    private String tradeCard[] = {"刷卡-尾号【1245】","刷卡-尾号【1745】","刷卡-尾号【8245】"};
    private String tradeTime[] = {"2018-11-12 15:12:45","2018-09-12 15:12:45","2018-10-12 15:12:45"};
    private String tradeMoney[] = {"66.00","76.00","86.00"};
    private String tradeHua[] = {"10000.00","70000.00","80000.00"};
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String userinfo,token;
    CommonAdapter mAdapter;

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_trade;
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(true)
                .statusBarDarkFont(true, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(android.R.color.white)
                .init();
    }

    @Override
    protected void initData() {
        commonTitle.setText("我的交易");
        commonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getUser();
//        list= new ArrayList<>();
        getData();
//        MyTradeListAdapter adapter = new MyTradeListAdapter(this,list);
//        myTrade.setAdapter(adapter);

    }

    private void getData() {
        customDialog = new CustomDialog(this,R.style.CustomDialog);
        customDialog.show();

        OkHttpUtils.post(Webcon.url2 + Webcon.bank_binding_Record)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            customDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    list = PubBean.getJsonArr(jsonObject, "payRecords");
                                    if(list.size()==0){
                                        trade_list_no.setVisibility(View.VISIBLE);
                                        myTrade.setVisibility(View.GONE);
                                    }else {
                                        trade_list_no.setVisibility(View.GONE);
                                        myTrade.setVisibility(View.VISIBLE);

                                        myTrade.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                                MyTradeActivity.this, list, R.layout.item_trade_list) {
                                            @Override
                                            public void convert(final ViewHolder holder, final PubBean pubBean) {
                                                TextView fail = holder.getView(R.id.item_trade_fail);
                                                holder.setText(R.id.item_trade_card, "刷卡-尾号【" + pubBean.getCreditNoStr() + "】");
                                                holder.setText(R.id.item_trade_money, pubBean.getStatusStr());
                                                holder.setText(R.id.item_trade_hua, "刷卡：" + pubBean.getAmount());
                                                holder.setText(R.id.item_trade_sjdz, "(实际到账：" + pubBean.getIncome() + "+手续费:" + pubBean.getSettfee() +"元+"+ pubBean.getRate() + "元)");
                                                holder.setText(R.id.item_trade_orderNo, "订单号"+pubBean.getOrderid());
                                                holder.setText(R.id.item_trade_time, pubBean.getCreateTime());
                                                if(pubBean.getStatusStr().equals("已完成")){
                                                    fail.setVisibility(View.GONE);
                                                }else {
                                                    fail.setVisibility(View.VISIBLE);
                                                    holder.setText(R.id.item_trade_fail,"失败："+ pubBean.getSubject());
                                                }
                                            }
                                        });
                                    }

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

                    @Override
                    public String parseNetworkResponse(Response response) throws Exception {
                        return super.parseNetworkResponse(response);
                    }
                });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

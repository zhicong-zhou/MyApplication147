package com.jzkl.view.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseFragment;
import com.jzkl.Bean.MyJuan;
import com.jzkl.Bean.MyOrder;
import com.jzkl.Bean.MyService;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.adapter.MyGridAdapter;
import com.jzkl.adapter.MyGridAdapter2;
import com.jzkl.adapter.MyGridAdapter3;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.MyGridView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.AddressActivity;
import com.jzkl.view.activity.BankAddActivity;
import com.jzkl.view.activity.BankListActivity;
import com.jzkl.view.activity.CashActivity;
import com.jzkl.view.activity.CompanyActivity;
import com.jzkl.view.activity.DianziJuanActivity;
import com.jzkl.view.activity.IntegrateActivity;
import com.jzkl.view.activity.IntegrteListActivity;
import com.jzkl.view.activity.LoginActivity;
import com.jzkl.view.activity.MyTradeActivity;
import com.jzkl.view.activity.MyYouHuiActivity;
import com.jzkl.view.activity.OrderListActivity;
import com.jzkl.view.activity.SettingActivity;
import com.jzkl.view.activity.SwingBindingActivity;
import com.jzkl.view.activity.UserInfoActivity;
import com.jzkl.view.activity.WalletActivity;
import com.jzkl.view.activity.WebviewActivity;
import com.jzkl.view.jpush.ExampleUtil;
import com.jzkl.view.jpush.LocalBroadcastManager;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Response;

public class MyFragment extends BaseFragment {
    @BindView(R.id.my_img)
    ImageViewPlus myImg;
    @BindView(R.id.my_name)
    TextView myName;
    @BindView(R.id.my_renz)
    TextView myRenz;
    @BindView(R.id.my_back)
    ImageView myBack;
    @BindView(R.id.my_rl)
    RelativeLayout myRl;
    @BindView(R.id.my_price)
    TextView myPrice;
    @BindView(R.id.my_go_tixian)
    LinearLayout myGoTixian;
    @BindView(R.id.my_integrte)
    TextView myIntegrte;
    @BindView(R.id.my_go_buy)
    LinearLayout myGoBuy;

    @BindView(R.id.my_juan_grid)
    MyGridView myJuanGrid;
    @BindView(R.id.my_order_grid)
    MyGridView myOrderGrid;
    @BindView(R.id.my_service_grid)
    MyGridView myServiceGrid;
    @BindView(R.id.my_order_all)
    RelativeLayout myOrderAll;
    @BindView(R.id.my_wallet)
    TextView myWallet;

    @BindView(R.id.my_price_ll)
    LinearLayout my_price_ll;

    private String[] myOrderName = {"待付款", "待发货", "已发货", "已完成"};
    private int[] myOrderImg = {R.mipmap.my_dpay, R.mipmap.my_dfh, R.mipmap.my_send, R.mipmap.my_finish_pay};
    List<MyOrder> listOrder;

    private String[] myServiceName = {"绑定","银行卡", "我的交易","设置","客服","积分规则" , "收货地址", "合作单位"};
    private int[] myServiceImg = {R.mipmap.my_bd_bank, R.mipmap.my_bank,R.mipmap.my_trade,R.mipmap.my_set,R.mipmap.my_service, R.mipmap.my_integrte_rule, R.mipmap.my_addr, R.mipmap.my_integrte,};
    List<MyService> listService;
    Intent intent;

    private String[] myJuanName = {"电子卡券", "优惠券"};
    private int[] myJuanImg = {R.mipmap.my_juan_dz, R.mipmap.my_juan};
    List<MyJuan> listJuan;
    String userinfo,token,credit,status,userId;
    CustomDialog customDialog;
    public static boolean isForeground = false;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
        }
    }
    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                /*字体颜色默认是白色   写上是深色*/
                .statusBarDarkFont(false, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @OnClick({R.id.my_rl, R.id.my_wallet, R.id.my_go_tixian, R.id.my_go_buy, R.id.my_order_all,R.id.my_price_ll})
    public void Onclick(View view) {
        switch (view.getId()) {
            /*用户信息 登录*/
            case R.id.my_rl:
                if(userinfo!=""){
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            /*我的钱包*/
            case R.id.my_wallet:
                if(userinfo!=""){
                    intent = new Intent(getActivity(), WalletActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
                /*点金额去 钱包*/
            case R.id.my_price_ll:
                if(userinfo!=""){
                    intent = new Intent(getActivity(), WalletActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            /*去提现*/
            case R.id.my_go_tixian:
                if(userinfo!=""){
                    intent = new Intent(getActivity(), CashActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            /*去花费*/
            case R.id.my_go_buy:
                if(userinfo!=""){
                    intent = new Intent(getActivity(), IntegrteListActivity.class);
                    intent.putExtra("walletyJifen", credit);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            /*全部订单*/
            case R.id.my_order_all:
                if(userinfo!=""){
                    intent = new Intent(getActivity(), OrderListActivity.class);
                    intent.putExtra("postion", "1");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void initData() {

        customDialog = new CustomDialog(getActivity(), R.style.CustomDialog);
        customDialog.show();
        getUser();

        listOrder = new ArrayList<>();
        getData();
        MyGridAdapter adapter = new MyGridAdapter(getActivity(), listOrder);
        myOrderGrid.setAdapter(adapter);
        myOrderGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderListActivity.class);
                intent.putExtra("postion", String.valueOf(position+1));
                startActivity(intent);
            }
        });

        listService = new ArrayList<>();
        getData2();
        MyGridAdapter2 adapter2 = new MyGridAdapter2(getActivity(), listService);
        myServiceGrid.setAdapter(adapter2);
        myServiceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyService service = (MyService) parent.getAdapter().getItem(position);
                if (position == 0) {
                    /*刷卡绑定*/
                    Intent intent = new Intent(getActivity(), SwingBindingActivity.class);
                    startActivity(intent);
                }else if(position == 1){
                    if(!status.equals("1")){
                        /*银行卡*/
                        Intent intent = new Intent(getActivity(), BankListActivity.class);
                        intent.putExtra("bankSelect","1");
                        startActivity(intent);
                    }else {
                        /*刷卡绑定*/
                        Intent intent = new Intent(getActivity(), SwingBindingActivity.class);
                        startActivity(intent);
                    }
                }else if(position == 2){
                    /*我的交易*/
                    Intent intent = new Intent(getActivity(), MyTradeActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    /*设置*/
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    intent.putExtra("serviceTitle", service.getMyServiceName());
                    startActivity(intent);
                }else if (position == 4) {
                    /*客服*/
                    PopWin();
                } else if (position == 5) {
                    /*积分规则*/
                    Intent intent = new Intent(getActivity(), IntegrateActivity.class);
                    startActivity(intent);
                } else if (position == 6) {
                    /*我的地址*/
                    if(userinfo!=""){
                        intent = new Intent(getActivity(), AddressActivity.class);
                        intent.putExtra("orderAddress","0");
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }else if (position == 7) {
                    /*合作单位*/
                    Intent intent = new Intent(getActivity(), CompanyActivity.class);
                    startActivity(intent);
                }
            }
        });

        listJuan = new ArrayList<>();
        getData3();
        MyGridAdapter3 adapter3 = new MyGridAdapter3(getActivity(), listJuan);
        myJuanGrid.setAdapter(adapter3);
        myJuanGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    /*电子券*/
                    if(userinfo!=""){
                        intent = new Intent(getActivity(), DianziJuanActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                } else if (position == 1) {
                    /*优惠券*/
                    if(userinfo!=""){
//                        intent = new Intent(getActivity(), MyYouHuiActivity.class);
//                        startActivity(intent);
                        ToastUtil.show("暂未开通");
                    }else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /*用户信息*/
    private void getUserInfo() {
        OkHttpUtils.post(Webcon.url + Webcon.userInfo)
                .headers("token",token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            customDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    String balance = jsonObject.getString("balance");
                                    credit = jsonObject.getString("credit");
                                    JSONObject user = jsonObject.getJSONObject("user");
                                    String uName = user.getString("name");
                                    userId = user.getString("userId");
                                    String mobile = user.getString("mobile");
                                    status = user.getString("status");
                                    boolean flag = user.has("headimgUrl");
                                    String headimgUrl="";
                                    if(flag){
                                        headimgUrl = user.getString("headimgUrl");
                                    }
                                    String statusStr = user.getString("realnameStr");
                                    /*元*/
                                    myPrice.setText(balance);
                                    /*积分*/
                                    myIntegrte.setText(credit);
                                    myName.setText(uName);
                                    myRenz.setVisibility(View.VISIBLE);
                                    myRenz.setText(statusStr);
                                    if(headimgUrl.equals("") || headimgUrl.equals("null")){
                                        myImg.setImageResource(R.mipmap.user_head);
                                    }else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(headimgUrl,getActivity(),myImg);
                                    }
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setUserInfo(getActivity(), s);
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
                        customDialog.dismiss();
                    }
                });
    }

    /*客服*/
    private void PopWin() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_custorm, null);
        TextView dialogText = dialogView.findViewById(R.id.pop_custorm_title);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.pop_custorm_tel);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.pop_custorm_cancel);
        TextView pop_custorm_qq = dialogView.findViewById(R.id.pop_custorm_qq);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
//        layoutDialog.setTitle("提示");
//        layoutDialog.setIcon(R.mipmap.ic_launcher_round);
        /*获取屏幕宽高*/
        dialogBtnConfirm.setText("0351-3339000");
        dialogBtnCancel.setText("取消");

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹窗在底部*/
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);

        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*拨号*/
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ACTION_DIAL 跳转 拨号页面 ACTION_CALL直接拨打*/
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:"+"035714554252");
                intent.setData(data);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        /*取消*/
        dialogBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        /*QQ客服*/
        pop_custorm_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isQQClientAvailable(getActivity())){
                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=1191885480&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                }else{
                    ToastUtil.show("请安装QQ客户端");
                }
            }
        });
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void getData() {
        for (int i = 0; i < myOrderName.length; i++) {
            MyOrder myOrder = new MyOrder();
            myOrder.setMyOrderImg(myOrderImg[i]);
            myOrder.setMyOrderName(myOrderName[i]);
            listOrder.add(myOrder);
        }
    }

    private void getData2() {
        for (int i = 0; i < myServiceName.length; i++) {
            MyService myService = new MyService();
            myService.setMyServiceImg(myServiceImg[i]);
            myService.setMyServiceName(myServiceName[i]);
            listService.add(myService);
        }
    }

    private void getData3() {
        for (int i = 0; i < myJuanName.length; i++) {
            MyJuan myJuan = new MyJuan();
            myJuan.setMyJuanName(myJuanName[i]);
            myJuan.setMyJuanImg(myJuanImg[i]);
            listJuan.add(myJuan);
        }
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(getActivity());
        if(userinfo!=""){
            try {
                JSONObject json = new JSONObject(userinfo);
                token = json.getString("token");
                String mobile = json.getString("mobile");
                String password = json.getString("password");
                /*用去用户信息*/
                getUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            myName.setText("未登录");
            myImg.setImageResource(R.mipmap.user_head);
            myRenz.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        getUser();
    }
}

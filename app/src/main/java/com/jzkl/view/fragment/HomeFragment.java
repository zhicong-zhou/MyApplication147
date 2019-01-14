package com.jzkl.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseFragment;
import com.jzkl.Bean.ADInfo;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.Bean.HomeGrid;
import com.jzkl.Bean.HomeSaleServer;
import com.jzkl.Bean.HomeYHList;
import com.jzkl.Bean.PubBean;
import com.jzkl.R;
import com.jzkl.adapter.HomeGridAdapter;
import com.jzkl.adapter.HomeSaleServerAdapter;
import com.jzkl.adapter.HomeYhListAdapter;
import com.jzkl.util.CommonAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.FadingScrollView;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.MyGridView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.ViewHolder;
import com.jzkl.util.Webcon;
import com.jzkl.util.bann.CycleViewPager;
import com.jzkl.util.bann.ViewFactory;
import com.jzkl.util.gg.LampView;
import com.jzkl.view.activity.AgentAddActivity;
import com.jzkl.view.activity.BankCreditRegisterActivity;
import com.jzkl.view.activity.BusinessRenzActivity;
import com.jzkl.view.activity.DianziJuanOrderActivity;
import com.jzkl.view.activity.HomeAgentActivity;
import com.jzkl.view.activity.HomeDaiKActivity;
import com.jzkl.view.activity.InfoActivity;
import com.jzkl.view.activity.MyYouHuiActivity;
import com.jzkl.view.activity.SaleProblemActivity;
import com.jzkl.view.activity.SaleServiceActivity;
import com.jzkl.view.activity.SetGuideDeatilActivity;
import com.jzkl.view.activity.SetRenZActivity;
import com.jzkl.view.activity.SolicitudeActivity1;
import com.jzkl.view.activity.SwingBindingActivity;
import com.jzkl.view.activity.SwingCardActivity;
import com.jzkl.view.activity.UserInfoActivity;
import com.jzkl.view.activity.WebviewActivity;
import com.jzkl.view.category.CategoryActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {


    @BindView(R.id.home_img)
    ImageViewPlus homeImg;
    @BindView(R.id.homr_name)
    TextView homrName;
    @BindView(R.id.home_renz)
    TextView homeRenz;
    @BindView(R.id.home_info)
    RelativeLayout homeInfo;
    @BindView(R.id.home_info_num)
    TextView home_info_num;

    @BindView(R.id.home_bussnum)
    TextView homeBussnum;
    @BindView(R.id.home_dlnum)
    TextView homeDlnum;
    @BindView(R.id.home_gridview)
    MyGridView homeGridview;
    @BindView(R.id.home_yh_all)
    LinearLayout homeYhAll;
    @BindView(R.id.home_yh_gridview)
    MyGridView homeYhGridview;
    @BindView(R.id.home_yh_gridview2)
    MyGridView homeYhGridview2;
    @BindView(R.id.home_sale_server)
    MyGridView homeSaleServer;

    @BindView(R.id.home_headImg)
    LinearLayout home_headImg;
    @BindView(R.id.nac_image)
    LinearLayout nac_image;
    @BindView(R.id.nac_layout)
    FrameLayout nac_layout;
    @BindView(R.id.mScrollView)
    FadingScrollView mScrollView;


    CycleViewPager cycleViewPager;
    @BindView(R.id.home_bus_ll)
    LinearLayout homeBusLl;
    @BindView(R.id.home_dl_ll)
    LinearLayout homeDlLl;
    @BindView(R.id.home_dianzi_all)
    LinearLayout home_dianzi_all;
    @BindView(R.id.home_scr_info)
    ImageView home_scr_info;
    @BindView(R.id.home_scr_erwerma)
    LinearLayout home_scr_erwerma;
    @BindView(R.id.home_scr_erwerma1)
    LinearLayout home_scr_erwerma1;
    @BindView(R.id.home_info_num2)
    TextView home_info_num2;
    @BindView(R.id.home_gg)
    LampView home_gg;


    private String[] imageUrls = {
            "http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
            "http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
            "http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
            "http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg"};
    private List<ImageView> views = new ArrayList<>();
    private List<ADInfo> infos = new ArrayList<>();

    private String[] mHomeName = {"邀请推广", "贷款", "信用卡办理", "成为代理", "客户关怀", "商户认证", "快速刷卡", "分类"};
    private int[] mHomeImg = {R.mipmap.home_yqtg, R.mipmap.home_dk, R.mipmap.home_xyk, R.mipmap.home_cwdl, R.mipmap.home_cursom_gh, R.mipmap.home_busniess_renz
            , R.mipmap.home_kjsk, R.mipmap.home_more};
    List<HomeGrid> listOrder;

    private int[] mHomeYhImg = {R.mipmap.banner, R.mipmap.banner, R.mipmap.banner};
    List<HomeYHList> homeYhList;

    private int[] mHomeYhGImg = {R.mipmap.banner, R.mipmap.banner, R.mipmap.banner};
    private String[] mHomeYhGTitle = {"88元自助卷", "88元自助卷222", "88元自助卷333"};
    private String[] mHomeYhGName = {"汉山拿1", "汉山拿222", "汉山拿333"};
    List<PubBean> homeYhGrid;

    private int[] mHomeSaleImg = {R.mipmap.home_poble_agent2, R.mipmap.home_poble_jjcg2, R.mipmap.home_poble_lqhc2};
    private String[] mHomeSaleReason = {"故障", "机具采购", "领取耗材"};
    private String[] mHomeSaleContent = {"故障线上查看进度", "POS机", "流量卡，打印机"};
    List<HomeSaleServer> homeServer;
    protected ImmersionBar mImmersionBar;

    CustomDialog customDialog;
    String userinfo, token,messageNum,realnameStr,status;
    CommonAdapter mAdapter;
    HomeYhListAdapter adapter3;
    Intent intent;
    private int REQUEST_CODE_SCAN = 111;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUser();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }

//    @Override
//    protected void initImmersionBar() {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.fullScreen(true)
//                .statusBarDarkFont(true, 0.2f)
//                .fitsSystemWindows(true)
//                .statusBarColor(R.color.transparent)
//                .init();
//    }


    @OnClick({R.id.home_headImg,R.id.home_bus_ll, R.id.home_dl_ll, R.id.home_info, R.id.home_dianzi_all, R.id.home_scr_info, R.id.home_scr_erwerma,R.id.home_scr_erwerma1})
    public void Onclick(View view) {
        switch (view.getId()) {
            /*头像*/
            case R.id.home_headImg:
                intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            /*商户代理*/
            case R.id.home_bus_ll:
                intent = new Intent(getActivity(), AgentAddActivity.class);
                intent.putExtra("addStatus", "1");
                startActivity(intent);
                break;
            /*新增代理*/
            case R.id.home_dl_ll:
                intent = new Intent(getActivity(), AgentAddActivity.class);
                intent.putExtra("addStatus", "0");
                startActivity(intent);
                break;
            /*消息*/
            case R.id.home_info:
                intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);
                break;
            /*电子卷  原来是优惠卷换了*/
            case R.id.home_dianzi_all:
                intent = new Intent(getActivity(), MyYouHuiActivity.class);
                startActivity(intent);
                break;
            /*消息*/
            case R.id.home_scr_info:
                intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);
                break;
                /*上面的二维码扫码*/
            case R.id.home_scr_erwerma1:
                AndPermission.with(this).permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                         * 也可以不传这个参数
                         * 不传的话  默认都为默认不震动  其他都为true
                         * */
                        ZxingConfig config = new ZxingConfig();
                        config.setPlayBeep(true);
                        config.setShake(true);
                        intent.putExtra(com.yzq.zxinglibrary.common.Constant.INTENT_ZXING_CONFIG, config);

                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                    }
                }).start();
                break;
            /*二维码扫描*/
            case R.id.home_scr_erwerma:
                AndPermission.with(this).permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        Intent intent = new Intent(getActivity(), CaptureActivity.class);
                        /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                         * 也可以不传这个参数
                         * 不传的话  默认都为默认不震动  其他都为true
                         * */
                        ZxingConfig config = new ZxingConfig();
                        config.setPlayBeep(true);
                        config.setShake(true);
                        intent.putExtra(com.yzq.zxinglibrary.common.Constant.INTENT_ZXING_CONFIG, config);

                        startActivityForResult(intent, REQUEST_CODE_SCAN);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

                        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);

                        Toast.makeText(getActivity(), "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
                    }
                }).start();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String url = "";
                try {
                    url = data.getStringExtra(com.yzq.zxinglibrary.common.Constant.CODED_CONTENT);
                    /*支付 优惠卷*/
                    confirmErwm(url);
                    Intent intent = new Intent();
                    //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    startActivity(intent);


                } catch (Exception e) {
//                    Toast.makeText(getActivity(), e+"", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    /*扫描成功后调  确认优惠卷*/
    private void confirmErwm(String code) {
        Map<String,String> map = new HashMap<>();
        map.put("discountNum",code);
        final String json = new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.home_txm_confirm)
                .headers("token",token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if(s!=null){
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                int code = jsonObject.getInt("code");
                                if(code == 0){
                                    popWin(msg);
                                }else {
                                    popWin(msg);
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

    private void popWin(String msg) {
        if(msg.equals("success")){
            msg = "成功";
        }
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_intergter, null);
        TextView dialogText = dialogView.findViewById(R.id.dialog_text);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        TextView dialogBtnConfirm = dialogView.findViewById(R.id.dialog_btn_confirm);
        TextView dialogBtnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        dialogContent.setText(msg);
        dialogBtnConfirm.setText("确定");
        dialogBtnCancel.setText("取消");
        /*获取屏幕宽高*/
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*确定*/
        dialogBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
    @Override
    protected void initData() {
        getUser();
        /*轮播*/
        configImageLoader();
        initialize();
        /*九宫格*/
        getGridView();
        /*优惠卷*/
        getYhList();
        /*优惠卷2*/
        getYhGridview();
        /*售后服务*/
        getSaleServer();

        nac_layout.setAlpha(0);
        mScrollView.setFadingView(nac_layout);
        mScrollView.setFadingHeightView(nac_image);
    }

    public void getUser() {
        userinfo = new SharedPreferencesUtil().getToken(getActivity());
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
    }

    /*用户信息*/
    private void getUserInfo() {
        OkHttpUtils.post(Webcon.url + Webcon.userInfo)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != "") {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    String balance = jsonObject.getString("balance");
                                    String credit = jsonObject.getString("credit");
                                    /*红点*/
                                    String unreadnum = jsonObject.getString("unreadnum");
                                    String newRealNameNum = jsonObject.getString("newRealNameNum");
                                    String newMerchantNum = jsonObject.getString("newMerchantNum");
                                    if (unreadnum.equals("0")) {
                                        home_info_num.setVisibility(View.GONE);
                                        home_info_num2.setVisibility(View.GONE);
                                    } else {
                                        home_info_num.setVisibility(View.VISIBLE);
                                        home_info_num2.setVisibility(View.VISIBLE);
                                    }
                                    homeBussnum.setText(newMerchantNum);
                                    homeDlnum.setText(newRealNameNum);

                                    JSONObject user = jsonObject.getJSONObject("user");
                                    String uName = user.getString("name");
                                    messageNum = user.getString("messageNum");
                                    String userId = user.getString("userId");
                                    String mobile = user.getString("mobile");
                                    status = user.getString("status");
                                    boolean flag = user.has("headimgUrl");
                                    String headimgUrl="";
                                    if(flag){
                                        headimgUrl = user.getString("headimgUrl");
                                    }
                                    realnameStr = user.getString("realnameStr");

                                    if (headimgUrl.equals("") || headimgUrl.equals("null")) {
                                        homeImg.setImageResource(R.mipmap.user_head);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(headimgUrl, getActivity(), homeImg);
                                    }
                                    homrName.setText(uName);
                                    homeRenz.setText(realnameStr);
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
                        ToastUtil.show("" + e);
                    }
                });
    }

    private void getSaleServer() {
        homeServer = new ArrayList<>();
        getData4();
        final HomeSaleServerAdapter adapter2 = new HomeSaleServerAdapter(getActivity(), homeServer);
        homeSaleServer.setAdapter(adapter2);
        homeSaleServer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeSaleServer saleServer = (HomeSaleServer) adapter2.getItem(position);
                if (position == 0) {
                    /*故障*/
                    Intent intent = new Intent(getActivity(), SaleProblemActivity.class);
                    intent.putExtra("serviceName", saleServer.getHomeSaleReason());
                    startActivity(intent);
                } else if (position == 1) {
                    /*机具采购*/
                    Intent intent = new Intent(getActivity(), SaleServiceActivity.class);
                    intent.putExtra("serviceName", saleServer.getHomeSaleReason());
                    startActivity(intent);
                } else if (position == 2) {
                    /*耗材领取*/
                    Intent intent = new Intent(getActivity(), SaleServiceActivity.class);
                    intent.putExtra("serviceName", saleServer.getHomeSaleReason());
                    startActivity(intent);
                }
            }
        });
    }

    private void getData4() {
        for (int i = 0; i < mHomeSaleImg.length; i++) {
            HomeSaleServer saleServer = new HomeSaleServer();
            saleServer.setHomeSaleImg(mHomeSaleImg[i]);
            saleServer.setHomeSaleReason(mHomeSaleReason[i]);
            saleServer.setHomeSaleContent(mHomeSaleContent[i]);
            homeServer.add(saleServer);
        }
    }

    /*优惠卷2*/
    private void getYhGridview() {
        getData3();
        homeYhGridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PubBean pubBean = (PubBean) homeYhGridview2.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), DianziJuanOrderActivity.class);
                intent.putExtra("yhjId", pubBean.getId());
                intent.putExtra("yhjName", pubBean.getTitle());
                startActivity(intent);
            }
        });
    }

    private void getData3() {
        Map<String, String> map = new HashMap<>();
        map.put("limit", "4");
        map.put("page", "1");

        String json = new Gson().toJson(map);

        OkHttpUtils.post(Webcon.url + Webcon.My_reduction_list)
                .headers("token", token)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(s);
                                int code = jsonObject1.getInt("code");
                                if (code == 0) {
                                    JSONObject jsonObject = jsonObject1.getJSONObject("pages");
                                    homeYhGrid = PubBean.getJsonArr(jsonObject, "list");
                                    homeYhGridview2.setAdapter(mAdapter = new CommonAdapter<PubBean>(
                                            getActivity(), homeYhGrid, R.layout.item_home_yh_grid) {
                                        @Override
                                        public void convert(final ViewHolder holder, final PubBean pubBean) {
                                            holder.setText(R.id.item_home_gtitle, pubBean.getTitle());
                                            holder.setText(R.id.item_home_gname, pubBean.getDescript());
                                            ImageView imageView = holder.getView(R.id.item_home_gimg);
                                            Glide.with(getActivity()).load(pubBean.getLogoUrl()).into(imageView);
                                        }
                                    });
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

    /*电子券 全部好礼那的3个*/
    private void getYhList() {
        getData2();
        homeYhGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeYHList homeYHList = (HomeYHList) homeYhGridview.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), DianziJuanOrderActivity.class);
                intent.putExtra("yhjId", homeYHList.getHomeYhId());
                intent.putExtra("yhjName", homeYHList.getHomeYhTitle());
                startActivity(intent);
            }
        });
    }

    /*电子券 全部好礼那的3个*/
    private void getData2() {
        customDialog = new CustomDialog(getActivity(), R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.home_dianzi_list)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                homeYhList = new ArrayList<>();
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                if (code == 0) {
                                    JSONArray array = jsonObject.getJSONArray("discounts");
                                    int as = 0;
                                    if(array.length() >= 3){
                                        as = 3;
                                    }else if(array.length() < 3){
                                        as = array.length();
                                    }else if(array.length() == 0){
                                        return;
                                    }
                                    for (int i = 0; i < as; i++) {
                                        HomeYHList yhList = new HomeYHList();
                                        JSONObject jsonObject1 = (JSONObject) array.get(i);
                                        String logoUrl = jsonObject1.getString("logoUrl");
                                        String id = jsonObject1.getString("id");
                                        String title = jsonObject1.getString("title");
                                        String descript = jsonObject1.getString("descript");

                                        yhList.setHomeYhId(id);
                                        yhList.setHomeYhImg(logoUrl);
                                        yhList.setHomeYhTitle(title);
                                        yhList.setHomeYhDesc(descript);
                                        homeYhList.add(yhList);
                                    }
                                    adapter3 = new HomeYhListAdapter(getActivity(), homeYhList);
                                    homeYhGridview.setAdapter(adapter3);
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
    /*九宫格*/
    private void getGridView() {
        getData();
        homeGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*邀请推广*/
                if (position == 0) {
                    EventBus.getDefault().post(new EventsWIFI("1"));
                } else if (position == 1) {
                    /*贷款*/
                    intent = new Intent(getActivity(), HomeDaiKActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    /*信用卡办理*/
                    Intent intent = new Intent(getActivity(), BankCreditRegisterActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    /*成为代理*/
                    Intent intent = new Intent(getActivity(), HomeAgentActivity.class);
                    startActivity(intent);
                } else if (position == 4) {
                    /*客户关怀*/
                    Intent intent = new Intent(getActivity(), SolicitudeActivity1.class);
                    intent.putExtra("messageNum",messageNum);
                    startActivity(intent);
                } else if (position == 5) {
                    /*商家认证*/
                    Intent intent = new Intent(getActivity(), BusinessRenzActivity.class);
                    startActivity(intent);
                } else if (position == 6) {
                    /*快速刷卡*/
                    if(realnameStr.equals("待认证")){
                        intent = new Intent(getActivity(), SetRenZActivity.class);
                        startActivity(intent);
                    }else {
                        if(!status.equals("1")){
                            Intent intent = new Intent(getActivity(), SwingCardActivity.class);
                            startActivity(intent);
                        }else {
                            /*刷卡绑定*/
                            Intent intent = new Intent(getActivity(), SwingBindingActivity.class);
                            startActivity(intent);
                        }
                    }
                } else if (position == 7) {
//                    ToastUtil.show("敬请期待");
                    /*更多*/
                    Intent intent = new Intent(getActivity(), CategoryActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    /*九宫格*/
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "11");
        String json =  new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.home_banner)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {
                            try {
                                listOrder = new ArrayList<>();
                                JSONObject json = new JSONObject(s);
                                int code = json.getInt("code");
                                if (code == 0) {
                                    JSONArray jsonArray = json.getJSONArray("icon");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        HomeGrid homeGrid = new HomeGrid();
                                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                        String logoUrl = jsonObject.getString("logoUrl");
                                        String descript = jsonObject.getString("descript");
                                        homeGrid.setMyOrderImg(logoUrl);
                                        homeGrid.setMyOrderName(descript);
                                        listOrder.add(homeGrid);
                                    }
                                }
                                HomeGridAdapter adapter4 = new HomeGridAdapter(getActivity(), listOrder);
                                homeGridview.setAdapter(adapter4);
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
//        for (int i = 0; i < mHomeName.length; i++) {
//            MyOrder myOrder = new MyOrder();
//            myOrder.setMyOrderImg(mHomeImg[i]);
//            myOrder.setMyOrderName(mHomeName[i]);
//            listOrder.add(myOrder);
//        }
    }

    @SuppressLint("NewApi")
    private void initialize() {
        /*轮播*/
        getBann();
        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
                .findFragmentById(R.id.home_fragment_cycle1);
    }

    /*获取轮播*/
    private void getBann() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "2");
        String json =  new Gson().toJson(map);
        OkHttpUtils.post(Webcon.url + Webcon.home_banner)
                .upJson(json)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s != null) {
                            try {
                                JSONObject json = new JSONObject(s);
                                int code = json.getInt("code");
                                if (code == 0) {
                                    JSONArray jsonArray = json.getJSONArray("icon");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonImg = (JSONObject) jsonArray.get(i);
                                        String img_url = jsonImg.getString("logoUrl");
                                        String goUrl = jsonImg.getString("goUrl");
                                        String descript = jsonImg.getString("descript");
                                        ADInfo info = new ADInfo();
                                        info.setUrl(img_url);
                                        info.setContent(goUrl);
                                        info.setType(descript);
                                        infos.add(info);
                                    }
                                    if (infos.size() > 0) {
                                        // 将最后一个ImageView添加进来
                                        views.add(ViewFactory.getImageView(getActivity(), infos.get(infos.size() - 1).getUrl()));
                                        for (int i = 0; i < infos.size(); i++) {
                                            views.add(ViewFactory.getImageView(getActivity(), infos.get(i).getUrl()));
                                        }
                                        // 将第一个ImageView添加进来
                                        views.add(ViewFactory.getImageView(getActivity(), infos.get(0).getUrl()));

                                        // 设置循环，在调用setData方法前调用
                                        cycleViewPager.setCycle(true);

                                        // 在加载数据前设置是否循环
                                        cycleViewPager.setData(views, infos, mAdCycleViewListener);
                                        //设置轮播
                                        cycleViewPager.setWheel(true);

                                        // 设置轮播时间，默认5000ms
                                        cycleViewPager.setTime(2000);
                                        //设置圆点指示图标组居中显示，默认靠右
                                        cycleViewPager.setIndicatorCenter();
                                    }
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

    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            if (cycleViewPager.isCycle()) {
                if(info.getType().equals("app")){
                    Intent intent = new Intent(getActivity(), SetGuideDeatilActivity.class);
                    intent.putExtra("guideId","新手指引");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), WebviewActivity.class);
                    intent.putExtra("bannUrl", info.getContent());
                    intent.putExtra("webviewTitle", "轮播");
                    startActivity(intent);
                }
            }
        }
    };

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.banner) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.banner) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.banner) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

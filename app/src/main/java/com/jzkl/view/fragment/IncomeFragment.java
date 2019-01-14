package com.jzkl.view.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseFragment;
import com.jzkl.Bean.IncomeName;
import com.jzkl.Bean.IncomeName2;
import com.jzkl.Constants;
import com.jzkl.R;
import com.jzkl.ShareUtil;
import com.jzkl.adapter.IncomeShAdapter;
import com.jzkl.util.CustomDialog;
import com.jzkl.util.ImageUtil;
import com.jzkl.util.ImageViewPlus;
import com.jzkl.util.MyListView;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.ToastUtil;
import com.jzkl.util.Webcon;
import com.jzkl.view.activity.AgentAddActivity;
import com.jzkl.view.activity.InfoActivity;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;
import it.sephiroth.android.library.picasso.Target;
import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.VIBRATOR_SERVICE;

/*
 * 推广
 * */
public class IncomeFragment extends BaseFragment {

    @BindView(R.id.income_ll)
    LinearLayout incomeLl;
    @BindView(R.id.income_txt)
    TextView incomeTxt;
    @BindView(R.id.income_rl)
    RelativeLayout incomeRl;
    @BindView(R.id.income_img)
    ImageViewPlus incomeImg;
    @BindView(R.id.income_name)
    TextView incomeName;
    @BindView(R.id.income_renz)
    TextView incomeRenz;
    @BindView(R.id.income_info)
    RelativeLayout incomeInfo;

    @BindView(R.id.income_info_num)
    TextView income_info_num;

    @BindView(R.id.qr_code)
    LinearLayout qr_code;
    @BindView(R.id.qr_code_img)
    ImageView qr_code_img;

    @BindView(R.id.income_scr)
    ScrollView scrollView;

    @BindView(R.id.qr_code_more)
    RelativeLayout qr_code_more;
    @BindView(R.id.qr_code_more2)
    RelativeLayout qr_code_more2;
    @BindView(R.id.income_dl_list)
    MyListView dlList;
    @BindView(R.id.income_sh_list)
    MyListView shList;

    List<IncomeName> list;
    List<IncomeName2> list2;
    protected ImmersionBar mImmersionBar;
    CustomDialog customDialog;
    String userToken, token, mobile, userId, tjName, erUrl;
    JSONArray agencys, merchants;
    Intent intent;

    private View popView;
    private PopupWindow popupWindow;
    IncomeShAdapter adapter;
    Vibrator vibrator;
    private IWXAPI api;
    private static final int THUMB_SIZE = 150;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    PhotoView photoView;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_income;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getUser();
            initImmersionBar();
            getUserTj();
        }
    }

    @Override
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.fullScreen(false)
                .statusBarDarkFont(false, 0.2f)
                .fitsSystemWindows(false)
                .statusBarColor(R.color.transparent)
                .init();
    }

    @OnClick({R.id.income_info,R.id.qr_code_more, R.id.qr_code_more2})
    public void Onclick(View view) {
        switch (view.getId()) {
            /*消息*/
            case R.id.income_info:
                intent = new Intent(getActivity(), InfoActivity.class);
                startActivity(intent);
                break;
            /*二维码分享*/
//            case R.id.qr_code:
//                popShare();
//                break;
            /*新增代理*/
            case R.id.qr_code_more:
                intent = new Intent(getActivity(), AgentAddActivity.class);
                intent.putExtra("addStatus", "0");
                startActivity(intent);
                break;
            /*新增商户*/
            case R.id.qr_code_more2:
                intent = new Intent(getActivity(), AgentAddActivity.class);
                intent.putExtra("addStatus", "1");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void initData() {
        api = WXAPIFactory.createWXAPI(getActivity(), Constants.APP_ID);
        getUser();
        getUserTj();
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        /*二维码分享*/
        qr_code_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popShare();
            }
        });
        /*长按*/
        qr_code_img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AndPermission.with(getActivity()).permission(Manifest.permission.WRITE_EXTERNAL_STORAGE).callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        vibrator.vibrate(30);
                        popMaxImg();
                    }
                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        Toast.makeText(getActivity(), "没有权限", Toast.LENGTH_LONG).show();
                    }
                }).start();
                return true;
            }
        });
    }
    /*推荐人信息*/
    private void getUserTj() {
        customDialog = new CustomDialog(getActivity(), R.style.CustomDialog);
        customDialog.show();
        OkHttpUtils.post(Webcon.url + Webcon.incomeUserInfo)
                .headers("token", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        customDialog.dismiss();
                        if (s != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                int code = jsonObject.getInt("code");
                                String msg = jsonObject.getString("msg");
                                if (code == 0) {
                                    String loaderName = jsonObject.getString("loaderName");
                                    String loaderPhone = jsonObject.getString("loaderPhone");

                                    agencys = jsonObject.getJSONArray("agencys");
                                    merchants = jsonObject.getJSONArray("merchants");

                                    JSONObject user = jsonObject.getJSONObject("user");
                                    String name = user.getString("name");
                                    String mobile = user.getString("mobile");
                                    String headimgUrl = user.getString("headimgUrl");
                                    String statusStr = user.getString("realnameStr");

                                    list = new ArrayList<>();
                                    /*新增的用户*/
                                    getDateDl();
                                    getDateSh();
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

    /*新增代理*/
    private void getDateDl() {
        try {
            list = new ArrayList<>();
            for (int i = 0; i < agencys.length(); i++) {
                IncomeName incomeName = new IncomeName();
                JSONObject jsonObject1 = (JSONObject) agencys.get(i);
                String name = jsonObject1.getString("name");
                String mobile = jsonObject1.getString("mobile");
                String createTime = jsonObject1.getString("createTime");
                String headimgUrl = jsonObject1.getString("headimgUrl");
                String realnameStr = jsonObject1.getString("realnameStr");

                incomeName.setIncomeName(name);
                incomeName.setIncomeRenz(realnameStr);

                list.add(incomeName);
            }

            adapter = new IncomeShAdapter(getActivity(), list);
            dlList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*新增商户*/
    private void getDateSh() {
        try {
            list = new ArrayList<>();
            for (int i = 0; i < merchants.length(); i++) {
                IncomeName incomeName = new IncomeName();
                JSONObject jsonObject1 = (JSONObject) merchants.get(i);
                String name = jsonObject1.getString("name");
                String mobile = jsonObject1.getString("mobile");
                String createTime = jsonObject1.getString("createTime");
                String headimgUrl = jsonObject1.getString("headimgUrl");
                String realnameStr = jsonObject1.getString("realnameStr");

                incomeName.setIncomeName(name);
                incomeName.setIncomeRenz(realnameStr);

                list.add(incomeName);
            }

            adapter = new IncomeShAdapter(getActivity(), list);
            shList.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*token*/
    public void getUser() {
        userToken = new SharedPreferencesUtil().getToken(getActivity());
        try {
            JSONObject json = new JSONObject(userToken);
            token = json.getString("token");
            mobile = json.getString("mobile");
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
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                                    userId = jsonObject1.getString("userId");
                                    tjName = jsonObject1.getString("name");
                                    boolean flag = jsonObject1.has("headimgUrl");
                                    String headimgUrl = "";
                                    if (flag) {
                                        headimgUrl = jsonObject1.getString("headimgUrl");
                                    }
                                    String statusStr = jsonObject1.getString("realnameStr");

                                    incomeName.setText(tjName);
                                    incomeRenz.setText(statusStr);

                                    if (headimgUrl.equals("null") || headimgUrl.equals("")) {
                                        incomeImg.setImageResource(R.mipmap.user_head);
                                    } else {
                                        com.jzkl.util.OkHttpUtils.picassoImage(headimgUrl, getActivity(), incomeImg);
                                    }

                                    /*红点*/
                                    String unreadnum = jsonObject.getString("unreadnum");
                                    if (unreadnum.equals("0")) {
                                        income_info_num.setVisibility(View.GONE);
                                    } else {
                                        income_info_num.setVisibility(View.VISIBLE);
                                    }
                                    /*保存用户信息*/
                                    new SharedPreferencesUtil().setUserInfo(getActivity(), s);
                                    /*二维码图片*/
                                    erUrl = Webcon.url + Webcon.er_coed + "userId=" + userId;
//                                    Glide.with(getActivity()).load(erUrl).into(qr_code_img);
                                    com.jzkl.util.OkHttpUtils.picassoImage(erUrl, getActivity(), qr_code_img);
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
                    }
                });
    }


    protected void initViewInfo() {
        WXWebpageObject webpage = new WXWebpageObject();
        /*https://www.pgyer.com/TN9z*/
        webpage.webpageUrl = Webcon.share + "?loeadrName=" + tjName + "&loeadrMobile=" + mobile;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "德晋";
        msg.description = "app分享地址";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = ShareUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /*长按后的大图*/
    @SuppressLint("ClickableViewAccessibility")
    private void popMaxImg() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_zoom_img, null);
        photoView = dialogView.findViewById(R.id.pop_code_img);

        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.5;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹窗在底部*/
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, (int)height);

        com.jzkl.util.OkHttpUtils.picassoImage(erUrl, getActivity(), photoView);
        photoView.enable();

        /*保存图片*/
        getSave();
    }

    private void getSave() {
        /*string 转bitmap*/
        Picasso.with(getActivity()).load(erUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                /*保存图片*/
                Bitmap bitmap1 = ImageUtil.drawTextToRightBottom(getActivity(),bitmap,tjName+"  ID:"+userId,4, Color.parseColor("#2D77FF"),10,2);
                ImageUtil.saveToSystemGallery(getActivity(),bitmap1);
                ToastUtil.show("保存成功");
            }
            @Override
            public void onBitmapFailed(Drawable drawable) {
            }
            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        });
    }


    /*分享*/
    @SuppressLint("ClickableViewAccessibility")
    private void popShare() {
        popView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_share, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);

        LinearLayout sharell = (LinearLayout) popView.findViewById(R.id.pop_sharell);
        TextView cancel = (TextView) popView.findViewById(R.id.pop_share_cancel);
        LinearLayout friend = (LinearLayout) popView.findViewById(R.id.pop_share_friend);
        LinearLayout wechat = (LinearLayout) popView.findViewById(R.id.pop_share_wechat);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        /*点外部关闭弹框*/
        sharell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return true;
            }
        });
        /*朋友圈*/
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                initViewInfo();
            }
        });
        /*分享给朋友*/
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                mTargetScene = SendMessageToWX.Req.WXSceneSession;
                initViewInfo();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}

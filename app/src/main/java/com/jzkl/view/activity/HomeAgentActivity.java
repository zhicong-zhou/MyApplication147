package com.jzkl.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.Constants;
import com.jzkl.R;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.Webcon;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 成为代理
 * */
public class HomeAgentActivity extends BaseActivity {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.agent_share_img)
    ImageView agentShareImg;
    @BindView(R.id.agent_share)
    TextView agentShare;
    @BindView(R.id.agent_share1)
    TextView agent_share1;

    @BindView(R.id.agent_recharge_img)
    ImageView agentRechargeImg;
    @BindView(R.id.agent_recharge)
    TextView agentRecharge;
    @BindView(R.id.agent_recharge_txt)
    TextView agent_recharge_txt;
    @BindView(R.id.agent_tuig_img)
    ImageView agentTuigImg;
    @BindView(R.id.agent_tuig)
    TextView agentTuig;
    @BindView(R.id.agent_tuig_txt)
    TextView agent_tuig_txt;


    protected ImmersionBar mImmersionBar;
    Intent intent;
    private View popView;
    private PopupWindow popupWindow;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private static final int THUMB_SIZE = 150;
    private IWXAPI api;
    String userinfo,userId,name,mobile;

    @Override
    protected void initView() {
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_agent2;
    }

    @OnClick({R.id.common_back,R.id.agent_share,R.id.agent_share1,R.id.agent_recharge,R.id.agent_recharge_txt,R.id.agent_tuig,R.id.agent_tuig_txt})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.common_back:
                finish();
                break;
                /*黄金 分享*/
            case R.id.agent_share1:
                popShare();
                break;
                /*黄金 支付金额*/
            case R.id.agent_share:
                intent = new Intent(this,AgentRechargeActivity.class);
                intent.putExtra("rechargeType","1");
                startActivity(intent);
                break;
                /*白金 推荐*/
            case R.id.agent_recharge_txt:
                popShare();
                break;
                /*白金 支付*/
            case R.id.agent_recharge:
                intent = new Intent(this,AgentRechargeActivity.class);
                intent.putExtra("rechargeType","2");
                startActivity(intent);
                break;
                /*钻石 推荐*/
            case R.id.agent_tuig_txt:
                popShare();
                break;
                /*钻石 金额*/
            case R.id.agent_tuig:
                intent = new Intent(this,AgentRechargeActivity.class);
                intent.putExtra("rechargeType","3");
                startActivity(intent);
                break;
        }
    }

    /*分享*/
    @SuppressLint("ClickableViewAccessibility")
    private void popShare() {
        popView = LayoutInflater.from(this).inflate(R.layout.pop_share, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAtLocation(popView, Gravity.BOTTOM, 0, 0);

        LinearLayout sharell = popView.findViewById(R.id.pop_sharell);
        TextView cancel =  popView.findViewById(R.id.pop_share_cancel);
        LinearLayout friend =  popView.findViewById(R.id.pop_share_friend);
        LinearLayout wechat =  popView.findViewById(R.id.pop_share_wechat);

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
                mTargetScene = SendMessageToWX.Req.WXSceneTimeline;
                initViewInfo();
                popupWindow.dismiss();
            }
        });
        /*分享给朋友*/
        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTargetScene = SendMessageToWX.Req.WXSceneSession;
                initViewInfo();
                popupWindow.dismiss();
            }
        });
    }

    protected void initViewInfo() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Webcon.share + "?loeadrName="+ name +"&loeadrMobile=" + mobile;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "德晋";
        msg.description = "app分享地址";
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
//        msg.thumbData = ShareUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
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
    protected void initData() {
        commonTitle.setText("成为代理");
        getUserInfo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
    /*用户信息*/
    public void getUserInfo() {
        userinfo = new SharedPreferencesUtil().getUserInfo(this);
        try {
            JSONObject json = new JSONObject(userinfo);
            JSONObject user = json.getJSONObject("user");
            userId = user.getString("userId");
            name = user.getString("name");
            mobile = user.getString("mobile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

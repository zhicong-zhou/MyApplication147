package com.jzkl.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Base.BaseActivity;
import com.jzkl.MainActivity;
import com.jzkl.R;
import com.jzkl.util.SharedPreferencesUtil;
import com.leo.library.bean.PointState;
import com.leo.library.view.GestureContentView;
import com.leo.library.view.IGesturePwdCallBack;
import com.leo.library.view.IndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.baidu.navisdk.jni.nativeif.JNISearchConst.KEY_NAME;

/*
 * 修改手势登录
 * */
public class LoginShouSureActivity extends BaseActivity implements IGesturePwdCallBack {

    @BindView(R.id.common_back)
    TextView commonBack;
    @BindView(R.id.common_title)
    TextView commonTitle;
    @BindView(R.id.common_img)
    ImageView commonImg;
    @BindView(R.id.id_indicator_view)
    IndicatorView indicatorView;
    @BindView(R.id.id_indicator)
    TextView idIndicator;
    @BindView(R.id.id_gesture_pwd)
    GestureContentView idGesturePwd;
    @BindView(R.id.login_ss_forget)
    TextView loginSsForget;
    @BindView(R.id.login_ss_update)
    TextView loginSsUpdate;

    @BindView(R.id.login_ss_ll)
    RelativeLayout login_ss_ll;

    private int count = 0;
    private String pwd, loginPwd, come;
    protected ImmersionBar mImmersionBar;
    Intent intent;

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

    @OnClick({R.id.common_back, R.id.login_ss_forget, R.id.login_ss_update})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.common_back:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login_shou;
    }

    @Override
    protected void initData() {
        login_ss_ll.setVisibility(View.GONE);
        commonTitle.setText("修改登录手势");
        /*获取登录手势密码*/
        loginPwd = new SharedPreferencesUtil().getLoginPwd(this);
        /*手势事件*/
        idGesturePwd.setGesturePwdCallBack(this);
    }

    @Override
    public void callBack(List<Integer> pwds) {
        if (pwds.size() > 3) {
            /*手势登录密码不为空  直接跳转  否则设置*/
            if (loginPwd != null && !loginPwd.equals("")) {
                if (loginPwd.equals(String.valueOf(pwds))) {
                    Toast.makeText(this, "开始重新设置", Toast.LENGTH_SHORT).show();
//                    /*把旧的  置成空*/
                    new SharedPreferencesUtil().setLoginPwd(LoginShouSureActivity.this, String.valueOf(""));
                    loginPwd = new SharedPreferencesUtil().getLoginPwd(this);
                    idGesturePwd.changePwdState(PointState.POINT_STATE_NORMAL, 0);
                } else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    idGesturePwd.changePwdState(PointState.POINT_STATE_NORMAL, 0);
                }
            } else {
                StringBuffer sbPwd = new StringBuffer();
                for (Integer pwd : pwds) {
                    sbPwd.append(pwd);
                }
                idIndicator.setText(sbPwd.toString());
                if (pwds != null && pwds.size() > 0) {
                    indicatorView.setPwds(pwds);
                }
                if (count++ == 0) {
                    pwd = sbPwd.toString();
                    Toast.makeText(this, "请再次绘制手势密码", Toast.LENGTH_SHORT).show();
                    idGesturePwd.changePwdState(PointState.POINT_STATE_NORMAL, 0);
                } else {
                    count = 0;
                    if (pwd.equals(sbPwd.toString())) {
                        Toast.makeText(this, "密码重置成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        /*保存手势密码*/
                        new SharedPreferencesUtil().setLoginPwd(LoginShouSureActivity.this, String.valueOf(pwds));
                    } else {
                        Toast.makeText(this, "两次密码不一致，请重新绘制", Toast.LENGTH_SHORT).show();
                        indicatorView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_shake));
                        count = 0;
                        idGesturePwd.changePwdState(PointState.POINT_STATE_ERRO, 0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                idGesturePwd.changePwdState(PointState.POINT_STATE_NORMAL, 0);
                            }
                        }, 1000);
                    }
                }
            }
        } else {
            Toast.makeText(this, "密码不能小于四位数", Toast.LENGTH_SHORT).show();
        }
    }
}

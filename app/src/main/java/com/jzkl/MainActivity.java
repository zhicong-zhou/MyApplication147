package com.jzkl;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzkl.Base.BaseActivity;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.util.SharedPreferencesUtil;
import com.jzkl.util.newinfo.ActivityCollector;
import com.jzkl.view.activity.IntergrterSuccessActivity;
import com.jzkl.view.activity.LoginActivity;
import com.jzkl.view.activity.WelcomeActivity;
import com.jzkl.view.fragment.HomeFragment;
import com.jzkl.view.fragment.IncomeFragment;
import com.jzkl.view.fragment.MyFragment;
import com.jzkl.view.fragment.ShopFragment;
import com.jzkl.view.jpush.ExampleUtil;
import com.jzkl.view.jpush.LocalBroadcastManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.iv_menu_home)
    ImageView ivMenuHome;
    @BindView(R.id.tv_menu_home)
    TextView tvMenuHome;
    @BindView(R.id.ll_menu_home)
    LinearLayout llMenuHome;
    @BindView(R.id.iv_menu_hot)
    ImageView ivMenuHot;
    @BindView(R.id.tv_menu_hot)
    TextView tvMenuHot;
    @BindView(R.id.ll_menu_hot)
    LinearLayout llMenuHot;
    @BindView(R.id.iv_menu_talk)
    ImageView ivMenuTalk;
    @BindView(R.id.tv_menu_talk)
    TextView tvMenuTalk;
    @BindView(R.id.ll_menu_talk)
    LinearLayout llMenuTalk;
    @BindView(R.id.iv_menu_me)
    ImageView ivMenuMe;
    @BindView(R.id.tv_menu_me)
    TextView tvMenuMe;
    @BindView(R.id.ll_menu_me)
    LinearLayout llMenuMe;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;


    // 布局管理器
    private FragmentManager fManager;
    private int vID;
    private HomeFragment fragment_home;
    private IncomeFragment fragment_income;
    private ShopFragment fragment_shop;
    private MyFragment fragment_me;
    private long exitTime = 0;
    String status,loginFirst;
    public static boolean isForeground = false;
    String userinfo,userId;

    @Override
    protected void initView() {
        /*接收 初始化*/
        EventBus.getDefault().register(this);
        loginFirst = getIntent().getStringExtra("loginFirst");
        // 初始化组件
        initViews();
        // 默认先点中第一个“首页”
        clickMenu(llMenuHome);

        if(loginFirst!=null){
            if(!loginFirst.equals("0")){
                popWin();
            }
        }
    }


    private void popWin() {
        //加载布局并初始化组件
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_login_reward, null);
        ImageView login_reward_img = dialogView.findViewById(R.id.login_reward_img);
        TextView login_reward_num = dialogView.findViewById(R.id.login_reward_num);
        Button login_reward_but = dialogView.findViewById(R.id.login_reward_but);
        /*积分*/
        login_reward_num.setText(loginFirst);
        final AlertDialog.Builder layoutDialog = new AlertDialog.Builder(this);
        /*获取屏幕宽高*/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double width = metrics.widthPixels * 0.9;
        double height = metrics.heightPixels * 0.4;

        layoutDialog.setView(dialogView);

        final AlertDialog dialog = layoutDialog.create();
        dialog.show();
        /*设置弹框宽度*/
        dialog.getWindow().setLayout((int) width, ViewGroup.LayoutParams.WRAP_CONTENT);

        /*确定*/
        login_reward_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,IntergrterSuccessActivity.class);
//                startActivity(intent);
                dialog.dismiss();
            }
        });
        /*取消*/
        login_reward_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /*邀请推广切换*/
    @Subscribe
    public void  onEvent(EventsWIFI event){
        status =event.getWifiStatus();
        if(status.equals("1")){
            FragmentTransaction trans = fManager.beginTransaction();
            /*首页*/
            ivMenuHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_select));
            tvMenuHome.setTextColor(getResources().getColor(R.color.home_word));
            /*收益*/
            ivMenuHot.setImageDrawable(getResources().getDrawable(R.mipmap.income_selected));
            tvMenuHot.setTextColor(getResources().getColor(R.color.home_worded));
            /*商城*/
            ivMenuTalk.setImageDrawable(getResources().getDrawable(R.mipmap.shop_select));
            tvMenuTalk.setTextColor(getResources().getColor(R.color.home_word));
            /*我的*/
            ivMenuMe.setImageDrawable(getResources().getDrawable(R.mipmap.my_select));
            tvMenuMe.setTextColor(getResources().getColor(R.color.home_word));

            // 隐藏所有的fragment
            hideFrament(trans);
            // 设置Fragment
            fragment_income = new IncomeFragment();
            trans.add(R.id.content, fragment_income);
            trans.commit();
        }
    }

    private void initViews() {
        // 布局管理器
        fManager = getSupportFragmentManager();
    }

    /**
     * 点击底部菜单事件
     *
     * @param v
     */
    public void clickMenu(View v) {
        FragmentTransaction trans = fManager.beginTransaction();
        vID = v.getId();
        // 设置menu样式
        setMenuStyle(vID);
        // 隐藏所有的fragment
        hideFrament(trans);
        // 设置Fragment
        setFragment(vID, trans);
        trans.commit();
    }

    /**
     * 设置menu样式
     *
     * @param vID
     */
    private void setMenuStyle(int vID) {
        // 首页
        if (vID == R.id.ll_menu_home) {
            ivMenuHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_selected));
            tvMenuHome.setTextColor(getResources().getColor(R.color.home_worded));
        } else {
            ivMenuHome.setImageDrawable(getResources().getDrawable(R.mipmap.home_select));
            tvMenuHome.setTextColor(getResources().getColor(R.color.home_word));
        }
        //收益
        if (vID == R.id.ll_menu_hot) {
            ivMenuHot.setImageDrawable(getResources().getDrawable(R.mipmap.income_selected));
            tvMenuHot.setTextColor(getResources().getColor(R.color.home_worded));
        } else {
            ivMenuHot.setImageDrawable(getResources().getDrawable(R.mipmap.income_select));
            tvMenuHot.setTextColor(getResources().getColor(R.color.home_word));
        }
        // 商城
        if (vID == R.id.ll_menu_talk) {
            ivMenuTalk.setImageDrawable(getResources().getDrawable(R.mipmap.shop_selected));
            tvMenuTalk.setTextColor(getResources().getColor(R.color.home_worded));
        } else {
            ivMenuTalk.setImageDrawable(getResources().getDrawable(R.mipmap.shop_select));
            tvMenuTalk.setTextColor(getResources().getColor(R.color.home_word));
        }
        // 我的
        if (vID == R.id.ll_menu_me) {
            ivMenuMe.setImageDrawable(getResources().getDrawable(R.mipmap.my_selected));
            tvMenuMe.setTextColor(getResources().getColor(R.color.home_worded));
        } else {
            ivMenuMe.setImageDrawable(getResources().getDrawable(R.mipmap.my_select));
            tvMenuMe.setTextColor(getResources().getColor(R.color.home_word));
        }
    }

    /**
     * 隐藏所有的fragment(编程初始化状态)
     *
     * @param trans
     */
    private void hideFrament(FragmentTransaction trans) {
        if (fragment_home != null) {
            trans.hide(fragment_home);
        }
        if (fragment_income != null) {
            trans.hide(fragment_income);
        }
        if (fragment_shop != null) {
            trans.hide(fragment_shop);
        }
        if (fragment_me != null) {
            trans.hide(fragment_me);
        }
    }

    /**
     * 设置Fragment
     *
     * @param vID
     * @param trans
     */
    private void setFragment(int vID, FragmentTransaction trans) {
        switch (vID) {
            case R.id.ll_menu_home:
                if (fragment_home == null) {
                    fragment_home = new HomeFragment();
                    trans.add(R.id.content, fragment_home);
                } else {
                    trans.show(fragment_home);
                }
                break;
            /*收益*/
            case R.id.ll_menu_hot:
                if (fragment_income == null) {
                    fragment_income = new IncomeFragment();
                    trans.add(R.id.content, fragment_income);
                } else {
                    trans.show(fragment_income);
                }
                break;
            /*商城*/
            case R.id.ll_menu_talk:
                if (fragment_shop == null) {
                    fragment_shop = new ShopFragment();
                    trans.add(R.id.content, fragment_shop);
                } else {
                    trans.show(fragment_shop);
                }
                break;
            /*我的*/
            case R.id.ll_menu_me:
                if (fragment_me == null) {
                    fragment_me = new MyFragment();
                    trans.add(R.id.content, fragment_me);
                } else {
                    trans.show(fragment_me);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    /*退出*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - exitTime < 2000) {
                ActivityCollector.exitApp();
            } else {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 || resultCode == 2) {

        }
    }
}

package com.jzkl.Base;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.util.KeyBoardListener;
import com.jzkl.util.newinfo.ActivityCollector;
import com.jzkl.util.newinfo.NetWorkUtils;
import com.jzkl.util.newinfo.RxBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;

public abstract class BaseActivity extends AppCompatActivity {

    AlertDialog dialog;

    private Disposable disposable;
    private AlertDialog.Builder builder;
    private boolean isShow, isSuccess;
    private Unbinder unbinder;

    private InputMethodManager imm;
    protected ImmersionBar mImmersionBar;

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        if (isImmersionBarEnabled())
            initImmersionBar();
        if(savedInstanceState == null) {
            //初始化沉浸式
            setContentView(getLayoutRes());
            unbinder =  ButterKnife.bind(this);
            KeyBoardListener.getInstance(this).init();
            initView();
            initData();
        }

        ActivityCollector.addActivity(this);
        onEvents();
    }

    protected abstract void initView();
    protected abstract int getLayoutRes();
    protected abstract void initData();

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    /*系统字体*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /*=====================网络=======================*/
    @Override
    protected void onResume() {
        super.onResume();
//      Log.d("AAAA", "isshow:" + isShow);
        if (isShow && !isSuccess) {
            dialog.show();
        }
    }
    public void onEvents() {
        if (!NetWorkUtils.isNetworkConnected(this)) {
            showSetNetworkUI(this);
        }

        disposable = RxBus.get().toFlowable(EventsWIFI.class)
                .subscribeWith(new ResourceSubscriber<EventsWIFI>() {
                    @Override
                    public void onNext(EventsWIFI wifiEvents) {
                        String wifi = wifiEvents.getWifiStatus();
                        if (wifi.equals("1")) {
                            if (dialog != null && dialog.isShowing()) {
                                isSuccess = true;
                                dialog.dismiss();
                            }
                        } else if (wifi.equals("2")) {
                            if (dialog != null && !dialog.isShowing()) {
                                isSuccess = false;
                                showSetNetworkUI(BaseActivity.this);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
//                        Log.e("RxJavaError：", t.getMessage());
                        onEvents();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
    /*
    * 打开设置网络界面
    */
    public void showSetNetworkUI(final Context context) {
        // 提示对话框
        if (builder == null) {
            builder = new AlertDialog.Builder(this)
                    .setTitle("网络设置提示")
                    .setMessage("网络连接不可用,是否进行设置?")
                    .setCancelable(false)
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent = null;
                            // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                                intent = new Intent(
                                        android.provider.Settings.ACTION_WIFI_SETTINGS);
                            } else {
                                intent = new Intent();
                                ComponentName component = new ComponentName(
                                        "com.android.settings",
                                        "com.android.settings.WirelessSettings");
                                intent.setComponent(component);
                                intent.setAction("android.intent.action.VIEW");
                            }

                            if (dialog!=null) {
                                if (((AlertDialog)dialog).isShowing()) {
                                    isShow = true;
                                } else {
                                    isShow = false;
                                }
                            }
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        dialog = builder.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁

        if (disposable != null) {
            disposable.dispose();
        }
        /*注销广播*/
        if (ActivityCollector.getSize() == 1) {
            ActivityCollector.exitApp();
        } else {
            ActivityCollector.removeActivity(this);
        }
        if(unbinder!=null){
            unbinder.unbind();
        }
    }
}

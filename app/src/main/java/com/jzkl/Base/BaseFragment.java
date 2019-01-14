package com.jzkl.Base;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.jzkl.Bean.EventsWIFI;
import com.jzkl.util.newinfo.NetWorkUtils;
import com.jzkl.util.newinfo.RxBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by admin on 2018/6/7.
 */

public abstract class BaseFragment extends Fragment {
    private View view;
    AlertDialog dialog;
    private Disposable disposable;
    private AlertDialog.Builder builder;
    private boolean isShow, isSuccess;
    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;
    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(setLayoutId(), container, false);
        }
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if (isImmersionBarEnabled())
            initImmersionBar();
        initData();
        initView();
        setListener();
        onEvents();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mImmersionBar != null)
            mImmersionBar.init();
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }


    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * view与数据绑定
     */
    protected void initView() {

    }

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
    }


    /*=====================网络=======================*/
    @Override
    public void onResume() {
        super.onResume();
//      Log.d("AAAA", "isshow:" + isShow);
        if (isShow && !isSuccess) {
            dialog.show();
        }
    }
    public void onEvents() {
        if (!NetWorkUtils.isNetworkConnected(getActivity())) {
            showSetNetworkUI(getActivity());
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
                                showSetNetworkUI(getActivity());
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
            builder = new AlertDialog.Builder(getActivity())
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
    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        unbinder.unbind();
        super.onDestroy();
    }
}

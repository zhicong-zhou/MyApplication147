package com.jzkl.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import com.baidu.mapapi.SDKInitializer;
import com.jzkl.R;
import com.jzkl.receiver.NetWorkStatusReceiver;
import com.jzkl.util.ToastUtil;
import com.jzkl.view.activity.WelcomeActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import cn.jpush.android.api.JPushInterface;


public class ApplicationDB extends Application {

    private NetWorkStatusReceiver receiver;
    private static ApplicationDB mInstance;
    private static final String TAG = "JIGUANG-Example";
    public static final String APP_ID = "8e71a1ae6b"; // TODO 替换成bugly上注册的appid
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

//        //做初始化Toast类的
        ToastUtil.init(getApplicationContext());

//        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        ImageLoader.getInstance().init(configuration);
//
        initLeakCanary();
        /*注册广播*/
        receiver = new NetWorkStatusReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(receiver, filter);

        /*极光*/
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
//        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
//        SpeechUtility.createUtility(ApplicationDB.this, "appid=" + getString(R.string.app_id));

        /***
         * 初始化定位sdk，建议在Application中创建
         */
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

        /**** Beta高级设置*****/
        /**
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        Beta.autoInit = true;
        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true;

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.initDelay = 1 * 1000;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.mipmap.ic_launcher;
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher;
        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = false;
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(WelcomeActivity.class);
        /*
         * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
         * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
         * 参数1： applicationContext
         * 参数2：appId
         * 参数3：是否开启debug*/
        Bugly.init(getApplicationContext(), APP_ID, false);
    }

    @Override
    protected void attachBaseContext(Context base) {
        // 在这里调用Context的方法会崩溃
        super.attachBaseContext(base);
        // 4.4的系统启动会崩溃  加这个
        MultiDex.install(this);
    }

    public static ApplicationDB getInstance() {
        return mInstance;
    }

    public void unRegistReceiver() {
        if (receiver!=null) {
            unregisterReceiver(receiver);
        }
    }
    /* 初始化内存泄漏检测*/
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}

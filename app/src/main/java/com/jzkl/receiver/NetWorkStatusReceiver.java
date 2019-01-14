package com.jzkl.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jzkl.Bean.EventsWIFI;
import com.jzkl.util.newinfo.RxBus;


/**
 * 广播接收器  接收网络是否打开
 * Created by Administrator on 2017/10/10.
 */

public class NetWorkStatusReceiver extends BroadcastReceiver {

    public NetWorkStatusReceiver() {

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AAAA", "接受");
//      监听网络连接的设置，包括wifi和移动数据的打开和关闭，接受此广播会慢一些
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            ConnectivityManager manager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                if (networkInfo.isConnected()) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // wifi可用
//                        EventBus.getDefault().register(new EventsWIFI("1"));
                        Log.d("AAAA", "发射");
                        RxBus.get().post(new EventsWIFI("1"));
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // 移动网络可用
//                        EventBus.getDefault().register(new EventsWIFI("1"));
                        Log.d("AAAA", "发射");
                        RxBus.get().post(new EventsWIFI("1"));
                    }
                } else {
                    // 没有网络连接
//                    EventBus.getDefault().register(new EventsWIFI("2"));
                    Log.d("AAAA", "发射");
                    RxBus.get().post(new EventsWIFI("2"));
                }
            } else {
                // 当前没有网络连接/没有开启网络
                Log.d("AAAA", "发射");
                RxBus.get().post(new EventsWIFI("2"));
            }
        }
    }
}

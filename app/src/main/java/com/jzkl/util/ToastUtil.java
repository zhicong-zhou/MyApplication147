package com.jzkl.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Android on 2016/8/1.
 */
public class ToastUtil {

    public static Context context;
    private static long time;

    public static void init(Context c) {
        context = c;
        time = System.currentTimeMillis();
    }

    /**
     * Toast工具类
     *
     * @param string
     */
    public static void show(String string) {
        //判断如果在2400毫秒内Toast过则不会Toast
        if (System.currentTimeMillis() - time > 2400 || System.currentTimeMillis() - time < -2400) {
            Toast toast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
            //设置弹出位置，此处水平居中
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            time = System.currentTimeMillis();
        }
    }
}
